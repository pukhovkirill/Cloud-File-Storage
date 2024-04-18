package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "shared_item")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private StorageItem item;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared = false;
}

