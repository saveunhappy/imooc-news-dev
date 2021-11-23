package com.imooc.article.controller;

import com.imooc.api.controller.article.ArticleHTMLControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;

@RestController
public class ArticleHTMLController implements ArticleHTMLControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(ArticleHTMLController.class);
    @Resource
    private GridFSBucket gridFSBucket;
    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public Integer download(String articleId, String articleMongoId) {
        String path = articlePath + File.separator + articleId + ".html";
        File file = new File(path);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            gridFSBucket.downloadToStream(new ObjectId(articleMongoId), outputStream);

        } catch (IOException e) {
            e.printStackTrace();
            GraceException.display(ResponseStatusEnum.SYSTEM_FILE_NOT_FOUND);
        }
        return HttpStatus.OK.value();
    }
}
