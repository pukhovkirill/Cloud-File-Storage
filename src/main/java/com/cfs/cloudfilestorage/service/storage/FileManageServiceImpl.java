package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class FileManageServiceImpl extends BaseManager implements FileManageService{
    private final FileRepository fileRepository;

    public FileManageServiceImpl(PersonService personService, FileRepository fileRepository){
        super(personService);
        this.fileRepository = fileRepository;
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FileDto item) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        var optionalPerson = findPerson();

        try{
            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            var client = MinioUtility.getClient();

            var objectName = String.format("user-%d-files/%s", person.getId(), item.getName());

            client.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .filename(item.getFileName())
                            .contentType(item.getContentType())
                            .build());

            MinioUtility.releaseClient(client);

            var file = new File();
            file.setName(item.getName());
            file.setFileName(item.getFileName());
            file.setFileSize(item.getFileSize());
            file.setContentType(item.getContentType());
            file.setLastModified(item.getLastModified());
            file.setOwner(person);

            file = fileRepository.save(file);

            person.getAvailableFiles().add(file);

            personService.updatePerson(person);

        }catch (MinioException e){
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    @Override
    public void rename(FileDto item, String newName) {

    }

    @Override
    public FileDto download(String name, String saveAsName) {
        return null;
    }

    @Override
    public void remove(FileDto item) {

    }

    @Override
    public void share(FileDto item) {

    }
}
