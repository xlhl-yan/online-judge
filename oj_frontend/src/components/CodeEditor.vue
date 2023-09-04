<template>
  <div
    id="code-editor"
    ref="codeEditorRef"
    style="min-height: 600px; height: 100vh"
  />
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { defineProps, onMounted, toRaw, ref, withDefaults } from "vue";

const codeEditorRef = ref();

/**
 * 定义组件属性的类型
 */
interface Props {
  value: string;
  handleChange: (value: string) => void;
  language: string;
}

/**
 *给组件定义初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  handleChange: (value: string) => {
    console.log(value);
  },
  language: () => "java",
});

onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  codeEditorRef.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: props.language,
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

// watchEffect([props.language], () => {
//   codeEditorRef.value = monaco.editor.create(codeEditorRef.value, {
//     value: props.value,
//     language: props.language,
//     automaticLayout: true,
//     minimap: {
//       enabled: true,
//     },
//     theme: "vs-dark",
//     colorDecorators: true,
//   });
//   codeEditorRef.value.onDidChangeModelContent(() => {
//     props.handleChange(toRaw(codeEditorRef.value).getValue());
//   });
// });
</script>

<style scoped></style>
