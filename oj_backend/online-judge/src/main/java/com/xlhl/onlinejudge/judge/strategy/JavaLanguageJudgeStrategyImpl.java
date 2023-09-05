package com.xlhl.onlinejudge.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.xlhl.onlinejudge.model.dto.question.JudgeCase;
import com.xlhl.onlinejudge.model.dto.question.JudgeConfig;
import com.xlhl.onlinejudge.judge.codesandbox.model.JudgeInfo;
import com.xlhl.onlinejudge.model.entity.Question;
import com.xlhl.onlinejudge.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Objects;

/**
 * JavaLanguageJudgeStrategyImpl
 * Java的判题策略
 *
 * @author xlhl
 * @version 1.0
 * @description
 */
public class JavaLanguageJudgeStrategyImpl implements JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();

        JudgeInfo responseInfo = new JudgeInfo();
        //  ● 先判断沙箱执行的结果输出数量与输入用例的数量是否相等
        JudgeInfoMessageEnum waiting = JudgeInfoMessageEnum.ACCEPTED;
        if (outputList.size() != inputList.size()) {
            waiting = JudgeInfoMessageEnum.WRONG_ANSWER;

            responseInfo.setTime(time);
            responseInfo.setMemory(memory);
            responseInfo.setMessage(waiting.getDescription());

            return responseInfo;
        }

        //  ● 判断每一项实际输出与输出用例是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase aCase = judgeCaseList.get(i);
            if (!Objects.equals(aCase.getOutput(), outputList.get(i))) {
                waiting = JudgeInfoMessageEnum.WRONG_ANSWER;
                responseInfo.setTime(time);
                responseInfo.setMemory(memory);
                responseInfo.setMessage(waiting.getDescription());

                return responseInfo;
            }
        }
        //  ● 判断每一项输出限制是否符合规则


        String judgeConfig = question.getJudgeConfig();
        JudgeConfig config = JSONUtil.toBean(judgeConfig, JudgeConfig.class);


        Long needTimeLimit = config.getTimeLimit();
        Long needMemoryLimit = config.getMemoryLimit();

        if (needMemoryLimit < memory) {
            waiting = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;

            responseInfo.setTime(time);
            responseInfo.setMemory(memory);
            responseInfo.setMessage(waiting.getDescription());
            return responseInfo;
        }

        //  假设Java本身需要额外执行 10s，实际耗时需要减去 10s
        Long javaTimeLimit = 1000 * 10L;
        if (needTimeLimit > (time + javaTimeLimit)) {
            waiting = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            responseInfo.setTime(time);
            responseInfo.setMemory(memory);
            responseInfo.setMessage(waiting.getDescription());
            return responseInfo;
        }

        //  ● 可能还有其他的异常情况

        responseInfo.setTime(time);
        responseInfo.setMemory(memory);
        responseInfo.setMessage(waiting.getDescription());

        return responseInfo;
    }
}
