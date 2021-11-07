package com.imooc.files.controller;

import com.imooc.files.resource.FileResource;
import com.imooc.api.controller.files.FileUploaderControllerApi;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);
    @Resource
    private UploaderService uploaderService;
    @Resource
    private FileResource fileResource;
    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {
        String path = "";
        if (file != null) {
            //获取文件上传的名称
            String filename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(filename)) {
//                String[] fileNameArr = filename.split("\\.");
//                String suffix = fileNameArr[fileNameArr.length - 1];
                String suffix = getFileExtention(filename);
                //判断后缀符合我们的预定义规范
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                //执行上传
//                path = uploaderService.uploadFdfs(file, suffix);
                path = uploaderService.uploadOSS(file,userId,suffix);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
            }
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        logger.info("path = " + path);
        String finalpath = "";
        if (StringUtils.isNotBlank(path)) {
            finalpath = fileResource.getOssHost() + path;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);

        }
        return GraceJSONResult.ok(finalpath);
    }
    private static String getFileExtention(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
