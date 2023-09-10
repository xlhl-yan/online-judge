package com.xlhl.onlinejudgemodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 提交状态枚举
 *
 * @author xlhl
 */

public enum QuestionSubmitStatusEnum {
    /**
     * wait 等待判题
     */
    WAITING("等待判题", 1),

    /**
     * running 判题中
     */
    RUNNING("判题中", 2),

    /**
     * success 通过
     */
    SUCCESS("通过", 3),

    /**
     * fail 失败
     */
    FAIL("失败", 4);

    private final String message;

    private final Integer value;

    QuestionSubmitStatusEnum(String message, Integer value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatusEnum submitStatusEnum : QuestionSubmitStatusEnum.values()) {
            if (submitStatusEnum.getValue().equals(value)) {
                return submitStatusEnum;
            }
        }
        return null;
    }
}
