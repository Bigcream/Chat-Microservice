import { useAuthUserStore } from '@src/stores/useAuthUserStore'
import { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useCookies } from "vue3-cookies";
import {reactive, computed } from "vue";
import { TokenResponse } from '@src/interface/auth'

export const requireAuth = async (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
): Promise<void> => {
  const { refreshToken } = useAuthUserStore()
  const signInPath = '/access/sign-in/'
  const signUpPath = '/access/sign-up/'
  const { cookies } = useCookies();
  const access_token = computed(() => localStorage.getItem('access_token'))
  const refresh_token = computed(() => localStorage.getItem('refresh_token'));

  const payload = reactive<TokenResponse>({
    refresh_token: refresh_token.value,
  });
  // console.log(access_token)
  // if(access_token){
  //   next('/')
  // } else

  if (to.path === signInPath ||to.path === signUpPath || access_token.value) {
    return next();
  } else if (refresh_token.value && !access_token.value) {
    refreshToken(payload);
    return next();
  }
  else {
    return next('/access/sign-in/')
  }
}
