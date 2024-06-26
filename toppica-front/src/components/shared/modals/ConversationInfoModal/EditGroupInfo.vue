<script setup lang="ts">
import { ArrowUturnLeftIcon } from "@heroicons/vue/24/solid";
import Typography from "@src/components/ui/data-display/Typography.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import DropFileUpload from "@src/components/ui/inputs/DropFileUpload.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import { Ref, ref, computed, onMounted } from "vue";
import { IGroupValues } from '@src/interface/roomAction';
import { useChatStore } from "@src/stores/useChatStore";

const emit = defineEmits(["active-page-change"]);

const chatStore = useChatStore();

const activeConversation = computed(() => chatStore.activeConversation)

interface GroupValues {
  id: number;
  name: string | undefined;
  avatar: File | undefined;
}

const groupValues: Ref<GroupValues> = ref({
  id: 0,
  name: "",
  avatar: undefined
});

const payload: Ref<IGroupValues> = ref({
  id: 0,
  name: "",
  avatar: undefined
});

const handleSubmit = () => {
  payload.value.id = activeConversation.value.id;
  payload.value.name = groupValues.value.name;
  payload.value.avatar = activeConversation.value.avatar;
  chatStore.updateRoomInfo(payload.value, groupValues.value.avatar);
  emit('active-page-change', {
    tabName: 'conversation-info',
    animationName: 'slide-right',
  })
};

onMounted(() => {
  groupValues.value.name = activeConversation.value.name;

})

</script>

<template>
  <div>
    <!--header-->
    <div class="px-5 mb-6 flex justify-between items-center">
      <Typography id="modal-title" variant="heading-1" class="default-outline" tabindex="0">
        Edit Group Info
      </Typography>

      <!--return button-->
      <button @click="
        $emit('active-page-change', {
          tabName: 'conversation-info',
          animationName: 'slide-right',
        })
        "
        class="group p-2 border rounded-full border-gray-200 dark:border-white dark:border-opacity-70 focus:outline-none focus:border-indigo-100 focus:bg-indigo-100 hover:bg-indigo-100 hover:border-indigo-100 dark:hover:border-indigo-400 dark:hover:bg-indigo-400 dark:focus:bg-reindigod-400 dark:focus:border-indigo-400 transition-all duration-200 outline-none">
        <ArrowUturnLeftIcon
          class="w-5 h-5 text-black opacity-50 dark:text-white dark:opacity-70 group-hover:text-indigo-500 group-hover:opacity-100 dark:group-hover:text-white" />
      </button>
    </div>

    <!--inputs-->
    <div class="px-5 mb-6">
      <div class="mb-5">
        <TextInput :value="groupValues?.name" @value-changed="(value) => (groupValues.name = value)" type="text"
          placeholder="Group name" label="Name" />
      </div>

      <div>
        <DropFileUpload :value="groupValues.avatar" @value-changed="(value) => (groupValues.avatar = value)"
          label="Avatar" />
      </div>
    </div>

    <!--save button-->
    <div class="px-5">
      <Button @click="
        handleSubmit()
        " class="w-full px-5 bg-indigo-400 hover:bg-indigo-500 active:bg-indigo-500">
        Save
      </Button>
    </div>
  </div>
</template>
