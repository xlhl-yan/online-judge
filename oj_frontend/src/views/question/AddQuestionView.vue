<template>
  <div id="addQuestionView">
    <a-form :model="form" label-align="auto">
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>

      <a-form-item field="tags" label="标签">
        <a-input-tag v-model="form.tags" placeholder="请输入标签" allow-clear />
      </a-form-item>

      <a-form-item field="content" label="题目内容">
        <markdown-edit
          :value="form.content"
          :handleChange="onContentChange"
          :mode="`split`"
        />
      </a-form-item>

      <a-form-item field="answer" label="答案">
        <markdown-edit
          :value="form.answer"
          :handleChange="onAnswerChange"
          :mode="`split`"
        />
      </a-form-item>

      <a-form-item label="判题规则" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 480px">
          <a-form-item
            field="judgeConfig.timeLimit"
            validate-trigger="input"
            label="时间限制"
          >
            <a-input-number
              :default-value="form.judgeConfig.timeLimit"
              mode="button"
              min="0"
              class="input-demo"
              v-model="form.judgeConfig.timeLimit"
            />
          </a-form-item>
          <a-form-item
            field="judgeConfig.stackLimit"
            validate-trigger="input"
            label="堆栈限制"
          >
            <a-input-number
              :default-value="form.judgeConfig.stackLimit"
              mode="button"
              min="0"
              class="input-demo"
              v-model="form.judgeConfig.stackLimit"
            />
          </a-form-item>
          <a-form-item
            field="judgeConfig.memoryLimit"
            validate-trigger="input"
            label="内存限制"
          >
            <a-input-number
              :default-value="form.judgeConfig.memoryLimit"
              mode="button"
              min="0"
              class="input-demo"
              v-model="form.judgeConfig.memoryLimit"
            />
          </a-form-item>
        </a-space>
      </a-form-item>

      <a-form-item label="测试用例" :content-flex="false" :merge-props="false">
        <a-form-item
          v-for="(judgeCaseItem, index) of form.judgeCase"
          :key="index"
          no-style
        >
          <a-space direction="vertical" style="min-width: 480px">
            <a-form-item
              :field="`form.judgeCase[${index}].input`"
              :label="`用例-${index}`"
              :key="index"
            >
              <a-input
                v-model="judgeCaseItem.input"
                placeholder="请输入测试用例"
              />
            </a-form-item>
            <a-form-item
              :field="`form.judgeCase[${index}].input`"
              :label="`用例-${index}`"
              :key="index"
            >
              <a-input
                v-model="judgeCaseItem.output"
                placeholder="请输入输出用例"
              />
            </a-form-item>
            <a-button
              style="margin-bottom: 20px"
              status="danger"
              @click="handleDelete(index)"
              >删除
            </a-button>
          </a-space>
        </a-form-item>

        <div>
          <a-button @click="handleAdd" status="success" type="outline"
            >新增测试用例
          </a-button>
        </div>
      </a-form-item>

      <a-form-item style="margin-bottom: 12px">
        <a-button
          @click="doSubmit"
          html-type="submit"
          type="primary"
          style="min-width: 200px"
          >提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import MarkdownEdit from "@/components/MarkdownEdit.vue";
import {
  QuestionAddRequest,
  QuestionControllerService,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute } from "vue-router";

const route = useRoute();
let form = ref<QuestionAddRequest>({
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  tags: [],
  title: "",
});

/**
 * 如果路径中包含 update 则表示为更新页面
 */
const updatePage = route.path.includes("update");
/**
 * 根据 id 获取旧数据
 */
const loadData = async () => {
  const id = route.query?.id;

  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    form.value.tags = JSON.parse(form.value.tags as any);
    if (!form.value.judgeCase) {
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
    }
    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
  } else {
    message.error("无法获取数据" + res.message);
  }
};
onMounted(() => {
  loadData();
});

/**
 * 新增测试用例
 */
const handleAdd = () => {
  form.value.judgeCase?.push({
    output: "",
    input: "",
  });
};

/**
 *删除测试用例
 */
const handleDelete = (index: number) => {
  form.value.judgeCase?.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.value.content = value;
};

const onAnswerChange = (value: string) => {
  form.value.answer = value;
};

const doSubmit = async () => {
  console.log(form.value);
  //  区分更新还是创建
  if (updatePage) {
    const res = await QuestionControllerService.updateQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("更新成功");
    } else {
      message.error("更新失败，" + res.message);
    }
  } else {
    const res = await QuestionControllerService.addQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("添加成功");
    } else {
      message.error("添加失败，" + res.message);
    }
  }
};
</script>

<style scoped>
#addQuestion {
}
</style>
