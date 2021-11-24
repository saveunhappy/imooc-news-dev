package com.imooc.article.controller;

import com.imooc.pojo.Article;
import com.imooc.pojo.Spouse;
import com.imooc.pojo.Stu;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/free")
public class FreemarkerController {
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerController.class);
    @Value("${freemarker.html.target}")
    private String htmlTarget;
    @ResponseBody
    @GetMapping("/createHtml")
    public String createHtml(Model model) throws Exception {
        //0.配置freemarker基本环境
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classPath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(makePathAndSubstring(classPath + "templates")));
        System.out.println(htmlTarget.replace("\\",File.separator));
        System.out.println(makePathAndSubstring(classPath + "templates"));
        String stranger = "world";
        model.addAttribute("there",stranger);
        //1.获得现有的模板ftl文件
        Template template = cfg.getTemplate("stu.ftl", StandardCharsets.UTF_8.name());
        //2.获取动态数据
        model = makeModel(model);
        //3.融合动态数据ftl,生成HTML
        File tempDic = new File(htmlTarget);
        if(!tempDic.exists()){
            tempDic.mkdirs();
        }
        Writer out = new FileWriter(htmlTarget + File.separator + "10010.html");
        template.process(model,out);
        out.close();
        return "ok";
    }

    private String makePathAndSubstring(String path){
        return path.substring(1).replace("/",File.separator);
    }
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
