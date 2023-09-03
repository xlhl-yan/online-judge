<template>
  <div id="code-editor" ref="codeEditorRef" style="height: 400px" />
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { defineProps, onMounted, ref, toRaw, withDefaults } from "vue";

const codeEditorRef = ref();
const codeEditor = ref();
const value = ref("HelloWorld");

/**
 * 定义组件属性的类型
 */
interface Props {
  value: string;
  handleChange: (value: string) => void;
}

/**
 *给组件定义初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  handleChange: (value: string) => {
    console.log(value);
  },
});

const fillValue = () => {
  if (!codeEditor.value) {
    return;
  }
  //  改变值
  toRaw(codeEditor.value).setValue("新的值");
};

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  codeEditorRef.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: "Java",
    automaticLayout: true,
    minimap: {
      enabled: true,
    },
    theme: "vs-dark",
    colorDecorators: true,
  });
  codeEditorRef.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditorRef.value).getValue());
  });
});
</script>

<style scoped></style>
