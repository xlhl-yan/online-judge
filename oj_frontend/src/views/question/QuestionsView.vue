<template>
  <div id="questionsView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item style="min-width: 400px" field="title" label="名称">
        <a-input v-model="searchParams.title" placeholder="搜索名称" />
      </a-form-item>
      <a-form-item style="min-width: 400px" field="tags" label="标签">
        <a-input-tag v-model="searchParams.tags" placeholder="搜索标签" />
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
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green"
            >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${
            record.submitNum ? record.acceptedNum / record.submitNum : `0`
          }% (${record.acceptedNum} / ${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestion(record)">做题</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();
const router = useRouter();
const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 2,
  current: 1,
});
const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
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
const toQuestion = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
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
#questionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
