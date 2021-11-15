package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "文件上传的Controller",tags = {"文件上传的Controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file)throws Exception;

    /**
     *文件上传到MongoDB的gridfs中
     */
    @PostMapping("/uploadToGridFS")
    GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO newAdminBO)throws Exception;

    @GetMapping("/readInGridFS")
    void readInGridFS(String faceId,
                                 HttpServletRequest request,
                                 HttpServletResponse response)throws Exception;
    @GetMapping("/readFace64InGridFS")
    GraceJSONResult readFace64InGridFS(String faceId,
                      HttpServletRequest request,
                      HttpServletResponse response)throws Exception;

}
