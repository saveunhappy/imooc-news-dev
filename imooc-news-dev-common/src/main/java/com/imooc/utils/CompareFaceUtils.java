package com.imooc.utils;
import com.aliyun.tea.*;
import com.aliyun.facebody20191230.*;
import com.aliyun.facebody20191230.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.facebody.model.v20191230.CompareFaceRequest;
import com.aliyuncs.facebody.model.v20191230.CompareFaceResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.extend.AliyunResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CompareFaceUtils {
    @Resource
    private AliyunResource aliyunResource;
    public void faceVerify(String face1, String face2, Float targetConfidence) {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-shanghai",
                aliyunResource.getAccessKeyID(),
                aliyunResource.getAccessKeySecret());
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        IAcsClient client = new DefaultAcsClient(profile);

        CompareFaceRequest request = new CompareFaceRequest();
        request.setQualityScoreThreshold(targetConfidence);
//        request.setImageDataA("http://122.152.205.72:88/group1/M00/00/05/CpoxxF5MvvGAfnLXAAIHiv37wNk363.jpg");
//        request.setImageDataB("http://122.152.205.72:88/group1/M00/00/05/CpoxxF5Mv3yAH74mAACOiTd9pO4462.jpg");
        request.setImageDataA(face1);
        request.setImageDataB(face2);

        try {
            CompareFaceResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }

}
