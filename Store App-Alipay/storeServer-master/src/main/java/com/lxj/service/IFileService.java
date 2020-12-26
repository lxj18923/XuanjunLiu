package com.lxj.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by LXJ
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
