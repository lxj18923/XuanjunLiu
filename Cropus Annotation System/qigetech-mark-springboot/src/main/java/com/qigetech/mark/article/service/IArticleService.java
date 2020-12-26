package com.qigetech.mark.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.article.entity.Article;
import com.qigetech.mark.origin.entity.Origin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-4-29
 */
public interface IArticleService extends IService<Article> {
    Map<String, Object> uploadArticle(InputStream inputStream) throws IOException;
    boolean isChinese(String str);
    boolean fileIsOK(InputStream inputStream);
    boolean textIsOK(String text);
    void addAndUpdateArticle(Article article);
    void addAndUpdateOrigin(Origin origin);
}
