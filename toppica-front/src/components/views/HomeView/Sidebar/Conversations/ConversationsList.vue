<script setup lang="ts">
import type { IConversation } from "@src/types";
import Conversation from "./Conversation.vue";
import { IRoomChat } from "@src/interface/chat";

const props = defineProps<{
  filteredConversations?: IRoomChat[];
  handleConversationChange: (conversationId: number) => void;
  activeId?: number;
}>();
</script>

<template>
  <div>
    <Conversation
      v-for="conversation in filteredConversations"
      :conversation="conversation"
      :key="conversation.id"
      role="listitem"
      :handle-conversation-change="props.handleConversationChange"
      :is-active="activeId === conversation.id"
    />
  </div>
</template>
