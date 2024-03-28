package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.model.Folder;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DBUploadFolderCommand extends StorageCommand<FolderDto> {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileRepository fileRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(entity instanceof FolderDto item){
            var optionalPerson = findPerson();

            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(item.getPath()))) {
                var folderName = Paths.get(item.getPath()).getFileName().toString();

                var folder = new Folder(
                        null,
                        folderName,
                        person,
                        new ArrayList<>()
                );

                for (Path path : stream) {
                    if (Files.isDirectory(path))
                        continue;

                    var fileName = path.getFileName().toString();
                    var file = new File(
                            null,
                            folderName+"/"+fileName,
                            path.toUri().getPath(),
                            Files.probeContentType(path),
                            new Timestamp(System.currentTimeMillis()),
                            Files.size(path), person
                    );

                    file = fileRepository.save(file);

                    folder.getContent().add(file);
                }

                folderRepository.save(folder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
