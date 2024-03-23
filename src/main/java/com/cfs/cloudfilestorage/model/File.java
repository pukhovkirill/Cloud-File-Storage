package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Table(name = "file")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "file_name", unique = true, nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "last_modified", nullable = false)
    private Timestamp lastModified;

    @Column(name = "file_size", nullable = false)
    private double fileSize;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Person owner;
}
