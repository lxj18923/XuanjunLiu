package com.qigetech.mark.result.label.service.count;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.result.label.entity.count.CountStu;
import com.qigetech.mark.user.entity.user.User;


public interface ICountStuService extends IService<CountStu> {
    void countBySort();
    CountStu countByUserId(User user);
    void  dailyCount();
}
