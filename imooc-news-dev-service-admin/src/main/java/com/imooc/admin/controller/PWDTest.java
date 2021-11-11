package com.imooc.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.File;

public class PWDTest {
    public static void main(String[] args) {
        System.out.println(File.separator);
        String admin = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println(admin);
    }
}
