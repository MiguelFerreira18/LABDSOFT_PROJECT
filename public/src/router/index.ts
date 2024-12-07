import { createRouter, createWebHistory } from '@ionic/vue-router';
import { RouteRecordRaw } from 'vue-router';
import LoginView from '@/views/LoginView.vue';
import SignUpPage from '@/views/SignUpPage.vue';
import EventDetailsView from '@/views/Events/EventDetailsView.vue';
import EventHistoryView from '@/views/Events/EventHistoryView.vue';
import EventsView from '@/views/Events/EventsView.vue';
import RewardsDashboard from '@/views/RewardsDashboard.vue';
import DashboardEventsView from '@/views/Events/DashboardEventsView.vue';
import AddEventView from '@/views/Events/AddEventView.vue';
import MapView from '@/views/maps/MapView.vue';
import HomeView from '@/views/TabHomePage.vue';
import ProfileView from '@/views/TabProfilePage.vue';
import FooterComponent from '@/components/common/FooterComponent.vue';
import { IsJWTExpired } from '@/lib/jwt';
import { IsDataTheSame } from '@/lib/signUpUtil';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { public: true },
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignUpPage,
    meta: { public: true },
  },
  {
    path: '/',
    redirect: '/home',
  },
  {
    path: '/',
    component: FooterComponent,
    meta: { requiresAuth: true, roles: ['Admin', 'User', 'Institution'] },
    children: [
      {
        path: '',
        redirect: '/home',
      },
      {
        path: 'home',
        component: HomeView,
      },
      {
        path: 'search',
        component: EventHistoryView,
      },
      {
        path: 'events/create',
        component: AddEventView,
      },
      {
        path: 'profile',
        component: ProfileView,
      },
      {
        path: '/events/attended',
        name: 'AttendedEvents',
        component: EventHistoryView,
      },
      {
        path: '/events/dashboard',
        name: 'DashboardEventsView',
        component: DashboardEventsView,
      },
      {
        path: '/event/EventDetail/:id',
        name: 'EventDetails',
        component: EventDetailsView,
      },
      {
        path: '/events',
        name: 'Events',
        component: EventsView,
      },
      {
        path: '/rewards',
        name: 'Rewards',
        component: RewardsDashboard,
      },
      {
        path: '/map',
        name: 'map',
        component: MapView,
      },
    ],
  },
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});
router.beforeEach(async (to, from, next) => {
  const isDataTheSame = await IsDataTheSame();
  if (!isDataTheSame) {
    localStorage.clear();
  }
  const token = localStorage.getItem('token');
  let isAuthenticated = Boolean(token);
  const role = localStorage.getItem('role');
  const isOutDated = IsJWTExpired(token || '');

  if ((to.meta.requiresAuth && !isAuthenticated) || isOutDated) {
    next('/login');
  } else if (
    to.meta.roles &&
    role &&
    //@ts-expect-error includes might not exist on null
    !to.meta.roles.includes(role)
  ) {
    next('/login');
  } else {
    next();
  }
});

export default router;
