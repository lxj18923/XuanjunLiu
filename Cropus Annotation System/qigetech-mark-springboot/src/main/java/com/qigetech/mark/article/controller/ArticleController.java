package com.qigetech.mark.article.controller;


import com.qigetech.mark.article.service.IArticleService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleServicelImpl;

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @PostMapping("/upload")
    public ResultBundle<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (articleServicelImpl.fileIsOK(file.getInputStream())){
            Map<String,Object> resultMap = articleServicelImpl.uploadArticle(file.getInputStream());
            resultMap.put("err", 0);
            resultMap.put("info", "文件上传成功！");
            return resultBundleBuilder.bundle("Add Article",()->
                    resultMap
            );
        } else {
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("totalArticle", 0);
            resultMap.put("totalOrigin", 0);
            resultMap.put("err",1);
            resultMap.put("info","文件内容错误！各列抬头依次为：标题、链接地址、正文内容、日期、文章来源、语言，请再次核实。");
            return resultBundleBuilder.bundle("Add Article",()->
                    resultMap
            );
        }
    }
}
