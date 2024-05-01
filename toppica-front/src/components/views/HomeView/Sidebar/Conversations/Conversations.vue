<script setup lang="ts">

import type { Ref } from "vue";
import { onMounted, ref, watch, computed, reactive } from "vue";

import useStore from "@src/store/store";

import { PencilSquareIcon } from "@heroicons/vue/24/outline";
import ComposeModal from "@src/components/shared/modals/ComposeModal/ComposeModal.vue";
import NoConversation from "@src/components/states/empty-states/NoConversation.vue";
import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import FadeTransition from "@src/components/ui/transitions/FadeTransition.vue";
import ArchivedButton from "@src/components/views/HomeView/Sidebar/Conversations/ArchivedButton.vue";
import ConversationsList from "@src/components/views/HomeView/Sidebar/Conversations/ConversationsList.vue";
import SidebarHeader from "@src/components/views/HomeView/Sidebar/SidebarHeader.vue";
import { useChatStore } from "@src/stores/useChatStore";
import { IRoomChat } from "@src/interface/chat";
import { socket } from "@src/socket/socket";
import { useNotificationStore } from "@src/stores/useNotificationStore";
import { INotificationMessage } from "@src/interface/notification";

const chatStore = useChatStore();

const notificationStore = useNotificationStore();

const store = useStore();

const keyword: Ref<string> = ref("");

let composeOpen = computed(() => chatStore.composeModalStatus)

// determines whether the archive is open or not
let openArchive = ref(false);

// the filtered list of conversations.

const filteredConversations: Ref<IRoomChat[]> = computed(() => chatStore.listRoomChatByEmail);

const archiveConversations: Ref<IRoomChat[]> = computed(() => chatStore.getroomChatArchives);


// filter the list of conversation based on search text.
watch([keyword, openArchive], () => {
  if (openArchive.value) {
    // search conversations
    chatStore.updateChatRoomArchiveWhenSearch(keyword.value);
    // filteredConversations.value =
    //   archiveConversations.value?.filter((room) =>
    //     room.name
    //       ?.toLowerCase()
    //       .includes(keyword.value.toLowerCase())
    //   ) || [];
  } else {
    // search archived conversations
    chatStore.updateChatRoomWhenSearch(keyword.value);
    // filteredConversations.value =
    // filteredConversations.value?.filter((room) =>
    //     room.name
    //       ?.toLowerCase()
    //       .includes(keyword.value.toLowerCase())
    //   ) || [];
  }
});

// (event) switch between the rendered conversations.
const handleConversationChange = (conversationId: number) => {
  chatStore.activeConversationId = conversationId;
  chatStore.getConversationById(conversationId)
  chatStore.conversationOpen = "open";
};

// (event) close the compose modal.
const closeComposeModal = () => {
  // composeOpen.value = false;
  chatStore.updateModalStatus(false);
};

// if the active conversation is in the archive
// then open the archive
onMounted(() => {
  chatStore.getRoomChatList(1);
  chatStore.getRoomChatList(2);
  let conversation = store.archivedConversations.find(
    (conversation) => conversation.id === store.activeConversationId
  );

  if (conversation) openArchive.value = true;
});

// const openArchiveConversation = () => {
//   openArchive.value = !openArchive.value
//   // composeOpen.value = false;
//   chatStore.updateModalStatus(false);
// };

</script>

<template>
  <div>
    <SidebarHeader>
      <!--title-->
      <template v-slot:title>Messages</template>

      <!--side actions-->
      <template v-slot:actions>
        <IconButton @click="chatStore.updateModalStatus(true)" aria-label="compose conversation"
          title="compose conversation" class="w-7 h-7">
          <PencilSquareIcon class="w-[1.25rem] h-[1.25rem] text-indigo-300 hover:text-indigo-400" />
        </IconButton>
      </template>
    </SidebarHeader>

    <!--search bar-->
    <div class="px-5 xs:pb-6 md:pb-5">
      <SearchInput v-model="keyword" />
    </div>

    <!--conversations-->
    <div role="list" aria-label="conversations" class="w-full h-full scroll-smooth scrollbar-hidden"
      style="overflow-x: visible; overflow-y: scroll">
      <Loading1 v-if="store.status === 'loading' || store.delayLoading" v-for="item in 6" />

      <div v-else>
        <ArchivedButton v-if="archiveConversations.length > 0" :open="openArchive" @click="openArchive = !openArchive" />

        <div v-if="store.status === 'success' &&
          !store.delayLoading &&
          filteredConversations.length > 0
          ">
          <FadeTransition>
            <component :is="ConversationsList" :filtered-conversations="filteredConversations"
              :active-id="(chatStore.activeConversationId as number)"
              :handle-conversation-change="handleConversationChange" :key="openArchive ? 'archive' : 'active'" />
          </FadeTransition>
        </div>

        <div v-else>
          <NoConversation v-if="archiveConversations.length === 0" />
        </div>
      </div>
    </div>

    <!--compose modal-->
    <ComposeModal :open="composeOpen" :close-modal="closeComposeModal" />
  </div>
</template>
