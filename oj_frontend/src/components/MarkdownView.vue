<template>
  <Viewer :value="value" :plugins="plugins" />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { defineProps, withDefaults } from "vue";
import { Viewer } from "@bytemd/vue-next";

const plugins = [gfm(), highlight()];

/**
 * 定义组件属性的类型
 */
interface Props {
  value: string;
}

/**
 *给组件定义初始值
 */
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
});
</script>
<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
</style>
