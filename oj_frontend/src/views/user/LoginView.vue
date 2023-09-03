<template>
  <div class="loginView">
    <h1 style="margin-bottom: 20px">登录</h1>
    <a-form
      :model="form"
      @submit="handleSubmit"
      label-align="left"
      style="max-width: 480px; margin: 0 auto"
      auto-label-width
    >
      <a-form-item
        field="userAccount"
        tooltip="Please enter username"
        label="账号"
      >
        <a-input v-model="form.userAccount" placeholder="请输入用户账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码" tooltip="密码不少于8位">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" style="width: 120px" type="primary"
          >登录
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
}) as UserLoginRequest;

const store = useStore();
/**
 * 提交表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser");
    await router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登录失败," + res.message);
  }
};
</script>
