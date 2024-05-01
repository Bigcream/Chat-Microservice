<script setup lang="ts">
import { computed } from "vue";
import MesssageNotification from "./MesssageNotification.vue";
import FriendNotification from "./FriendNotification.vue";
import GroupNotification from "./GroupNotification.vue";
import { INotification } from "@src/interface/notification";

const props = defineProps<{
  notification: INotification;
}>();

const ActiveComponent = computed((): any => {
  if (props.notification.type === "FRIEND_NOTIFICATION") {
    return FriendNotification;
  } else if (props.notification.type === "DM_NOTIFICATION") {
    return MesssageNotification;
  } else if (props.notification.type === "ROOM_ACTION_NOTIFICATION") {
    return GroupNotification;
  }
});
</script>

<template>
  <component :is="ActiveComponent" :notification = "notification"/>
</template>
