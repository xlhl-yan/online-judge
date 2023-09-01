<template>
  <div id="app">
    <BasicLayouts />
  </div>
</template>

<style>
#app {
}
</style>
<script setup lang="ts">
import BasicLayouts from "@/layouts/BasicLayouts";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();
router.beforeEach((to, from, next) => {
  //  判断当前用户是否有权限
  if (to.meta?.access === "canAdmin") {
    if (store.state.user.loginUser?.role !== "admin") {
      next("/noAuth");
      return;
    }
  }
  next();
});
</script>
