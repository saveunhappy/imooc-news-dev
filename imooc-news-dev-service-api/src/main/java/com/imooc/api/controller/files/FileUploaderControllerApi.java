package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件上传的Controller",tags = {"文件上传的Controller"})
@RequestMapping("fs")
public interface FileUploaderControllerApi {
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "C")
    @PostMapping("/uploadFace")
    GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file)throws Exception;
}
