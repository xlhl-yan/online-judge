<template>
  <Editor
    :mode="mode"
    :value="value"
    :plugins="plugins"
    @change="handleChange"
  />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { ref, withDefaults, defineProps } from "vue";

const plugins = [gfm(), highlight()];

/**
 * 定义组件属性的类型
 */
interface Props {
  value: string;
  mode?: (value: string) => void;
  handleChange: (value: string) => void;
}

/**
 *给组件定义初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  mode: () => "split",
  handleChange: (value: string) => {
    console.log(value);
  },
});
</script>
<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
</style>
