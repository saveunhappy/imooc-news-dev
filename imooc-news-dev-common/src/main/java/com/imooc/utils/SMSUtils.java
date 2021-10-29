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
            Credential cred = new Credential(tencentResource.getSecretId(),
                    tencentResource.getSecretKey());
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

            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

}
