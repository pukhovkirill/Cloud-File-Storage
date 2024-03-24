package com.cfs.cloudfilestorage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "folder")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Person owner;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "folder_to_file",
            joinColumns = { @JoinColumn(name = "folder_id", referencedColumnName = "id", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false) }
    )
    private List<File> content;

}
