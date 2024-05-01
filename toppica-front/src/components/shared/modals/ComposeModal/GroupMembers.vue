<script setup lang="ts">
import { ref, computed, reactive, inject } from "vue";
import type { Ref } from "vue";

import useStore from "@src/store/store";

import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import ContactItem from "@src/components/shared/blocks/ContactItem.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import Checkbox from "@src/components/ui/inputs/Checkbox.vue";
import ScrollBox from "@src/components/ui/utils/ScrollBox.vue";
import { useUserStore } from "@src/stores/useUserStore";
import { useChatStore } from "@src/stores/useChatStore";
import { IConversation, IMessage, IRoomChat } from "@src/interface/chat";
import { IContact } from "@src/interface/userdata";


const chatStore = useChatStore();

const store = useStore();

const userStore = useUserStore();

const contacts = computed(() => userStore.getContactsOfUser)
// a list of contacts selected to make a call
const selectedContacts: Ref<IContact[]> = ref([]);

// checks whether a contact is selected or not
const isContactSelected = (contact: IContact) => {
  if (contact) {
    return Boolean(
      selectedContacts.value.find((item) => item.id === contact.id)
    );
  } else {
    return false;
  }
};

// (event) change the value of selected contacts
const handleSelectedContactsChange = (contact: IContact) => {
  let contactIndex = selectedContacts.value.findIndex(
    (item) => item.id === contact.id
  );
  if (contactIndex !== -1) {
    selectedContacts.value.splice(contactIndex, 1);
  } else {
    selectedContacts.value.push(contact);
  }
};
const message = reactive<IMessage>({
  id: 1,
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
  },
  roomChatId : 0,
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
  type: 'group',
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

const  {groupName}  = inject('groupName')

const onClick = () => {
  // newChatRoom.avatar = contact.avatar;
  newChatRoom.name = groupName.value;
  chatStore.updateModalStatus(false);
  chatStore.activeConversationId = newChatRoom.id;
  newConversation.contacts = selectedContacts.value;
  newConversation.name = groupName.value;
  chatStore.addNewRoomChat(newChatRoom, newConversation);
  chatStore.conversationOpen = "open";
};
</script>

<template>
  <div>
    <!--search-->
    <div class="mx-5 mt-3 mb-5">
      <SearchInput />
    </div>

    <!--contacts-->
    <ScrollBox class="overflow-y-scroll max-h-[12.5rem] mb-5">
      <ContactItem
        v-if="store.status === 'success' && !store.delayLoading && store.user"
        v-for="(contact, index) in contacts"
        :contact="contact"
        @click="handleSelectedContactsChange(contact)"
        :active="isContactSelected(contact)"
        :key="index"
      >
        <template v-slot:checkbox>
          <Checkbox :value="isContactSelected(contact)" />
        </template>
      </ContactItem>

      <Loading1
        v-if="store.status === 'loading' || store.delayLoading"
        v-for="item in 3"
      />
    </ScrollBox>

    <div class="flex px-5 mt-5 pb-6">
      <div class="grow"></div>
      <!--previous button-->
      <Button
        @click="
          $emit('active-page-change', {
            tabName: 'group-info',
            animationName: 'slide-right',
          })
        "
        variant="ghost"
        class="px-5 mr-4"
      >
        <Typography variant="body-5" no-color> Previous </Typography>
      </Button>

      <!--next button-->
      <Button
      @click="onClick"
        class="px-5 bg-indigo-400 hover:bg-indigo-500 active:bg-indigo-500"
      >
        Finish
      </Button>
    </div>
  </div>
</template>
