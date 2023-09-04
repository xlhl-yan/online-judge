import { RouteRecordRaw } from "vue-router";
import NoAuth from "@/views/NoAuth.vue";
import AccessEnum from "@/access/accessEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import LoginView from "@/views/user/LoginView.vue";
import RegisterView from "@/views/user/RegisterView.vue";
import AddQuestion from "@/views/question/AddQuestionView.vue";
import ManageQuestion from "@/views/question/ManageQuestionView.vue";
import QuestionsView from "@/views/question/QuestionsView.vue";
import ViewQuestionView from "@/views/question/ViewQuestionView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    meta: {
      hideInMenu: true,
    },
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: LoginView,
      },
      {
        path: "/user/register",
        name: "注册",
        component: RegisterView,
      },
    ],
  },
  {
    path: "/",
    name: "主页",
    component: QuestionsView,
  },
  {
    path: "/questions",
    name: "搜索题目",
    component: QuestionsView,
  },
  {
    path: "/add/question",
    name: "上传题目",
    component: AddQuestion,
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    path: "/update/question",
    name: "更新题目",
    component: AddQuestion,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/view/question/:id",
    name: "做题",
    component: ViewQuestionView,
    props: true,
    meta: {
      hideInMenu: true,
      access: AccessEnum.USER,
    },
  },
  {
    path: "/manage/question",
    name: "管理页面",
    component: ManageQuestion,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  // {
  //   path: "/header",
  //   name: "影藏页面",
  //   component: HomeView,
  //   meta: {
  //     hideInMenu: true,
  //   },
  // },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuth,
    meta: {
      hideInMenu: true,
    },
  },
  // {
  //   path: "/Admin",
  //   name: "管理员",
  //   component: AdminView,
  //   meta: {
  //     access: AccessEnum.ADMIN,
  //   },
  // },
  // {
  //   path: "/about",
  //   name: "关于我的",
  //   component: () => import("../views/AboutView.vue"),
  // },
];
