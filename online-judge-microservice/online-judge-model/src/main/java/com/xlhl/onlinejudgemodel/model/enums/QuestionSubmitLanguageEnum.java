package com.xlhl.onlinejudgemodel.model.enums;

import java.util.Objects;

/**
 * 编程语言枚举
 *
 * @author xlhl
 */

public enum QuestionSubmitLanguageEnum {
    /**
     * 支持的编程语言
     */
    JAVA("java"),
    PYTHON("python"),
    C("c"),
    PHP("php"),
    CPP("cpp"),
    GOLANG("go"),
    JAVASCRIPT("js");

    private final String language;

    public String getLanguage() {
        return language;
    }

    QuestionSubmitLanguageEnum(String language) {
        this.language = language;
    }

    public static QuestionSubmitLanguageEnum getLanguageEnum(String language) {
        QuestionSubmitLanguageEnum[] languageEnums = QuestionSubmitLanguageEnum.values();
        for (QuestionSubmitLanguageEnum languageEnum : languageEnums) {
            if (Objects.equals(languageEnum.getLanguage(), language)) {
                return languageEnum;
            }
        }
        return null;
    }
}
