<template>
  <a-row id="globalHeader" class="grid-demo" align="center" :wrap="false">
    <a-col flex="100px">
      <div class="title-bar">
        <img class="logo" src="../assets/logo.jpg" />
        <div class="title">这里是logo</div>
      </div>
    </a-col>
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path"
          >{{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>
        <a-space v-if="store.state.user?.loginUser?.userName">
          {{ store.state.user?.loginUser?.userName }}
        </a-space>
        <a-space v-else>
          <a-button @click="doLogin" type="primary">登录</a-button>
        </a-space>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";

const store = useStore();
const router = useRouter();
const route = useRoute();
/**
 * 展示在菜单的路由
 */
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    return checkAccess(
      store.state.user.loginUser,
      item?.meta?.access as string
    );
  });
});

//  默认主页
const selectedKeys = ref(["/"]);
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

// setTimeout(() => {
//   store.dispatch("user/getLoginUser", {
//     userName: "Helena",
//     userRole: AccessEnum.ADMIN,
//   });
// }, 1000 * 3);

const doLogin = () => {
  router.push({
    path: "/user/login",
    replace: true,
  });
};

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.title-bar {
}

.logo {
  height: 100px;
}

.title {
  color: #444;
  margin-left: 10px;
}
</style>
