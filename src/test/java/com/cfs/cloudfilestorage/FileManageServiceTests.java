package com.cfs.cloudfilestorage;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.service.storage.Impl.DBFileManageService;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.MinioClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
public class FileManageServiceTests {

    @Autowired
    @Qualifier("BucketFileManager")
    FileManageService service;

    @Autowired
    MinioClient client;

    @Autowired
    MockMvc mockMvc;


    @MockBean
    DBFileManageService dbFileManageService;

    {
        service = Mockito.mock(DBFileManageService.class);
    }

    @Test
    void uploadTest() throws Exception {
        var fileDto = new FileDto();
        fileDto.setName("user-1-files/test_file.txt");
        fileDto.setFileName("/home/yukir/IdeaProjects/Cloud-File-Storage/src/test/java/com/cfs/cloudfilestorage/test_file.txt");
        fileDto.setContentType("text/txt");;
        fileDto.setFileSize(39);
        fileDto.setLastModified(new Timestamp(System.currentTimeMillis()));


        try (MockedStatic<MinioUtility> minioUtility = Mockito.mockStatic(MinioUtility.class)) {
            minioUtility.when(MinioUtility::getClient).thenReturn(client);
            service.upload(fileDto);
        }

        Assertions.assertTrue(true);
    }
}
