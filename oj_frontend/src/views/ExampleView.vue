<template>
  <div id="example-view">
    <MarkdownEdit :value="markdownValue" :handleChange="onMDChange" />
    <CodeEditor :value="codeValue" :handleChange="onCodeChange" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import MarkdownEdit from "@/components/MarkdownEdit.vue";
import CodeEditor from "@/components/CodeEditor.vue";

const markdownValue = ref("");
const codeValue = ref("");
const onMDChange = (v: string) => {
  markdownValue.value = v;
};
const onCodeChange = (v: string) => {
  codeValue.value = v;
};
</script>
