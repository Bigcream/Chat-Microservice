<script setup lang="ts">
import { ref, onMounted, onUnmounted, provide, reactive, onBeforeUnmount, computed } from "vue";

import useStore from "@src/store/store";
import { fetchData } from "@src/store/defaults";

import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";

import { useSocketStore } from "./socket/useSocketStore";
import { socket } from "@src/socket/socket";
import { useChatStore } from "./stores/useChatStore";
import { useUserStore } from "./stores/useUserStore";
import axios from '@src/apis/axios'
import { AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
import { TokenResponse } from '@src/interface/auth'
import { useAuthUserStore } from '@src/stores/useAuthUserStore'
import { INotificationMessage } from "./interface/notification";
import { useNotificationStore } from "./stores/useNotificationStore";

const access_token = computed(() => localStorage.getItem('access_token'))
const refresh_token = computed(() => localStorage.getItem('refresh_token'));
const email = computed(() => localStorage.getItem('email'));

const socketStore = useSocketStore();

const userStore = useUserStore();

const chatStore = useChatStore();

const authUserStore = useAuthUserStore();

const notificationStore = useNotificationStore();

provide('$socket', socket);

const notificationMessage = reactive<INotificationMessage>({
  id: 0,
  title: "",
  content: "",
  roomChatId: 0,
  avatar: ""
});

socket.on("send message", (message) => {
  chatStore.updateMessage(message);
  const notification = JSON.parse(message.notification);
  notificationMessage.avatar = notification.avatar;
  notificationMessage.content = notification.content;
  notificationMessage.title = notification.title;
  notificationMessage.roomChatId = message.roomChatId;
  notificationStore.setActiveNotification(notificationMessage, message.sender.email);
});

socket.on("user connected", (user) => {
  userStore.updateOnlineStatus(user.username, true);
});

socket.on("user disconnected", (user) => {
  userStore.updateOnlineStatus(user.username, false);
});

interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  _retry?: boolean
}

const payload = reactive<TokenResponse>({
  refresh_token: refresh_token.value,
});

axios.interceptors.request.use(
  (config) => {
    if (access_token.value) {
      config.headers.Authorization = `Bearer ${access_token.value}`
    }
    return config
  },
  async (error: AxiosError) => {
    return Promise.reject(error)
  }
)

axios.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  async (error: AxiosError) => {
    const { logout, refreshToken } = useAuthUserStore()
    const originalRequest = error.config as CustomAxiosRequestConfig
    // console.log(error)
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      try {
        if (refresh_token.value) {
          const data = await refreshToken(payload)
          if (data) {
            // update access token in original response
            if (originalRequest.headers !== undefined) {
              originalRequest.headers.Authorization = `Bearer ${data}`
              
            }
          }
        }

        // retry the original request
        return axios(originalRequest)
      } catch (error: unknown) {
        // logout the user and redirect to login page
        logout()
        return Promise.reject(error)
      }
    }
    // window.location.reload()
    // if(error.response?.status === 401 && originalRequest._retry){
    //   logout()
    // }
    // logout the user and redirect to login page


    return Promise.reject(error)
  }
)

// Fixes
// todo fix clicking back to close conversations.

// future features:
// todo add video calling
// todo add stories

// Refactoring code:
// todo reorganize component structure
// todo rerfactor make everything that can be a ui component into one.
// todo refactor remove getters from utils file and add them to store folder.
// todo improve the video component.

// Accessability:
// todo improve the way you view messages.
// todo make multi-select more accessible.
// todo make dropdown menus more accessible.
// todo make modals more accessible.
// todo make lists (i.e conversations, contacts, calls) more accessible.

// SEO.
// todo improve seo.

// Performance:
// todo add dynamic imports.
// todo add chunking.

const store = useStore();


// // Router
// import { useAuthUserStore } from '!/stores/auth/useAuthUserStore'

// const { logout } = useAuthUserStore()

// // logout all window instances
// const syncLogout = (event: StorageEvent) => {
//   if (event.key === 'logout') {
//     window.removeEventListener('storage', syncLogout)
//     logout()
//   }
// }

// window.addEventListener('storage', syncLogout)
// update localStorage with state changes
store.$subscribe((_mutation, state) => {
  localStorage.setItem("chat", JSON.stringify(state));
});

// here we load the data from the server.
onMounted(async () => {
  store.status = "loading";
  if (email.value && access_token.value) {
    socketStore.connect(email.value, access_token.value);
    socketStore.bindEvents();
    chatStore.getRoomChatList(1);
  }
  // fake server call
  setTimeout(() => {
    store.delayLoading = false;
  });
  const request = await fetchData();

  store.$patch({
    status: "success",
    user: request.data.user,
    conversations: request.data.conversations,
    notifications: request.data.notifications,
    archivedConversations: request.data.archivedConversations,
  });
});
onBeforeUnmount(() => {
  socket.close();
})
// the app height
const height = ref(`${window.innerHeight}px`);

// change the app height to the window hight.
const resizeWindow = () => {
  height.value = `${window.innerHeight}px`;
};

// and add the resize event when the component mounts.
onMounted(() => {
  window.addEventListener("resize", resizeWindow);
});

// remove the event when un-mounting the component.
onUnmounted(() => {
  window.removeEventListener("resize", resizeWindow);
});
</script>

<template>
  <div :class="{ dark: store.settings.darkMode }">
    <div class="bg-white dark:bg-gray-800 transition-colors duration-500" :style="{ height: height }">
      <router-view v-slot="{ Component }">
        <FadeTransition>
          <component :is="Component" />
        </FadeTransition>
      </router-view>
    </div>
  </div>
</template>
