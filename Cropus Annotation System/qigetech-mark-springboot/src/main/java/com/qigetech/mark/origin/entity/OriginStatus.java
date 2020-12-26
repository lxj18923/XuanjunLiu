package com.qigetech.mark.origin.entity;

public enum  OriginStatus {

    /**
     * @Author liuxuanjun
     * @Description 未标注
     * @Date 15:15 2019-06-09
     * @Param
     * @return
     **/
    UNLABELED(0),

    //标注一次
    MARK_ONCE(1),

    //交叉验证失败
    MARK_TWICE(2),

    //交叉验证失败后标注第3次
    MARK_THIRD(3),

    //交叉验证成功
    PROOFREAD_SUCCESS(5),
    //专家标注后的句子
    MARK_fourth(4),

    //异常数据
    WRONG_DELETE(9);




    private int data;

    private OriginStatus(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

}
