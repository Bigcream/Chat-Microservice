<script setup lang="ts">
import { IContactGroup, IContact } from "@src/interface/userdata";
import type { Ref } from "vue";
import { computed, ref } from "vue";

import {
  EllipsisVerticalIcon,
  InformationCircleIcon,
  TrashIcon,
} from "@heroicons/vue/24/outline";
import Typography from "@src/components/ui/data-display/Typography.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";
import { useUserStore } from "@src/stores/useUserStore";

const props = defineProps<{
  contactGroups?: IContactGroup[];
  bottomEdge?: number;
}>();
const userStore = useUserStore();

const contactByGroups = computed(() => userStore.contactByGroups)

const currentUser = computed(() => userStore.currentContactByEmail)

// the position of the dropdown menu.
const dropdownMenuPosition = ref(["top-6", "right-0"]);

// controll the states of contact dropdown menus
const dropdownMenuStates: Ref<boolean[][] | undefined> = ref(
  props.contactGroups?.map((contactGroup) => {
    let group = contactGroup.contacts.map(() => false);
    return group;
  })
);

// close all contact dropdown menus
const handleCloseAllMenus = () => {
  dropdownMenuStates.value = props.contactGroups?.map((contactGroup) => {
    let group = contactGroup.contacts.map(() => false);
    return group;
  });
};

// (event) open/close the selected dropdown menu.
const handleToggleDropdown = (
  event: Event,
  groupIndex: number,
  index: number
) => {
  if (props.bottomEdge) {
    let buttonBottom = (
      event.currentTarget as HTMLElement
    ).getBoundingClientRect().bottom;

    if (buttonBottom >= props.bottomEdge - 75) {
      dropdownMenuPosition.value = ["bottom-6", "right-0"];
    } else {
      dropdownMenuPosition.value = ["top-6", "right-0"];
    }
  }

  dropdownMenuStates.value = (dropdownMenuStates.value as boolean[][]).map(
    (group) => {
      return group.map((value, idx) => {
        if (idx === index) return value;
        else return false;
      });
    }
  );

  dropdownMenuStates.value[groupIndex][index] = !(
    dropdownMenuStates.value as boolean[][]
  )[groupIndex][index];
};

// (event) close doprdown menu when clicking outside
const handleClickOutside = (event: Event) => {
  let target = event.target as HTMLElement;
  let parentElement = target.parentElement as HTMLElement;

  if (
    target &&
    !target.classList.contains("open-menu") &&
    !(parentElement && parentElement.classList.contains("open-menu"))
  ) {
    handleCloseAllMenus();
  }
};

const getFullName = (contact: IContact) => {
  return contact.firstName + " " + contact.lastName;
};

const removeContact = (receiverId: number) => {
  userStore.removeContact(currentUser.value.id , receiverId);
};

const CallStatusIcon = computed(() => {

});
</script>

<template>
  <div v-for="(group, groupIndex) in contactByGroups" :key="groupIndex">
    <!--group title-->
    <Typography variant="heading-3" class="w-full px-5 pb-3 pt-5">
      {{ group.letter }}
    </Typography>

    <!--contacts-->
    <div v-for="(contact, index) in group.contacts" :key="index">


      <ul role="list" class="max-w-sm divide-y divide-gray-200 dark:divide-gray-700">
        <li class="py-3 sm:py-4">
          <div class="flex items-center space-x-3 rtl:space-x-reverse">
            <div class="flex-shrink-0">
              <img class="w-8 h-8 rounded-full" :src="contact.avatar" alt="Neil image">
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold text-gray-900 truncate dark:text-white">
                {{ getFullName(contact) }}
              </p>
            </div>
            <span v-if="contact.online"
              class="inline-flex items-center bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-green-900 dark:text-green-300">
              <span class="w-2 h-2 me-1 bg-green-500 rounded-full"></span>
              Available
            </span>
            <span v-else
              class="inline-flex items-center bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-red-900 dark:text-red-300">
              <span class="w-2 h-2 me-1 bg-red-500 rounded-full"></span>
              Unavailable
            </span>
            <!--dropdown menu-->
            <div class="relative">
              <!--dropdown menu button-->
              <IconButton :id="'open-contact-menu-' + index"
                :aria-expanded="(dropdownMenuStates as boolean[][])[groupIndex][index]"
                :aria-controls="'contact-menu-' + index"
                @click="(event) => handleToggleDropdown(event, groupIndex, index)" class="open-menu w-6 h-6"
                title="toggle contact menu" aria-label="toggle contact menu">
                <EllipsisVerticalIcon class="open-menu h-5 w-5 text-black opacity-60 dark:text-white dark:opacity-70"
                  tabindex="0" />
              </IconButton>

              <Dropdown :id="'contact-menu-' + index" :close-dropdown="handleCloseAllMenus"
                :handle-click-outside="handleClickOutside" :aria-labelledby="'open-contact-menu-' + index"
                :show="(dropdownMenuStates as boolean[][])[groupIndex][index]" :position="dropdownMenuPosition">
                <DropdownLink>
                  <InformationCircleIcon class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70" />
                  Personal information
                </DropdownLink>

                <DropdownLink @click="removeContact(contact.id)" color="danger">
                  <TrashIcon class="h-5 w-5 mr-3" />
                  Delete contact
                </DropdownLink>
              </Dropdown>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
