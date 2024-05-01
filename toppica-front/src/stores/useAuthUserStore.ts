// Pinia store
import { defineStore } from 'pinia'

// Router
import router from '@src/router'

// Api
import axios from '@src/apis/axios'

import { AxiosError } from 'axios'

import type { Ref } from "vue";
import { ref } from "vue";

// Interface
import { IAuthUserInterface, IEmployees, IRegisterForm, IUserInterface, TokenResponse } from '@src/interface/auth'

const initalUserState: Partial<IUserInterface> = {
  username: '',
  email: '',
  isUserLoggedIn: false,
  access_token: ''
}

const message: Ref<string> = ref(
  ""
);

export const useAuthUserStore = defineStore('authUser', {
  state: () => {
    return {
      message: message,
      user: initalUserState,
      loadingSession: false
    }
  },
  getters: {
    getAccessToken: (state): string | undefined => state.user.access_token,
    getActiveUser: (state) => {
      return state.user;
    },
    getMessage: (state) => {
      return state.message;
    }
  },
  actions: {
    async login(userDetails: Partial<IAuthUserInterface>): Promise<void> {
      const { email: email, password: password } = userDetails

      try {
        const { data } = await axios.post('/login', JSON.stringify({ email, password }))

        if(data && data.error_description){
          this.message = data.error_description;
        }

        if(data.access_token && data.refresh_token && data.email){
          localStorage.setItem("access_token", data.access_token);
          localStorage.setItem("refresh_token", data.refresh_token);
          localStorage.setItem("email", data.email);
          this.user = {
            ...this.user,
            access_token: data.access_token,
            email: data.email,
            isUserLoggedIn: true
          }
          axios.interceptors.request.use(
            (config) => {
              if (data.access_token) {
                config.headers.Authorization = `Bearer ${data.access_token}`
              }
              return config
            },
            async (error: AxiosError) => {
              return Promise.reject(error)
            }
          )
          router.push('/');
        }
      } catch (error: unknown) {
        console.log('error')
      } finally {
        this.loadingSession = false
      }
    },
    async register(userDetails: Partial<IRegisterForm>): Promise<void> {
      try {
        const { firstName: firstName,
          lastName: lastName,
          email: email,
          password: password,
          passwordConfirm: passwordConfirm } = userDetails
        await axios.post('/register', JSON.stringify({ firstName, lastName, email, password, passwordConfirm }))
        // once created we redirect the user back to the login page to login
        router.push('/access/sign-in/')
      } catch (error: unknown) {
        console.log('error')
      }
    },
    async logout(): Promise<void> {
      this.user = initalUserState

      // logout of any other tabs that are open
      // await axios.get('/logout')
      localStorage.removeItem("access_token");
      localStorage.removeItem("refresh_token");
      localStorage.removeItem("email");
      window.location.reload();
      router.push('/access/sign-in/');
    },
    async getEmployees() {
      try {
        const { data } = await axios.get<IEmployees>('/employees')

        console.log(data)
      } catch (error: unknown) {
        console.log('error')
      }
    },
    updateAccessToken(accessToken: string) {
      this.user = {
        ...this.user,
        access_token: accessToken,
        isUserLoggedIn: true
      }
    },
    async refreshToken(token: Partial<TokenResponse>): Promise<string | undefined> {
      const { refresh_token: refresh_token } = token
      const email = localStorage.getItem("email");
      // check if the user still has a valid token
      try {
        const { data } = await axios.post('/refresh-token', JSON.stringify({ refresh_token, email }))
        if(data && data.error_description){
          this.logout();
        }
        if(data.accessToken && data.email && data.access_token && data.refresh_token){
          this.updateAccessToken(data.accessToken);
          localStorage.setItem("email", data.email);
          localStorage.setItem("access_token", data.access_token);
          localStorage.setItem("refresh_token", data.refresh_token);
        }

        return data.access_token;
      } catch (error) {
        console.log('error')
      }
    },
    UpdateMessage(message: string) {
      this.message = message;
    }
  }
})
