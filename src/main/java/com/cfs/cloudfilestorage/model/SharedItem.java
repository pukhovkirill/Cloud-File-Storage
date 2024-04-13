package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "shared_item")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false, unique = true)
    private StorageItem item;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared = false;
}

