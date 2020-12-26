package com.qigetech.mark.dictionary.controller;


import com.qigetech.mark.dictionary.entity.Dictionary;
import com.qigetech.mark.dictionary.service.IDictionaryService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Autowired
    private IDictionaryService dictionaryServiceImpl;

    /**author:zzy date:2020/3/21 */
    @GetMapping("/partofspeech")
    public ResultBundle<List<Dictionary>> getPart(@RequestParam(name = "word") String word){
        return resultBundleBuilder.bundle("get partofspeech",()->
                dictionaryServiceImpl.getPartOfSpeech(word)
        );
    }

}
