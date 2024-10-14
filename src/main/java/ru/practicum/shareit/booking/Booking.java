package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bookings")
/*@NamedEntityGraph(name = "booking_entity_graph",
attributeNodes = {@NamedAttributeNode("item"), @NamedAttributeNode("booker")} )*/
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    Item item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    User booker;
    @Column(nullable = false)
    BookingStatus status;
    @Column(nullable = false, name = "start_time")
    LocalDateTime start;
    @Column(nullable = false, name = "end_time")
    LocalDateTime end;


}
