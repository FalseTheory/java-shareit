package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
/*@NamedEntityGraph(name = "item.owner",
attributeNodes = @NamedAttributeNode("owner"))*/
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    User owner;
    @Column(nullable = false)
    Boolean available;
}
