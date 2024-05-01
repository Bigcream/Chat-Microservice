<script setup lang="ts">
import { INotification } from '@src/interface/notification';
import { useUserStore } from '@src/stores/useUserStore';
import { useNotificationStore } from '@src/stores/useNotificationStore';


const userStore = useUserStore();

const notificationStore = useNotificationStore();


const contactAction = (status: number, addContact: string, id: number) => {
  userStore.contactAction(status, addContact);
  console.log(props.notification)
  notificationStore.deleteNotification(id);
};

const props = defineProps<{
  notification: INotification;
}>();


</script>

<template>
  <div class="divide-y divide-gray-100 dark:divide-gray-700">
    <a href="#" class="flex px-4 py-3 hover:bg-gray-100 dark:hover:bg-gray-700">
      <div class="flex-shrink-0">
        <img class="rounded-full w-11 h-11" :src="props.notification.payload.avatar" style="width: 2.5rem; height: 2.5rem;" alt="Jese image">
        <div class="relative flex items-center justify-center w-5 h-5 ms-6 -mt-5 bg-gray-900 border border-white rounded-full dark:border-gray-800">
          <svg class="w-2 h-2 text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 18">
            <path d="M6.5 9a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9ZM8 10H5a5.006 5.006 0 0 0-5 5v2a1 1 0 0 0 1 1h11a1 1 0 0 0 1-1v-2a5.006 5.006 0 0 0-5-5Zm11-3h-2V5a1 1 0 0 0-2 0v2h-2a1 1 0 1 0 0 2h2v2a1 1 0 0 0 2 0V9h2a1 1 0 1 0 0-2Z"/>
          </svg>
        </div>
      </div>
      <div class="w-full ps-3">
          <div class="text-gray-500 text-sm mb-1.5 dark:text-gray-400" style="margin-bottom: 0.3rem;" v-html="props.notification.payload.content"></div>
        
          <div class="text-xs text-blue-600 dark:text-blue-500">{{ props.notification.timeFormat }}</div>

          <div class="text-gray-500 text-sm mb-1.5 dark:text-gray-400" style="padding-top: 4px;">
            <a @click="contactAction(1, props.notification.sender, props.notification.id)" href="#" class="inline-flex items-center px-4 py-2 text-sm font-medium text-center
             text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none
              focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800" 
              style="font-size: 0.7rem;line-height: normal;">Accept</a>
            <a @click="contactAction(2, props.notification.sender, props.notification.id)" href="#" class="py-2 px-4 ms-2 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100
             dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700" 
             style="background-color: red;color: white;font-size: 0.7rem;">Reject</a>
          </div>
      </div>
    </a>
  </div>

</template>
