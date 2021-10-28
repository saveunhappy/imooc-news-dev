package com.imooc.utils;

import com.imooc.utils.extend.TencentResource;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.stereotype.Component;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import javax.annotation.Resource;

@Component
public class SMSUtils {
    @Resource
    private TencentResource tencentResource;
    public static final String signName = "江涛的小屋";

    public void sendSMS(String mobile, String code) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(tencentResource.getSecretId(),
                    tencentResource.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-nanjing");
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {mobile};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(tencentResource.getSmsSdkAppId());
            req.setSignName(signName);
            req.setTemplateId(tencentResource.getTemplateId());

            String[] templateParamSet1 = {code};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
//    public void sendSMS(String mobile, String code) {
//        DefaultProfile profile = DefaultProfile.getProfile(
//                "cn-shanghai",
//                aliyunResource.getAccessKeyID(),
//                aliyunResource.getAccessKeySecret());
//        /** use STS Token
//         DefaultProfile profile = DefaultProfile.getProfile(
//         "<your-region-id>",           // The region ID
//         "<your-access-key-id>",       // The AccessKey ID of the RAM account
//         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
//         "<your-sts-token>");          // STS Token
//         **/
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        CommonRequest request = new CommonRequest();
//        request.setSysMethod(MethodType.POST);
//        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setSysVersion("2017-05-25");
//        request.setSysAction("SendSms");
//        request.putQueryParameter("PhoneNumbers", mobile);
//        request.putQueryParameter("SignName", "456");
//        request.putQueryParameter("TemplateCode", "789");
//        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
//
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendSMS(String mobile, String code) {
//        try {
//            Credential cred = new Credential(aliyunResource.getAccessKeyID(),
//                    aliyunResource.getAccessKeySecret());
//            CvmClient client = new CvmClient(cred, "ap-shanghai");
//
//            DescribeInstancesRequest req = new DescribeInstancesRequest();
//            DescribeInstancesResponse resp = client.DescribeInstances(req);
//
//            System.out.println(DescribeInstancesResponse.toJsonString(resp));
//        } catch (TencentCloudSDKException e) {
//            System.out.println(e.toString());
//        }
//    }
}
