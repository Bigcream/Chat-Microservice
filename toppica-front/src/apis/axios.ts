import axios from 'axios'
import { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
import { useAuthUserStore } from '@src/stores/useAuthUserStore'
import { useCookies } from "vue3-cookies";
import {reactive, computed } from "vue";
import { TokenResponse } from '@src/interface/auth'
const BASE_URL = import.meta.env.VITE_API_URL
interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  _retry?: boolean
}

const access_token = computed(() => localStorage.getItem('access_token'))
const refresh_token = computed(() => localStorage.getItem('refresh_token'));
const payload = reactive<TokenResponse>({
  refresh_token: refresh_token.value,
});

// let isRefreshingToken = false
const axiosApi =  axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  } // do not remove this, its added to add params later in the config
  // withCredentials: true
})

// axiosApi.interceptors.request.use(
//   (config) => {
//     if (access_token.value) {
//       config.headers.Authorization = `Bearer ${access_token.value}`
//     }
//     return config
//   },
//   async (error: AxiosError) => {
//     return Promise.reject(error)
//   }
// )

// axios.interceptors.response.use(
//   (response: AxiosResponse) => {
//     return response
//   },
//   async (error: AxiosError) => {
//     const { logout, refreshToken } = useAuthUserStore()
//     const originalRequest = error.config as CustomAxiosRequestConfig
//     console.log(error)
//     if (error.response?.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true
//       try {
//         if (refresh_token.value) {
//           const data = await refreshToken(payload)
//           if (data) {
//             // update access token in original response
//             if (originalRequest.headers !== undefined) {
//               originalRequest.headers.Authorization = `Bearer ${data}`
              
//             }
//           }
//         }

//         // retry the original request
//         return axios(originalRequest)
//       } catch (error: unknown) {
//         // logout the user and redirect to login page
//         logout()
//         return Promise.reject(error)
//       }
//     }

//     return Promise.reject(error)
//   }
// )

// axiosApi.interceptors.response.use(
//   (response: AxiosResponse) => {
//     return response
//   },
//   async (error: AxiosError) => {
//     const { logout, refreshToken } = useAuthUserStore()
//     const originalRequest = error.config as CustomAxiosRequestConfig

//     if (error.response?.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true
//       console.log("refreshToken")
//       try {
//         await refreshToken(payload)
//         if (access_token) {
//           // update access token in original response
//           if (originalRequest.headers !== undefined) {
//             originalRequest.headers.Authorization = `Bearer ${access_token}`
//           }
//         }
//         // retry the original request
//         return axios(originalRequest)
//       } catch (error: unknown) {
//         // logout the user and redirect to login page
//         localStorage.removeItem("access_token");
//         localStorage.removeItem("email");
//         // logout()
//         return Promise.reject(error)
//       }
//     }

//     // logout the user and redirect to login page
//     // logout()
//     localStorage.removeItem("access_token");
//     localStorage.removeItem("email");
//     return Promise.reject(error)
//   }
// )

export default axiosApi;
