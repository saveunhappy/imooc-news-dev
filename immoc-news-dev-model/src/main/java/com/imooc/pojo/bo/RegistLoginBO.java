package com.imooc.pojo.bo;

import javax.validation.constraints.NotEmpty;

public class RegistLoginBO {
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    @NotEmpty(message = "短信验证码不能为空")
    private String smsCode;

    @Override
    public String toString() {
        return "RegistLoginBO{" +
                "mobile='" + mobile + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
