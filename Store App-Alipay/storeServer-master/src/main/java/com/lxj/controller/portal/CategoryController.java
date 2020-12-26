package com.lxj.controller.portal;

import com.lxj.common.ServerResponse;
import com.lxj.pojo.Category;
import com.lxj.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by LXJ
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<List<Category>> getList(){
        return iCategoryService.getCategoryList();

    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(
            @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){

        return iCategoryService.getChildrenParallelCategory(categoryId);

    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(
            @RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){

        return iCategoryService.selectCategoryAndChildrenById(categoryId);

    }











}
