package com.lxj.service;

import com.github.pagehelper.PageInfo;
import com.lxj.common.ServerResponse;
import com.lxj.pojo.Product;
import com.lxj.vo.ProductDetailVo;

/**
 * Created by LXJ
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);



}
