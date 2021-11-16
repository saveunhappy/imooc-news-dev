package com.imooc.files.controller;

import com.google.common.io.Files;
import com.imooc.exception.GraceException;
import com.imooc.files.resource.FileResource;
import com.imooc.api.controller.files.FileUploaderControllerApi;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(FileUploaderController.class);
    @Resource
    private UploaderService uploaderService;
    @Resource
    private FileResource fileResource;
    @Resource
    private GridFSBucket gridFSBucket;
    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {
        String path = "";
        if (file != null) {
            //获取文件上传的名称
            String fileName = file.getOriginalFilename();
            // 判断文件名不能为空
            if (StringUtils.isNotBlank(fileName)) {
                String[] fileNameArr = fileName.split("\\.");
                // 获得后缀
                String suffix = fileNameArr[fileNameArr.length - 1];
                // 判断后缀符合我们的预定义规范
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")
                ) {
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

    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO newAdminBO) throws Exception {
        //获取图片的base64字符串
        String file64 = newAdminBO.getImg64();
        //将base64字符串转换为byte数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());
        //转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png", inputStream);
        //获得文件在gridfs中的主键
        String fileIdString = fileId.toString();
        return GraceJSONResult.ok(fileIdString);
    }

    @Override
    public void readInGridFS(String faceId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        //0.判空
        if(StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null")){
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        //1.从gridFs中存取
        File adminFace = readGridFSByFaceId(faceId);
        FileUtils.downloadFileByStream(response,adminFace);
    }

    @Override
    public GraceJSONResult readFace64InGridFS(String faceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //0.获得gridfs中人脸文件
        File myFace = readGridFSByFaceId(faceId);
        //1.转换人脸为base64
        String base64Face = FileUtils.encodeBase64(myFace);
        return GraceJSONResult.ok(base64Face);
    }

    private File readGridFSByFaceId(String faceId){
        //虽然在Navicat中看的是ID,但是官方文档中说如果你要是使用主键还是得使用_id
        GridFSFindIterable gridFSFiles = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFS = gridFSFiles.first();
        if(gridFS == null){
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }
        String filename = gridFS.getFilename();
        System.out.println(filename);
        File fileTemp = new File("/images/temp_face");
        if(!fileTemp.exists()){
            if(!fileTemp.mkdirs()){
                logger.error("创建文件失败");
            }
        }
        File myFile = new File("/images/temp_face/" + filename);
        //创建文件输出流
        try (OutputStream os = new FileOutputStream(myFile)) {
            gridFSBucket.downloadToStream(new ObjectId(faceId),os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myFile;
    }

}
