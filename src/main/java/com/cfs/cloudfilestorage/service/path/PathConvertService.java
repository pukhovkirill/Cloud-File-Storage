package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.PathView;

public interface PathConvertService {

    String getFileName(String path);

    String getFullName(String path);

    String getParent(String path);

    PathView getPathView(String path);
}
