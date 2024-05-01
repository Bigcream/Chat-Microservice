import { reactive, computed } from "vue";
import { io } from "socket.io-client";
// import { useChatStore } from "@src/stores/useChatStore";

// const chatStore = useChatStore();

// const activeConversation = computed(() => chatStore.activeConversation)

export const state = reactive({
  connected: false,
  fooEvents: [],
  barEvents: []
});

// "undefined" means the URL will be computed from the `window.location` object
const URL = import.meta.env.VITE_VUE_APP_SOCKET_URL
const access_token = localStorage.getItem('access_token');
// export const socket = io(URL);

export const socket = io(URL, {
  autoConnect: false,
  path: "/socket/ws"
});

// export const socket = io(URL, {
//   reconnectionDelayMax: 10000,
//   autoConnect: false,
//   path: "/socket/ws",
//   auth: {
//     token: "123"
//   },
//   query: {
//     "my-key": "my-value"
//   }
// });

socket.on("disconnect", (reason) => {
  if (reason === "io server disconnect") {
    // the disconnection was initiated by the server, you need to reconnect manually
    socket.connect();
  }
});

// socket.on("users", (users) => {
//   console.log("users", users)
// });

// socket.on("friend request", () => {
//   console.log("friend request")
// });
// socket.on("send message", (message) => {
//   console.log("user test", message)
//   // activeConversation.value.messages.push(message);
// });

// socket.on("foo", (...args) => {
//   state.fooEvents.push(args);
// });

// socket.on("bar", (...args) => {
//   state.barEvents.push(args);
// });