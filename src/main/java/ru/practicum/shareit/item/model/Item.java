package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.User;


@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String description;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;
    @Column(nullable = false)
    Boolean available;
}
