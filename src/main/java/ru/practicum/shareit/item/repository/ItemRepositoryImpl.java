package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    private final Map<Long, List<Item>> usersItems = new HashMap<>();
    private long idCount = 0;


    @Override
    public Item create(Item item) {
        item.setId(++idCount);
        items.put(item.getId(), item);
        List<Item> userItems = usersItems.get(item.getOwner().getId());
        if (userItems == null) {
            userItems = new ArrayList<>();
            userItems.add(item);
            usersItems.put(item.getOwner().getId(), userItems);
        } else {
            userItems.add(item);
        }

        return item;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);

        return item;

    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return usersItems.get(userId);
    }

    @Override
    public List<Item> search(Long userId, String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(item ->
                        ((item.getDescription().toLowerCase().contains(text.toLowerCase())
                                || item.getName().toLowerCase().contains(text.toLowerCase()))) && item.getAvailable())
                .toList();
    }
}
