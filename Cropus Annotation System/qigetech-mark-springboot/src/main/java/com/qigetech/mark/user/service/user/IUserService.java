package com.qigetech.mark.user.service.user;


import com.qigetech.mark.user.entity.user.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-14
 */
public interface IUserService extends IService<User> {
    List<Map<String, Object>> seletedRolesByUserId(long userId);
}
