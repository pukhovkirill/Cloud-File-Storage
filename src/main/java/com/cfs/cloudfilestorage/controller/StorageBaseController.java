package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StorageBaseController {

    protected final ItemManageService itemManageService;
    protected final PathManageService pathManageService;
    protected final PathConvertService pathConvertService;
    protected final AuthorizedPersonService authorizedPersonService;

    public StorageBaseController(PathManageService pathManageService,
                            ItemManageService itemManageService,
                            PathConvertService pathConvertService,
                            AuthorizedPersonService authorizedPersonService) {
        this.itemManageService = itemManageService;
        this.pathManageService = pathManageService;
        this.pathConvertService = pathConvertService;
        this.authorizedPersonService = authorizedPersonService;
    }

    protected Person findPerson(){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        return optPerson.get();
    }

    private BigDecimal computeTotalSizePercent(long usedSpaceBytes, long totalDiskSpaceBytes) {
        BigDecimal usedSpaceGB = convertToGigabytes(usedSpaceBytes);
        BigDecimal totalDiskSpaceGB = convertToGigabytes(totalDiskSpaceBytes);

        usedSpaceGB = usedSpaceGB.divide(totalDiskSpaceGB, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        return usedSpaceGB.setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal convertToGigabytes(long totalSize) {
        BigDecimal bytes = BigDecimal.valueOf(totalSize);
        bytes = bytes.divide(BigDecimal.valueOf(1024 * 1024 * 1024), 4, RoundingMode.HALF_UP);
        bytes = bytes.setScale(2, RoundingMode.HALF_EVEN);
        return bytes;
    }

    private long computeSize(Person person){
        long totalSize = 0;
        for(var item : person.getAvailableItems()){
            if(!item.getContentType().equals("folder")){
                totalSize += item.getItemSize();
            }
        }

        return totalSize;
    }

    protected StorageUsage computeStorageUsage(Person person){
        var size = computeSize(person);
        var sizeInGigabytes = convertToGigabytes(size);
        var percentUsage = computeTotalSizePercent(size, 15L * 1024 * 1024 * 1024);

        return StorageUsage.builder()
                .percent(String.format("%.2f", sizeInGigabytes))
                .totalSize(String.format("%.2f", percentUsage))
                .build();
    }

    @Setter
    @Getter
    @Builder
    public static class StorageUsage{
        private String percent;
        private String totalSize;
    }
}
