// Pinia store
import { defineStore } from 'pinia'


// Api
import axios from '@src/apis/axios'


// Interface
import { Ref, ref } from 'vue';
import { INotification, INotificationMessage } from '@src/interface/notification';


const newMessageStatus: Ref<boolean> = ref(false);

const currentPage: Ref<number> = ref(1);

const hasNext: Ref<boolean> = ref(false);

export const useNotificationStore = defineStore('notification', {

  state: () => {
    return {
      currentPage: currentPage,
      hasNext: hasNext,
      newMessageStatus: newMessageStatus,
      notification: {} as INotificationMessage,
      notifications: [] as INotification[]
    }
  },
  getters: {
    activeNotification: (state) => {
      return state.notification
    },
    checkHaveNewMessage: (state) => {
      return state.newMessageStatus
    },
    getCurrentPage: (state) => {
      return state.currentPage
    },
    getHasNextStatus: (state) => {
      return state.hasNext
    },
    getListNotification: (state) => {
      return state.notifications
    }
  },
  actions: {
    setActiveNotification(notificationMessage: INotificationMessage, sender: string): void {
      const currentEmail = localStorage.getItem("email");
      if(currentEmail&& sender != currentEmail){
        this.notification = notificationMessage;
        this.updateStatusNewMessage(true);
      }
    },
    updateStatusNewMessage(status: boolean): void {
      try {
        this.newMessageStatus = status;
      } catch (error: unknown) {
        console.log('error updateStatusNewMessage')
      }
    },
    async getListNotificationByEmail(page : number): Promise<void> {
      try {
        const {data} = await axios.get('/chat/notifications/' + localStorage.getItem("email") + "?page=" + page + "&size=7");
        if(data.content){
          this.notifications = data.content;
        }
        this.currentPage = page;
        this.hasNext = data.hasNext;

      } catch (error: unknown) {
        console.log('error')
      }
    },
    updateListNotification(notification: INotification): void {
      try {
        this.notifications.unshift(notification);
      } catch (error: unknown) {
        console.log('error updateListNotification')
      }
    },
    async deleteNotification(id : number): Promise<void> {
      try {
        await axios.delete('/chat/notification/' + id);
        const index = this.notifications.findIndex(noti => noti.id == id);
        this.notifications.splice(index, 1);
      } catch (error: unknown) {
        console.log('error')
      }
    }
  }
})
