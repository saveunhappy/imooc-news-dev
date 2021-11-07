package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {
    //FastDFS
    String uploadFdfs(MultipartFile file, String fileExtName) throws IOException;
    //oss
    String uploadOSS(MultipartFile file,String userId, String fileExtName) throws Exception;
}
