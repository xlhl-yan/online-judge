<template>
  <div id="viewQuestionsView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <div>
                <a-descriptions
                  title="判题条件"
                  :column="{ xs: 1, md: 2, lg: 3 }"
                >
                  <a-descriptions-item label="时间限制">
                    {{ question.judgeConfig.timeLimit ?? 0 }}
                  </a-descriptions-item>
                  <a-descriptions-item label="内存限制">
                    {{ question.judgeConfig.memoryLimit ?? 0 }}
                  </a-descriptions-item>
                  <a-descriptions-item label="堆栈限制">
                    {{ question.judgeConfig.stackLimit ?? 0 }}
                  </a-descriptions-item>
                </a-descriptions>
              </div>
              <markdown-view :value="question.content || ''" />
              <template #extra>
                <a-space wrap>
                  <a-tag
                    v-for="(tag, index) of question.tags"
                    :key="index"
                    color="green"
                    >{{ tag }}
                  </a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论" disabled>
            这里是评论区
          </a-tab-pane>
          <a-tab-pane key="answer" title="题解" disabled>
            这里是题解区
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item
            style="min-width: 400px"
            field="language"
            label="编程语言"
          >
            <a-select
              v-model="form.language"
              style="width: 320px"
              placeholder="请选择语言"
            >
              <a-option value="java">java</a-option>
              <a-option value="c">c</a-option>
              <a-option value="python">python</a-option>
              <a-option value="php">php</a-option>
              <a-option value="cpp">cpp</a-option>
              <a-option value="go">golang</a-option>
              <a-option value="js">javascript</a-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="doSubmit">搜索</a-button>
          </a-form-item>
        </a-form>
        <CodeEditor
          :value="form.code"
          :language="form.language"
          :handleChange="changeCode"
        />
        <a-divider size="0" />
        <a-button @click="doSubmit" type="primary" style="min-width: 200px"
          >提交代码
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { defineProps, onMounted, ref, withDefaults } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MarkdownView from "@/components/MarkdownView.vue";

interface Props {
  id: string;
}

const question = ref<QuestionVO>();
const tableRef = ref();
const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});
const changeCode = (value: string) => {
  form.value.code = value;
};

/**
 * 提交代码
 */
const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }
  const res = await QuestionSubmitControllerService.doSubmitQuestionUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功");
  } else {
    message.error("提交失败" + res.message);
  }
};

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error("加载失败", res.message);
  }
};

onMounted(() => {
  loadData();
  console.log(tableRef);
});
</script>
<style scoped>
#viewQuestionsView {
}
</style>
