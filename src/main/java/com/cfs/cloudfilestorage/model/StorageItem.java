package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    @Column(unique = true)
    private String name;

    @Column(name = "item_name", unique = true)
    private String path;

    @Column(name = "content_type", nullable = false)
    private String contentType = "folder";

    @Column(name = "last_modified", nullable = false)
    private Timestamp lastModified;

    @Column(name = "item_size", nullable = false)
    private int itemSize;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;
}
