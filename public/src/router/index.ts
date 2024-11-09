import { createRouter, createWebHistory } from "@ionic/vue-router";
import { RouteRecordRaw } from "vue-router";
import TabsPage from "../views/TabsPage.vue";
import LoginView from "@/views/LoginView.vue";
import SignUpPage from "@/views/SignUpPage.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: { public: true },
  },
  {
    path: "/signup",
    name: "signup",
    component: SignUpPage,
    meta: { public: true },
  },
  {
    path: "/tabs/",
    component: TabsPage,
    children: [
      {
        path: "",
        redirect: "/tabs/tab1",
      },
      {
        path: "tab1",
        component: () => import("@/views/Tab1Page.vue"),
      },
      {
        path: "tab2",
        component: () => import("@/views/Tab2Page.vue"),
      },
      {
        path: "tab3",
        component: () => import("@/views/Tab3Page.vue"),
      },
    ],
  },
  { path: "/:pathMatch(.*)*", redirect: "/login" },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  const isAuthenticated = token;
  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/login");
  } else {
    next();
  }
});

export default router;
