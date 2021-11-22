package com.imooc.article.controller;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.Spouse;
import com.imooc.pojo.Stu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/free")
public class FreemarkerController {
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerController.class);
    @GetMapping("/hello")
    public Object hello(Model model){
        String stranger = "world";
        model.addAttribute("there",stranger);
        makeModel(model);
        return "stu";
    }
    private Model makeModel(Model model){
        Spouse spouse = new Spouse();
        spouse.setAge(22);
        spouse.setUsername("jack");
        Stu stu = new Stu();
        stu.setUid("333");
        stu.setAge(11);
        stu.setAmount(11f);
        stu.setBirthday(new Date());
        stu.setHaveChild(true);
        stu.setUsername("rose");
        stu.setSpouse(spouse);
        stu.setParents(getParents());
        stu.setArticleList(getArticles());
        model.addAttribute("stu",stu);
        return model;
    }

    private List<Article> getArticles() {
        Article article1 = new Article();
        article1.setId("1001");
        article1.setTitle("今天天气不错");

        Article article2 = new Article();
        article2.setId("1002");
        article2.setTitle("今天下雨了");

        Article article3 = new Article();
        article3.setId("1003");
        article3.setTitle("昨天下雨了");

        List<Article> list = new ArrayList<>();
        list.add(article1);
        list.add(article2);
        list.add(article3);
        return list;
    }

    private Map<String, String> getParents() {
        Map<String, String> parents = new HashMap<>();
        parents.put("father", "LiLei");
        parents.put("mother", "HanMeimei");
        return parents;
    }
}
