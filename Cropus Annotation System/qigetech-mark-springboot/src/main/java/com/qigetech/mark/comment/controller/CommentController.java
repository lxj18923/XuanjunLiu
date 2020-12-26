package com.qigetech.mark.comment.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qigetech.mark.comment.entity.Comment;
import com.qigetech.mark.comment.entity.dto.CommentDTO;
import com.qigetech.mark.comment.entity.vo.CommentsVO;
import com.qigetech.mark.comment.service.ICommentService;
import com.qigetech.mark.user.auth.utils.AuthUtils;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-06
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Autowired
    private ICommentService commentServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @PostMapping //提供用户提意见功能，将comment保存到数据库，？？？
    public ResultBundle<Boolean> addComment(@RequestBody CommentDTO commentDTO, Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        Long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username)).getId();
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setUserId(userId.intValue());
        return resultBundleBuilder.bundle("",()->commentServiceImpl.save(comment));
    }

    @GetMapping("/list")
    public ResultBundle<IPage<CommentsVO>> getPage(@RequestParam(name = "current",defaultValue = "0")long current,
                                                   @RequestParam(name = "size",defaultValue = "20")long size){
        return resultBundleBuilder.bundle("",()->
                commentServiceImpl.getPage(current,size));
    }

}
