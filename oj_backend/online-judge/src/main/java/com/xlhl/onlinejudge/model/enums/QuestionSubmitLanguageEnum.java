package com.xlhl.onlinejudge.model.enums;

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
    JAVA("Java"),
    PYTHON("Python"),
    C("C"),
    PHP("PHP"),
    CPP("C++"),
    GOLANG("golang"),
    JAVA_SCRIPT("JavaScript");

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
