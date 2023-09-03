package com.xlhl.onlinejudge.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交
 * @author xlhl
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目 id
     */
    @TableField(value = "questionId")
    private Long questionId;

    /**
     * 编程语言
     */
    @TableField(value = "language")
    private String language;

    /**
     * 用户代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 判题信息
     */
    @TableField(value = "judgeInfo")
    private String judgeInfo;

    /**
     * 判题状态（1- 待判题、2- 判题中、3- 成功、4- 失败）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建用户 id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}