<script setup lang="ts">
import useStore from "@src/store/store";
import { computed, onMounted, Ref, reactive } from "vue";
import NoContacts from "@src/components/states/empty-states/NoContacts.vue";
import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import ContactItem from "@src/components/shared/blocks/ContactItem.vue";
import ScrollBox from "@src/components/ui/utils/ScrollBox.vue";
import { useChatStore } from "@src/stores/useChatStore";
import { useUserStore } from "@src/stores/useUserStore";
import { IContact } from "@src/interface/userdata";
import { IConversation, IMessage, IRoomChat } from "@src/interface/chat";
import { getFullName } from "@src/utils";
import { vInfiniteScroll } from '@vueuse/components'

const store = useStore();

const userStore = useUserStore();

const chatStore = useChatStore();


const contacts = computed(() => userStore.getAllContactInSystem)

const hasNextContactAll = computed(() => userStore.getHasNextContactAll)

const message = reactive<IMessage>({
  id: 1,
  type: "",
  content: "",
  date: "3:00 pm",
  state: "read",
  sender: {
    id: 0,
    email: "user@gmail.com",
    firstName: "Elijah",
    lastName: "Sabrina",
    lastSeen: new Date(),
    avatar: "",
    online: false
  },
  roomChatId: 0
});

const newChatRoom = reactive<IRoomChat>({
  id: 0,
  type: 'couple',
  name: "test",
  avatar: "",
  admins: [],
  message: message,
  pinnedMessage: message,
  pinnedMessageHidden: false,
  replyMessage: message,
  unread: 0,
  draftMessage: "",
});

const newConversation = reactive<IConversation>({
  id: 0,
  type: 'couple',
  name: "test",
  avatar: "",
  admins: [],
  contacts: [],
  messages: [],
  pinnedMessage: undefined,
  pinnedMessageHidden: false,
  replyMessage: undefined,
  unread: 0,
  draftMessage: "",
});

const listContactNew: IContact[] = [];

const receiveContactSelected = (contact: IContact) => {
  newChatRoom.avatar = contact.avatar;
  newChatRoom.name = getFullName(contact);
  chatStore.updateModalStatus(false);
  chatStore.activeConversationId = newChatRoom.id;
  listContactNew.push(contact);
  newConversation.contacts = listContactNew;
  newConversation.avatar = contact.avatar;
  newConversation.name = getFullName(contact);
  chatStore.addNewRoomChat(newChatRoom, newConversation);
  chatStore.conversationOpen = "open";
}

onMounted(() => {
  if (contacts) {
    userStore.getAllContactInSystemApi(1);
  }
});

function handleScroll() {
  
  console.log('[Load more handleScroll]')
}

function onLoadMore() {
  
  console.log('[Load more hasNextContactAll]')
}
</script>

<template>
  <div class="pb-6">
    <!--search-->
    <div class="mx-5 mb-5">
      <SearchInput />
    </div>

    <!--contacts-->
    <ScrollBox v-on:scroll.native="handleScroll()" class="overflow-y-scroll max-h-[12.5rem]"    
    v-infinite-scroll="[
      onLoadMore, { 
        distance: 0,
        interval: 1000,
        canLoadMore: () => {
          return hasNextContactAll == false
        }
      }
    ]">
      <Loading1 v-if="store.status === 'loading' || store.delayLoading" v-for="item in 3" />

      <ContactItem v-else-if="store.status === 'success' &&
        !store.delayLoading &&
        store.user &&
        store.user.contacts.length > 0
        " v-for="(contact, index) in contacts" :key="index" :contact="contact"
        @contact-selected="receiveContactSelected" />

      <NoContacts vertical v-else />
    </ScrollBox>
  </div>
</template>
