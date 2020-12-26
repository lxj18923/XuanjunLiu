package com.qigetech.mark.dictionary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.dictionary.entity.Dictionary;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-03-22
 */
public interface IDictionaryService extends IService<Dictionary> {
    /** author：zzy date：2020.3.21 */
    List<Dictionary> getPartOfSpeech(String word);
}
