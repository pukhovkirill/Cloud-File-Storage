package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Table(name = "item")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String path;

    @Column(name = "content_type", nullable = false)
    private String contentType = "folder";

    @Column(name = "last_modified", nullable = false)
    private Timestamp lastModified;

    @Column(name = "item_size", nullable = false)
    private int itemSize;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "availableItems")
    private List<Person> people;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private SharedItem sharedItem;
}
