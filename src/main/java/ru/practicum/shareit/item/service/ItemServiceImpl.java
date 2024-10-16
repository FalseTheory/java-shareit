package ru.practicum.shareit.item.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto create(ItemCreateDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));

        Item item = itemMapper.mapCreateDtoToItem(itemDto);
        item.setOwner(user);
        return itemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long itemId, Long userId) {
        return itemRepository.findById(itemId)
                .map(item -> {
                            List<Booking> bookings = null;
                            if (item.getOwner().getId().equals(userId)) {
                                bookings = bookingRepository.findBookingsForItem(itemId);
                            }
                            return itemMapper.mapToItemDto(item, bookings,
                                    commentRepository.findItemComments(item.getId()).stream()
                                            .map(commentMapper::mapToCommentDto).toList());
                        }

                )
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemId + " не найден"));
    }

    @Override
    @Transactional
    public ItemDto update(ItemUpdateDto itemDto) {
        userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + itemDto.getOwnerId() + " не найден"));
        Item updatedItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemDto.getId() + " не найден"));

        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }

        return itemMapper.mapToItemDto(itemRepository.save(updatedItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsForUser(Long userId) {

        List<Item> items = itemRepository.getUserItems(userId);
        List<Long> ids = items.stream().map(Item::getId).toList();
        List<Booking> bookingList = bookingRepository.findOwnerBookings(userId);
        List<CommentDto> commentList = commentRepository.findAllById(ids).stream()
                .map(commentMapper::mapToCommentDto)
                .toList();
        Map<Long, List<Booking>> bookingsMap = new HashMap<>();
        for (Long id : ids) {
            bookingsMap.put(id, new ArrayList<>());
        }
        for (Booking booking : bookingList) {
            Long id = booking.getItem().getId();
            if (ids.contains(id)) {
                bookingsMap.get(id).add(booking);
            }
        }

        return itemRepository.getUserItems(userId).stream()
                .map(item -> itemMapper.mapToItemDto(item, bookingsMap.get(item.getId()), commentList))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAvailableItems(Long userId, String text) {
        return itemRepository.search(userId, text).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto postComment(CommentCreateDto commentCreateDto) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(commentCreateDto.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + commentCreateDto.getUserId() + " не найден"));
        Item item = itemRepository.findById(commentCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + commentCreateDto.getItemId() + " не найден"));


        if (bookingRepository.findUserBookings(user.getId())
                .stream()
                .noneMatch(booking -> booking.getItem().getId().equals(item.getId())
                        && booking.getStatus() == BookingStatus.APPROVED
                        && booking.getEnd().isBefore(now))) {
            throw new ValidationException("Пользователь не брал в аренду предмет с id - " + item.getId()
                    + " или аренда еще не завершилась");
        }

        Comment comment = commentMapper.mapCreateDtoToComment(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(Timestamp.from(Instant.now()));
        comment = commentRepository.save(comment);


        return commentMapper.mapToCommentDto(comment);
    }


}
