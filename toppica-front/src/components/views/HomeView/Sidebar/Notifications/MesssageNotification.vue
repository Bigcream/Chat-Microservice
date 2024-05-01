<script setup lang="ts">
import { INotification } from "@src/interface/notification";

const props = defineProps<{
  notification: INotification;
}>();
</script>

<template>
  <div class="divide-y divide-gray-100 dark:divide-gray-700">
    <a href="#" class="flex px-4 py-3 hover:bg-gray-100 dark:hover:bg-gray-700">
      <div class="flex-shrink-0">
        <img class="rounded-full w-11 h-11" :src="props.notification.payload.avatar" style="width: 2.8rem; height: 2.8rem;" alt="Jese image">
        <div class="relative flex items-center justify-center w-5 h-5 ms-6 -mt-5 bg-green-400 border border-white rounded-full dark:border-gray-800">
          <svg class="w-2 h-2 text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 18">
            <path d="M18 0H2a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h2v4a1 1 0 0 0 1.707.707L10.414 13H18a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2Zm-5 4h2a1 1 0 1 1 0 2h-2a1 1 0 1 1 0-2ZM5 4h5a1 1 0 1 1 0 2H5a1 1 0 0 1 0-2Zm2 5H5a1 1 0 0 1 0-2h2a1 1 0 0 1 0 2Zm9 0h-6a1 1 0 0 1 0-2h6a1 1 0 1 1 0 2Z"/>
          </svg>
        </div>
      </div>
      <div class="w-full ps-3">
          <div class="text-gray-500 text-sm mb-1.5 dark:text-gray-400" v-html="$props.notification.payload.content" ></div>
          <div class="text-xs text-blue-600 dark:text-blue-500">{{$props.notification.timeFormat}}</div>
      </div>
    </a>
  </div>
</template>
