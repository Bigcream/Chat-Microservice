// Pinia store
import { defineStore } from 'pinia'


// Api
import axios from '@src/apis/axios'

import type { Ref } from "vue";
import { ref } from "vue";

// Interface
import { IConversation, IRoomChat, IMessageRequest, IMessage } from '@src/interface/chat';
import { IGroupValues } from '@src/interface/roomAction';

const activeConversationId: Ref<number> = ref(0);
const conversationOpen: Ref<string | undefined> = ref(
  "open"
);
const modalStatus: Ref<boolean> = ref(false);
export const useChatStore = defineStore('chatRoom', {

  state: () => {
    return {
      conversations: {} as IConversation,
      roomChats: [] as IRoomChat[],
      roomChatArchives: [] as IRoomChat[],
      activeConversationId: activeConversationId,
      conversationOpen: conversationOpen,
      composeModalStatus: modalStatus
    }
  },
  getters: {
    conversationById: (state) => {
      return state.conversations
    },
    getActiveConversationId: (state) => {
      return state.activeConversationId
    },
    activeConversation: (state) => {
      return state.conversations
    },
    listRoomChatByEmail: (state) => {
      return state.roomChats
    },
    getComposeModalStatus: (state) => {
      return state.composeModalStatus
    },
    getconversationOpen: (state) => {
      return state.conversationOpen
    },
    getroomChatArchives: (state) => {
      return state.roomChatArchives
    }
  },
  actions: {
    async getConversationById(roomId: number): Promise<void> {
      try {
        const { data } = await axios.get('/chat/room-chat/conversation?roomChatId=' + roomId + '&sender=' + localStorage.getItem('email'))
        this.conversations = data
      } catch (error: unknown) {
        console.log('error')
      }
    },
    async getRoomChatList(status: number): Promise<void> {
      try {
        const { data } = await axios.get('/chat/rooms?email=' + localStorage.getItem('email') + '&status=' + status)
        if(status == 1){
          this.roomChats = data
          if (this.roomChats) {
            if (this.activeConversationId == 0 && this.roomChats.length > 0) {
              this.activeConversationId = this.roomChats[0].id;
              this.getConversationById(this.activeConversationId)
            }
          }
        }

        if(status == 2){
          this.roomChatArchives = data
          if (this.roomChatArchives) {
            if (this.activeConversationId == 0 && this.roomChatArchives.length > 0) {
              this.activeConversationId = this.roomChatArchives[0].id;
              this.getConversationById(this.activeConversationId)
            }
          }
        }

      } catch (error: unknown) {
        console.log(error)
      }
    },
    async getRoomChatById(roomChatId: number): Promise<void> {
      try {
        const { data } = await axios.get('/chat/room-chat/' + roomChatId + '?email=' + localStorage.getItem('email'))
        this.roomChats.unshift(data);
        this.activeConversationId = data.id;
        this.getConversationById(this.activeConversationId)
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async readConversation(roomId: number): Promise<void> {
      try {
        axios.post('/chat/read-conversation/' + roomId + '?email=' + localStorage.getItem('email'));
        const index = this.roomChats.findIndex(room => room.id == roomId);
        const roomChat = this.roomChats[index];
        roomChat.unread = 0;
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async addNewRoomChat(room: IRoomChat, conversation: IConversation): Promise<void> {
      try {
        const existChat = this.roomChats.find(roomChat => roomChat.name == room.name);
        if (existChat) {
          this.activeConversationId = existChat.id;
          this.getConversationById(this.activeConversationId);
        } else {
          this.roomChats.unshift(room);
          this.conversations = conversation;
        }
      } catch (error: unknown) {
        console.log(error)
      }
    },
    updateModalStatus(status: boolean): void {
      try {
        this.composeModalStatus = status;
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async updateRoomInfo(roomValues: IGroupValues, file: File | undefined): Promise<void> {
      const { id: id, name: name, avatar: avatar } = roomValues
      var bodyFormData = new FormData();
      if (file) {
        bodyFormData.append("file", file);
      }
      bodyFormData.append("roomInfoString", JSON.stringify({ id, name, avatar }))
      const { data } = await axios.put('/chat/room-chat-info', bodyFormData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      this.activeConversation.avatar = data.avatar;
      this.activeConversation.name = data.name;
      const index = this.roomChats.findIndex(room => room.id == activeConversationId.value);
      this.roomChats[index].avatar = data.avatar;
      this.roomChats[index].name = data.name;
      console.log(data)
    },
    async sendMessage(payLoad: IMessageRequest): Promise<void> {
      try {
        const { data } = await axios.post('/chat/send-message', payLoad);
        // update if is create new room
        if(payLoad.roomChatId == 0){
          this.roomChats.splice(0, 1);
          await this.getRoomChatById(data.roomChatId);
        }
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async deleteConversation(roomChatId: number): Promise<void> {
      try {
        await axios.delete('/chat/room-chat/' + roomChatId)
        const index = this.roomChats.findIndex(room => room.id == roomChatId);
        this.roomChats.splice(index, 1);
        if (index == 0 && this.roomChats.length > 1) {
          this.activeConversationId = this.roomChats[index + 1].id;
          this.getConversationById(this.activeConversationId);
        }
        else if (this.roomChats.length > 1) {
          this.activeConversationId = this.roomChats[index - 1].id;
        }

      } catch (error: unknown) {
        console.log(error)
      }
    },
    updateMessage(message: IMessage): void {
      try {
        const roomChatId = message.roomChatId;
        const index = this.roomChats.findIndex(room => room.id == roomChatId);
        if (index == -1 && this.roomChats.length == 0) {
          this.getRoomChatById(roomChatId);
        }
        else {
          const roomChat = this.roomChats[index];
          roomChat.message = message;
          this.roomChats.splice(index, 1);
          this.roomChats.unshift(roomChat);
          const unread = this.roomChats[index].unread;
          if (message.sender.email != localStorage.getItem("email")) {
            if (unread) {
              this.roomChats[index].unread = unread + 1;
            } else {
              this.roomChats[index].unread = 1;
            }
          }
          if (activeConversationId.value === roomChatId) {
            this.activeConversation.messages.push(message);
          }
        }
      } catch (error: unknown) {
        console.log(error)
      }
    },
    async replyMessage(roomChatId: number): Promise<void> {
      try {
        const index = this.roomChats.findIndex(room => room.id == roomChatId);
        this.activeConversationId = roomChatId;
        const roomChat = this.roomChats[index];
        await this.getConversationById(roomChat.id);
        if (roomChat.type === "couple") {
          await this.readConversation(roomChatId);
        } else {
          roomChat.unread = 0;
        }
      } catch (error: unknown) {
        console.log(error)
      }
    },
    setConversationOpen(status: string): void {
      try {
        this.conversationOpen = status;
      } catch (error: unknown) {
        console.log(error)
      }
    },
    updateChatRoomWhenSearch(keyword: string): void {
      try {
        this.roomChats =
        this.roomChats?.filter((room) =>
          room.name
            ?.toLowerCase()
            .includes(keyword.toLowerCase())
        ) || [];
      } catch (error: unknown) {
        console.log(error)
      }
    },
    updateChatRoomArchiveWhenSearch(keyword: string): void {
      try {
        this.roomChats =
        this.roomChatArchives?.filter((room) =>
          room.name
            ?.toLowerCase()
            .includes(keyword.toLowerCase())
        ) || [];
      } catch (error: unknown) {
        console.log(error)
      }
    }
  }
})
