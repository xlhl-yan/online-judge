package com.xlhl.onlinejudgemodel.model.enums;

/**
 * 代码沙箱类型枚举
 *
 * @author xlhl
 */

public enum CodeSandBoxTypeEnum {
    /**
     * 代码沙箱类型枚举
     */
    EXAMPLE("example", "实例代码沙箱"),
    REMOTE("remote", "远程调用代码沙箱"),
    THIRD_PARTY("third_party", "第三方代码沙箱");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;

    CodeSandBoxTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
