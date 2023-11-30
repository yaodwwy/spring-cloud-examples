import type { Router, LocationQueryRaw } from 'vue-router';
import NProgress from 'nprogress'; // progress bar

import { useUserStore } from '@/store';
import { isLogin } from '@/utils/auth';

export default function setupUserLoginInfoGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const userStore = useUserStore();
    if (isLogin()) {
      if (userStore.role) {
        next();
      } else {
        try {
          await userStore.info();
          next();
        } catch (error) {
          await userStore.logout();
          next({
            name: 'login',
            query: {
              redirect: to.name,
              ...to.query,
            } as LocationQueryRaw,
          });
        }
      }
    } else {
      // if (to.name === 'login') {
      //   next();
      //   return;
      // }
      // next({
      //   name: 'login',
      //   query: {
      //     redirect: to.name,
      //     ...to.query,
      //   } as LocationQueryRaw,
      // });

      // 未登录状态 跳转到登录页
      // window.location.href = 'http://localhost:9090/oauth2/authorize?client_id=pm-client&redirect_uri=http://localhost:5173/redirect-uri&response_type=code&scope=read_user+openid&state=IAM';

      next();
    }
  });
}
