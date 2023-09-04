<template>
  <div id="manageQuestion">
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
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">下线</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const tableRef = ref();
const router = useRouter();
const dataList = ref([]);
const total = ref(0);
const searchParams = ref({
  pageSize: 8,
  current: 1,
});

const onPageChange = (current: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: current,
  };
};

const doDelete = async (question: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
    await loadData();
  } else {
    message.error("删除失败", res.message);
  }
};
const doUpdate = (question: Question) => {
  router.push({
    path: "/update/question",
    query: {
      id: question.id,
    },
  });
};

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
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
/*
* "id": "1698193321865789441",
  "title": "测试标题~~",
  "content": "这里是内容",
  "tags": "[\"Java\",\"链表\"]",
  "answer": "二分查找",
  "submitNum": null,
  "acceptedNum": null,
  "judgeCase": "[{\"input\":\"1 2\",\"output\":\"3 4\"}]",
  "judgeConfig": "[{\"timeLimit\":1000,\"memoryLimit\":1000,\"stackLimit\":1000}]",
  "thumbNum": 0,
  "favourNum": 0,
  "userId": "1697862819820691458",
  "createTime": "2023-09-03T04:36:53.000+00:00",
  "updateTime": "2023-09-03T04:36:53.000+00:00",
  "isDelete": 0
* */
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "判题规则",
    dataIndex: "judgeConfig",
  },
  {
    title: "创建者id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>
