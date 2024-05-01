// import axios from './axios'

// import { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
// import { useAuthUserStore } from '@src/stores/useAuthUserStore'
// import { useCookies } from "vue3-cookies";
// import {reactive } from "vue";
// import { TokenResponse } from '@src/interface/auth'
// interface CustomAxiosRequestConfig extends AxiosRequestConfig {
//   _retry?: boolean
// }

// const { cookies } = useCookies();
// const access_token = "test"
// const refresh_token = cookies.get('refresh_token');
// const payload = reactive<TokenResponse>({
//   refresh_token: refresh_token,
// });
// let isRefreshingToken = false

// const interceptors = (): void => {
//   axios.interceptors.request.use(
//     (config) => {
//       if (access_token) {
//         config.headers.Authorization = `Bearer ${access_token}`
//       }
//       return config
//     },
//     async (error: AxiosError) => {
//       return Promise.reject(error)
//     }
//   )

//   axios.interceptors.response.use(
//     (response: AxiosResponse) => {
//       return response
//     },
//     async (error: AxiosError) => {
//       const { logout, refreshToken } = useAuthUserStore()
//       const originalRequest = error.config as CustomAxiosRequestConfig

//       if (error.response?.status === 401 && !originalRequest._retry) {
//         originalRequest._retry = true
//         isRefreshingToken = true

//         try {
//           await refreshToken(payload)
//           if (access_token) {
//             // update access token in original response
//             if (originalRequest.headers !== undefined) {
//               originalRequest.headers.Authorization = `Bearer ${access_token}`
//             }
//           }
//           // retry the original request
//           return axios(originalRequest)
//         } catch (error: unknown) {
//           // logout the user and redirect to login page
//           logout()
//           return Promise.reject(error)
//         } finally {
//           isRefreshingToken = false
//         }
//       }

//       // logout the user and redirect to login page
//       logout()
//       return Promise.reject(error)
//     }
//   )
// }
// export { interceptors }
