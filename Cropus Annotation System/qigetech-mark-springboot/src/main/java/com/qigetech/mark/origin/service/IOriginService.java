package com.qigetech.mark.origin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.vo.OriginLabelVO;
import com.qigetech.mark.user.entity.user.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
public interface IOriginService extends IService<Origin> {
    Origin getOrigin(Long userId,String language);
    boolean skip(Long userId,String language);
    //Origin getOriginByRandom(Long userId,String language);
    Page<OriginLabelVO> getOriginLabelPage(long current, long size, User user);
    Origin getOriginByCheck(Long userId,String language);
    Origin getOriginByNoCheck(String language);
//    Origin isNeedNewOrigin(Long userId,List<Origin> list);
    boolean deleteOrigin(Long userId,String language);
    Page<OriginLabelVO> getOriginSearchPage(long current, long size, Integer status, String source,String sentence);
}
