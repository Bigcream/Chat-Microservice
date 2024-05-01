// Pinia store
import { defineStore } from 'pinia'


// Api
import axios from '@src/apis/axios'


// Interface
import { IConversation, IRoomChat } from '@src/interface/chat';
import { IRoomAction, IGroupValues } from '@src/interface/roomAction';


export const useRoomActionStore = defineStore('roomAction', {

  state: () => {
    return {
      conversations: {} as IConversation,
      roomChats: [] as IRoomChat[]
    }
  },
  getters: {
    listRoomChatByEmail: (state) => {
      return state.roomChats
    }
  },
  actions: {
    async promoteToAdmin(roomAction: IRoomAction): Promise<void> {
      try {

        const { memberId: memberId, roomChatId: roomChatId } = roomAction
        await axios.post('/chat/promote-admin?memberId=' + memberId + "&roomChatId=" + roomChatId)

      } catch (error: unknown) {
        console.log('error')
      }
    },
    async demoteToMember(roomAction: IRoomAction): Promise<void> {
      try {

        const { memberId: memberId, roomChatId: roomChatId } = roomAction
        await axios.post('/chat/demote-member?memberId=' + memberId + "&roomChatId=" + roomChatId)

      } catch (error: unknown) {
        console.log('error')
      }
    },
    async removeMember(roomAction: IRoomAction): Promise<void> {
      try {

        const { memberId: memberId, roomChatId: roomChatId } = roomAction
        await axios.delete('/chat/remove-member?memberId=' + memberId + "&roomChatId=" + roomChatId)

      } catch (error: unknown) {
        console.log('error')
      }
    }
  }
})
