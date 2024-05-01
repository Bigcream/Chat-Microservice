<script setup lang="ts">
import { INotification } from '@src/interface/notification';


const props = defineProps<{
  notification: INotification;
}>();
</script>

<template>
  <div class="divide-y divide-gray-100 dark:divide-gray-700">
    <a href="#" class="flex px-4 py-3 hover:bg-gray-100 dark:hover:bg-gray-700">
      <div class="flex-shrink-0">
        <img class="rounded-full w-11 h-11" :src="props.notification.payload.avatar" style="width: 2.5rem; height: 2.5rem;" alt="Jese image">
        <div
          class="relative flex items-center justify-center w-5 h-5 ms-6 -mt-5 bg-blue-600 border border-white rounded-full dark:border-gray-800">
          <svg class="w-2 h-2 text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor"
            viewBox="0 0 18 18">
            <path
              d="M1 18h16a1 1 0 0 0 1-1v-6h-4.439a.99.99 0 0 0-.908.6 3.978 3.978 0 0 1-7.306 0 .99.99 0 0 0-.908-.6H0v6a1 1 0 0 0 1 1Z" />
            <path
              d="M4.439 9a2.99 2.99 0 0 1 2.742 1.8 1.977 1.977 0 0 0 3.638 0A2.99 2.99 0 0 1 13.561 9H17.8L15.977.783A1 1 0 0 0 15 0H3a1 1 0 0 0-.977.783L.2 9h4.239Z" />
          </svg>
        </div>
      </div>
      <div class="w-full ps-3">
        <div class="text-gray-500 text-sm mb-1.5 dark:text-gray-400" v-html="props.notification.payload.content"></div>
        <div class="text-xs text-blue-600 dark:text-blue-500">{{ props.notification.timeFormat }}</div>
    </div>
  </a>
</div></template>
