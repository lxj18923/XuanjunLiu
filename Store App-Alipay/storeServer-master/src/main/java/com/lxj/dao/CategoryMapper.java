package com.lxj.dao;

import com.lxj.pojo.Category;
import com.lxj.util.MyMapper;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {

    List<Category> selectCategoryChildrenByParentId(Integer parentId);


}