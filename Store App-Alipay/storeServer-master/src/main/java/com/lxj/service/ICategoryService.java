package com.lxj.service;


import com.lxj.common.ServerResponse;
import com.lxj.pojo.Category;

import java.util.List;

/**
 * Created by LXJ
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

    ServerResponse<List<Category>> getCategoryList();


}
