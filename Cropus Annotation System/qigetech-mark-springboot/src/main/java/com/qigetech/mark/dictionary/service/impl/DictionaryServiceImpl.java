package com.qigetech.mark.dictionary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.dictionary.entity.Dictionary;
import com.qigetech.mark.dictionary.mapper.DictionaryMapper;
import com.qigetech.mark.dictionary.service.IDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-03-22
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    @Override
    public List<Dictionary> getPartOfSpeech(String word) {
        List<Dictionary> dictionaries = this.baseMapper.selectList(new QueryWrapper<Dictionary>().eq("word", word));
        if(dictionaries != null){
            return  dictionaries;
        }else {
            return null;
        }
    }
}
