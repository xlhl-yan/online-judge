<template>
  <div class="registerView">
    <h1 style="margin-bottom: 20px">注册</h1>
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
      <a-form-item field="checkPassword" label="密码" tooltip="密码不少于8位">
        <a-input-password v-model="form.checkPassword" placeholder="确认密码" />
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit" style="width: 120px" type="primary"
          >注册
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const router = useRouter();
/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword: "",
}) as UserRegisterRequest;

/**
 * 提交表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  if (res.code === 0) {
    message.success("注册成功");
    await router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注册失败," + res.message);
  }
};
</script>
