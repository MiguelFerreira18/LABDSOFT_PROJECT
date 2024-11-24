import { createRouter, createWebHistory } from "@ionic/vue-router";
import { RouteRecordRaw } from "vue-router";
import TabsPage from "../views/TabsPage.vue";
import LoginView from "@/views/LoginView.vue";
import SignUpPage from "@/views/SignUpPage.vue";
import EventDetailsView from "@/views/Events/EventDetailsView.vue";
import EventHistoryView from "@/views/Events/EventHistoryView.vue";
import EventsView from "@/views/Events/EventsView.vue";
import RewardsDashboard from "@/views/RewardsDashboard.vue";
import DashboardEventsView from "@/views/Events/DashboardEventsView.vue";
import AddEventView from "@/views/Events/AddEventView.vue";
import MapView from "@/views/maps/MapView.vue";

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
    component: EventHistoryView,
  },
  {
    path: "/events/dashboard",
    name: "DashboardEventsView",
    component: DashboardEventsView,
  },
  {
    path: "/event/EventDetail/:id",
    name: "EventDetails",
    component: EventDetailsView,
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
    path: "/map",
    name: "map",
    component: MapView,
  },
  {
    path: "/tabs/",
    component: TabsPage,
    meta: { requiresAuth: true, roles: ["Admin", "User", "Institution"] },
    children: [
      {
        path: "",
        redirect: "/tabs/tabHome",
      },
      {
        path: "tabHome",
        component: () => import("@/views/TabHomePage.vue"),
      },
      {
        path: "tab2",
        component: () => import("@/views/Tab2Page.vue"),
      },
      {
        path: "tab3",
        component: () => import("@/views/Tab3Page.vue"),
      },
      {
        path: "tabProfile",
        component: () => import("@/views/TabProfilePage.vue"),
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
