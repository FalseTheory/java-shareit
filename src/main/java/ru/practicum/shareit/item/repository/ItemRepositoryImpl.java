package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository{

    private final Map<Long, Item> items;
    private long idCount;

    public ItemRepositoryImpl() {
        items = new HashMap<>();
        idCount = 0;
    }

    @Override
    public Item create(Item item, Long userId) {
        item.setOwnerId(userId);
        item.setId(++idCount);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item update(Long userId, Item item) {
        Item updatedItem = items.get(item.getId());
        if(item.getDescription()!=null) {
            updatedItem.setDescription(item.getDescription());
        }
        if(item.getAvailable()!=null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        if(item.getName()!=null) {
            updatedItem.setName(item.getName());
        }
        return updatedItem;

    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .toList();
    }

    @Override
    public List<Item> search(Long userId, String text) {
        if(text.isEmpty()){
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(item ->
                        (item.getDescription().contains(text)
                                || item.getName().contains(text)))
                .toList();
    }
}
