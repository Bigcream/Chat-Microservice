<script setup lang="ts">
import type { Ref } from "vue";
import { onMounted, ref, computed, onUpdated } from "vue";
import useStore from "@src/store/store";
import { useChatStore } from "@src/stores/useChatStore";
import Message from "@src/components/views/HomeView/Chat/ChatMiddle/Message/Message.vue";
import TimelineDivider from "@src/components/views/HomeView/Chat/ChatMiddle/TimelineDivider.vue";
import { useUserStore } from "@src/stores/useUserStore";
import { IMessage } from "@src/interface/chat";
import NewMessage from "@src/components/views/HomeView/Chat/Notification/NewMessage.vue"
import { useNotificationStore } from "@src/stores/useNotificationStore";
import { vInfiniteScroll } from '@vueuse/components'
import { on } from "events";

const props = defineProps<{
  handleSelectMessage: (messageId: number) => void;
  handleDeselectMessage: (messageId: number) => void;
  selectedMessages: number[];
}>();

const store = useStore();

const chatStore = useChatStore();

const userStore = useUserStore();

const notificationStore = useNotificationStore();

const container: Ref<HTMLElement | null> = ref(null);
  
const activeConversation = computed(() => chatStore.activeConversation)

const activeConversationId = computed(() => chatStore.getActiveConversationId)

let newMessage = computed(() => notificationStore.checkHaveNewMessage)

const currentContact = computed(() => userStore.currentContactByEmail)
// const activeConversation = <IConversation>inject("activeConversation");

// checks whether the previous message was sent by the same user.
const isFollowUp = (index: number, previousIndex: number): boolean => {
  if (previousIndex < 0) {
    return false;
  } else {
    let previousSender = activeConversation.value.messages[previousIndex].sender.id;
    let currentSender = activeConversation.value.messages[index].sender.id;
    return previousSender === currentSender;
  }
};

// checks whether the message is sent by the authenticated user.
const isSelf = (message: IMessage): boolean => {
  return Boolean(currentContact && message.sender.id === currentContact.value.id);
};

// checks wether the new message has been sent in a new day or not.
const renderDivider = (index: number, previousIndex: number): boolean => {
  if (index === 3) {
    return true;
  } else {
    return false;
  }
};

const isChangeConversation = computed(() => {
  return chatStore.getActiveConversationId;
})

onUpdated(() => {
  if(isChangeConversation){
    (container.value as HTMLElement).scrollTop = (
    container.value as HTMLElement
  ).scrollHeight;
  }
})
// scroll messages to bottom.
onMounted(() => {
  (container.value as HTMLElement).scrollTop = (
    container.value as HTMLElement
  ).scrollHeight;
});

function onLoadMore() {
  
  console.log('[Load more e oi]')
}

</script>

<template>

  <div
    ref="container"
    class="grow px-5 py-5 flex flex-col overflow-y-scroll scrollbar-hidden"
    v-infinite-scroll="[
      onLoadMore, { 
        distance: 0,
        direction: 'top',
        interval: 1000,
        canLoadMore: (el) => {
          return store.status != 'loading'
        }
      }
    ]"
  >
  <NewMessage v-if="newMessage"/>
    <div
      v-if="store.status !== 'loading'"
      v-for="(message, index) in activeConversation.messages"
      :key="index"
    >
      <TimelineDivider v-if="renderDivider(index, index - 1)" />

      <Message
        :message="message"
        :self="isSelf(message)"
        :follow-up="isFollowUp(index, index - 1)"
        :divider="renderDivider(index, index - 1)"
        :selected="props.selectedMessages.includes(message.id)"
        :handle-select-message="handleSelectMessage"
        :handle-deselect-message="handleDeselectMessage"
      />
    </div>
  </div>
</template>
