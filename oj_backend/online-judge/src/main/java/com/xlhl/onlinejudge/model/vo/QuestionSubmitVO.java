package com.xlhl.onlinejudge.model.vo;

import cn.hutool.json.JSONUtil;
import com.xlhl.onlinejudge.model.dto.questionsubmit.JudgeInfo;
import com.xlhl.onlinejudge.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交信息视图
 *
 * @author xlhl
 * @TableName question
 */
@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 代码
     */
    private String code;


    /**
     * 编程语言
     */
    private String language;


    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（1- 待判题、2- 判题中、3- 成功、4- 失败）
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 题目信息
     */
    private QuestionVO questionVO;

    /**
     * 提交者信息
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        if (judgeInfo != null) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }

        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionVO);
        String judgeInfo = questionSubmit.getJudgeInfo();

        questionVO.setJudgeInfo(JSONUtil.toBean(judgeInfo, JudgeInfo.class));

        return questionVO;
    }


    private static final long serialVersionUID = 1L;
}