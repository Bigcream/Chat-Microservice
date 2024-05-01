import { defineStore } from "pinia";
import { socket } from "./socket";

export const useSocketStore = defineStore("connection", {
  state: () => ({
    isConnected: false,
  }),

  actions: {
    bindEvents() {
      socket.on("connect", () => {
        console.log("connect")
        this.isConnected = true;
      });

      socket.on("disconnect", () => {
        this.isConnected = false;
      });
    },

    connect(username : string, access_token : string) {
      socket.auth = { username };
      socket.auth.token = {access_token};
      socket.connect();
    }
  },
});