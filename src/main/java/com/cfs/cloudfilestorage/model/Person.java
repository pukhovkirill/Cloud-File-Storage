package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "person")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "item_to_person",
            joinColumns = { @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false) }
    )
    private List<StorageItem> availableItems;
}
