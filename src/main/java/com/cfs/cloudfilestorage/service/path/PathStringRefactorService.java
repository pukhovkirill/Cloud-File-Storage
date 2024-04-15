package com.cfs.cloudfilestorage.service.path;

public interface PathStringRefactorService {

    String addBackSlash(String path);

    String removeBackSlash(String path);

    String getCleanName(String path);

    String getOriginalName(String path);

    String getParent(String path);

    String addRoot(String path);

    String getPersonRootFolder();

    String subtraction(String minuend, String subtrahend);

    String replaceLast(String string, String toReplace, String replacement);
}
