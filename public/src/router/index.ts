import { createRouter, createWebHistory } from "@ionic/vue-router";
import { RouteRecordRaw } from "vue-router";
import TabsPage from "../views/TabsPage.vue";
import LoginView from "@/views/LoginView.vue";
import SignUpPage from "@/views/SignUpPage.vue";
import RefactorAfterHavingEventsList from "@/views/RefactorAfterHavingEventsList.vue";
import AttendedEventsView from "@/views/Events/AttendedEventsView.vue";
import EventsView from "@/views/Events/EventsView.vue";
import RewardsDashboard from "@/views/RewardsDashboard.vue";
import DashboardEventsView from "@/views/Events/DashboardEventsView.vue";
import AddEventView from "@/views/Events/AddEventView.vue";

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
    path: "/events/create",
    name: "AddEventView",
    component: AddEventView,
  },
  {
    path: "/events/attended",
    name: "AttendedEvents",
    component: AttendedEventsView,
  },
  {
    path: "/events/dashboard",
    name: "DashboardEventsView",
    component: DashboardEventsView,
  },
  {
    path: "/event/EventDetail/:id",
    name: "EventDetails",
    component: RefactorAfterHavingEventsList,
  },
  {
    path: "/events",
    name: "Events",
    component: EventsView,
  },
  {
    path: "/rewards",
    name: "Rewards",
    component: RewardsDashboard,
  },
  {
    path: "/tabs/",
    component: TabsPage,
    meta: { requiresAuth: true, roles: ["Admin", "User", "Institution"] },
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
  const isAuthenticated = Boolean(token);
  const role = localStorage.getItem("role");
  if (to.meta.requiresAuth && !isAuthenticated) {
    next("/login");
    //@ts-expect-error includes might not exist on null
  } else if (to.meta.roles && !to.meta.roles.includes(role)) {
    next("/login");
  } else {
    next();
  }
});

export default router;
