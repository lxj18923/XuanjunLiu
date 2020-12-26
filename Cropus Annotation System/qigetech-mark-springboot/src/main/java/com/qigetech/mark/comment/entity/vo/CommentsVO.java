package com.qigetech.mark.comment.entity.vo;

import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import lombok.Data;

import java.util.List;

/**
 * Created by liuxuanjun on 2019-07-07
 * Project : qigetech-mark
 */
@Data
public class CommentsVO {

    /**
     * id : 5
     * content : dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd的的的的的
     * username : d d d
     * postDate : 2019-7-6 17:43
     * originId : 2
     * comments : [{"id":"6","content":"测试1ddddddddddddddd顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶","username":"用户1","postDate":"2019-7-6 17:43"},{"id":"7","content":"测试1ddddddddddddddd顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶","username":"用户1","postDate":"2019-7-6 17:43"}]
     * labelResult : [{"id":4,"markContent":"这是/n 一个/n 测试/a 1/n ","originId":4,"userId":2641,"username":"renhonglin","markDate":"Jun 11, 2019 11:39:34 AM"}]
     */

    private Integer id;
    private String content;
    private String username;
    private String postDate;
    private Integer originId;
    private List<CommentVO> comments;
    private List<LabelResultVO> labelResults;

}
