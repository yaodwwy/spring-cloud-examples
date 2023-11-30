import { createRouter, createWebHistory } from 'vue-router';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css';

import { appRoutes } from './routes';
import {REDIRECT_MAIN, NOT_FOUND_ROUTE, DEFAULT_LAYOUT} from './routes/base';
import createRouteGuard from './guard';
import {getToken1} from "@/api/user";

NProgress.configure({ showSpinner: false }); // NProgress Configuration

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: 'login',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue'),
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: '/redirect-uri',
      name: 'redirectWrapper1',
      component: () => import('@/views/login/index.vue'),
      meta: {
        requiresAuth: false,
      },
      beforeEnter(to, from, next){
        console.log(to.query.code);
        getToken1({
          code: to.query.code,
          grant_type: 'authorization_code',
          redirect_uri: 'http://localhost:5173/user',
        }).then(r=>{
          localStorage.setItem('token', r.access_token);

          // getUser({
          //   access_token: localStorage.getItem('token')
          // }).then(r=>{
          //   console.log(r);
          // })
          next({
            path: '/user',
          })
        })
      }
    },
    ...appRoutes,
    REDIRECT_MAIN,
    NOT_FOUND_ROUTE,
  ],
  scrollBehavior() {
    return { top: 0 };
  },
});

createRouteGuard(router);

export default router;
