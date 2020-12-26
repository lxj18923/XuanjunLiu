package com.qigetech.mark.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.comment.entity.Comment;
import com.qigetech.mark.comment.entity.vo.CommentVO;
import com.qigetech.mark.comment.entity.vo.CommentsVO;
import com.qigetech.mark.comment.mapper.CommentMapper;
import com.qigetech.mark.comment.service.ICommentService;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.service.user.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-06
 */
@Service
//显示留言页面的分页功能，没有看懂
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @Override
    public IPage<CommentsVO> getPage(long current,long size){
        Page<CommentsVO> commentsVOPage = new Page<>(current,size);
        IPage<Comment> commentIPage  = this.baseMapper.selectPage(new Page<>(current,size),new QueryWrapper<Comment>().eq("parent_id",0).orderByDesc("post_date"));
        List<Comment> comments = commentIPage.getRecords();
        List<CommentsVO> commentsVOS = new ArrayList<>();
        for(Comment comment : comments){
            CommentsVO commentsVO = new CommentsVO();
            //将已有的先复制过来
            BeanUtils.copyProperties(comment,commentsVO);
            commentsVO.setPostDate(comment.getPostDate().toString());
            commentsVO.setUsername(userServiceImpl.getById(comment.getUserId()).getName());
            //赋值评论
            List<CommentVO> commentVOS = this.baseMapper.selectCommentsByParentId(comment.getId());
            commentsVO.setComments(commentVOS);
            //赋值标注结果
            if(comment.getOriginId()!=null){
                //使用了labelresultimpl里的合句方法，慢如狗
                commentsVO.setLabelResults(labelResultServiceImpl.getSearchPageLabelResultByOriginId(comment.getOriginId()));
            }
            commentsVOS.add(commentsVO);
        }
        commentsVOPage.setRecords(commentsVOS);
        commentsVOPage.setTotal(commentIPage.getTotal());
        return commentsVOPage;
    }

}
