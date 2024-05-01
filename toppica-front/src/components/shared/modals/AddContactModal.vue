<script setup lang="ts">
import Typography from "@src/components/ui/data-display/Typography.vue";
import Button from "@src/components/ui/inputs/Button.vue";
import TextInput from "@src/components/ui/inputs/TextInput.vue";
import Modal from "@src/components/ui/utils/Modal.vue";
import { useUserStore } from "@src/stores/useUserStore";
import { getFullName } from "@src/utils";
import { computed } from "vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";

const userStore = useUserStore();

const contactNotFriends = computed(() => userStore.getContactNotFriends)

const props = defineProps<{
  openModal: boolean;
  closeModal: () => void;
}>();

const addContact = (addEmail: String) => {
  props.closeModal
  userStore.addContact(addEmail);
};
</script>

<template>
  <Modal :open="props.openModal" :closeModal="props.closeModal">
    <template v-slot:content>
      <div class="w-[18.75rem] bg-white dark:bg-gray-800 rounded py-6">
        <!--modal header-->
        <div class="flex justify-between items-center px-5">
          <Typography id="modal-title" variant="heading-1" class="default-outline" tabindex="0">
            Add Contact
          </Typography>

          <Button variant="outlined" color="danger" @click="props.closeModal" typography="body-4">
            esc
          </Button>
        </div>

      <!--search-->
      <div class="mx-5 mb-5" style="padding-top: 10px;">
        <SearchInput />
      </div>

        <div v-for="(contact, index) in contactNotFriends" class="w-full max-w-md p-4 bg-white rounded-lg ">
          <div class="flow-root">
            <ul role="list" class="divide-y divide-gray-200 dark:divide-gray-700">
              <li class="py-3 sm:py-4">
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <img class="w-8 h-8 rounded-full" :src="contact.avatar" alt="Neil image">
                  </div>
                  <div class="flex-1 min-w-0 ms-4">
                    <p class="text-sm font-medium text-gray-900 truncate dark:text-white" style="padding-right: 100px;">
                      {{ getFullName(contact) }}
                    </p>
                  </div>
                  <div class="inline-flex items-center text-base font-semibold text-gray-900 dark:text-white"
                    style="padding: 5px;">
                    <a @click="addContact(contact.email)" href="#" class="inline-flex items-center px-4 py-2 text-sm font-medium text-center
             text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none
              focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
                      style="font-size: 0.7rem;line-height: normal;">Add</a>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>

      </div>
    </template>
  </Modal>
</template>
