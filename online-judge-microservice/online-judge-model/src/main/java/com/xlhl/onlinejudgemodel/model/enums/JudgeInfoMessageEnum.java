package com.xlhl.onlinejudgemodel.model.enums;

/**
 * 提交信息枚举
 *
 * @author xlhl
 */

public enum JudgeInfoMessageEnum {

    /**
     * ● Accepted：成功
     * ● Wrong Answer：失败
     * ● Compile Error：编译错误
     * ● Memory Limit Exceeded：内存溢出
     * ● Time Limit Exceeded：运行超时
     * ● Presentation Error：展示错误
     * ● Output Limit Exceeded：输出溢出
     * ● Waiting：等待中
     * ● Dangerous Operation：危险操作
     * ● Runtime Error：运行错误（用户程序的问题）
     * ● System Error：系统错误
     */
    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "WrongAnswer"),
    COMPILE_ERROR("编译错误", "CompileError"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "MemoryLimitExceeded"),
    TIME_LIMIT_EXCEEDED("运行超时", "TimeLimitExceeded"),
    PRESENTATION_ERROR("展示错误", "PresentationError"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出", "OutputLimitExceeded"),
    WAITING("等待中", "Waiting"),
    DANGEROUS_OPERATION("危险操作", "DangerousOperation"),
    RUNTIME_ERROR("运行错误", "RuntimeError"),
    SYSTEM_ERROR("系统错误", "SystemError"),
    ;

    private final String description;

    private final String value;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    JudgeInfoMessageEnum(String description, String value) {
        this.description = description;
        this.value = value;
    }
}
