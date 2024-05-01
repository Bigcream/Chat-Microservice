<script setup lang="ts">
import type { INotification } from "@src/types";
import useStore from "@src/store/store";

import NoNotifications from "@src/components/states/empty-states/NoNotifications.vue";
import Loading1 from "@src/components/states/loading-states/Loading1.vue";
import Notification from "@src/components/views/HomeView/Sidebar/Notifications/Notification.vue";
import SidebarHeader from "@src/components/views/HomeView/Sidebar/SidebarHeader.vue";
import { useNotificationStore } from "@src/stores/useNotificationStore";
import { onMounted,computed } from "vue";

const store = useStore();

const notificationStore = useNotificationStore();

const notifications = computed(() => notificationStore.getListNotification)
import { socket } from "@src/socket/socket";

socket.on("friend request", (notification) => {
    console.log("friend request", notification);
    notificationStore.updateListNotification(notification);
  });
onMounted(() => {
  notificationStore.getListNotificationByEmail(1);
})
</script>

<template>
  <div>
    <SidebarHeader>
      <template v-slot:title>Notifications</template>
    </SidebarHeader>

    <div
      class="w-full h-full scroll-smooth scrollbar-hidden"
      style="overflow-x: visible; overflow-y: scroll"
    >
      <Loading1
        v-if="store.status === 'loading'  || store.delayLoading && notifications.length > 0"
        v-for="item in 6"
      />

      <Notification
        v-else-if="store.status === 'success' && !store.delayLoading"
        v-for="(notification, index) in notifications"
        :notification="notification"
        :key="index"
      />

      <NoNotifications v-if="notifications.length == 0" />
    </div>
  </div>
</template>
