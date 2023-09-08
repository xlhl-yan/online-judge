<template>
  <div id="questionsSubmitView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item style="min-width: 400px" field="language" label="编程语言">
        <a-select
          v-model="searchParams.language"
          style="width: 320px"
          placeholder="请选择语言"
        >
          <a-option>java</a-option>
          <a-option>c</a-option>
          <a-option>python</a-option>
          <a-option>php</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option>javascript</a-option>
        </a-select>
      </a-form-item>
      <a-form-item style="min-width: 400px" field="questionId" label="题号">
        <a-input v-model="searchParams.questionId" placeholder="搜索题号" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: total,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #judgeInfo="{ record }">
        {{ JSON.stringify(record.judgeInfo.message) }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #questionId="{ record }">
        <a-space>
          <a @click="toQuestion(record)">{{ record.questionId }}</a>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitQueryRequest,
  QuestionSubmitVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();
const router = useRouter();
const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  pageSize: 10,
  current: 1,
});
const columns = [
  {
    title: "提交号",
    dataIndex: "id",
  },
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
    dataIndex: "judgeInfo",
  },
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "题目id",
    dataIndex: "questionId",
    slotName: "questionId",
  },
  {
    title: "提交者id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
    dataIndex: "createTime",
  },
];

const onPageChange = (current: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: current,
  };
};

/**
 * 跳转到做题页面
 */
const toQuestion = (questionSubmit: QuestionSubmitVO) => {
  router.push({
    path: `/view/question/${questionSubmit.questionId}`,
  });
};

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
    {
      ...searchParams.value,
      sortField: "createTime",
      sortOrder: "descend",
    }
  );
  if (res.code === 0) {
    console.log(res.data.records);
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败", res.message);
  }
};

/**
 * 监听 loadData函数，变量发送改变时重新执行
 */
watchEffect(() => {
  loadData();
});

onMounted(() => {
  loadData();
  console.log(tableRef);
});

/**
 * 搜索提交
 */
const doSubmit = () => {
  //  重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};
</script>
<style>
#questionsSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
