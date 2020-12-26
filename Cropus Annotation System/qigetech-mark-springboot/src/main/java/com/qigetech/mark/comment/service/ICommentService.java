package com.qigetech.mark.comment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qigetech.mark.comment.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.comment.entity.vo.CommentsVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-06
 */
public interface ICommentService extends IService<Comment> {
    IPage<CommentsVO> getPage(long current, long size);
}
