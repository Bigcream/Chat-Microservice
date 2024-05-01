<script setup lang="ts">
import type { IConversation } from "@src/types";
import type { Ref } from "vue";
import { ref, computed, reactive } from "vue";
import { EllipsisVerticalIcon } from "@heroicons/vue/24/outline";
import { ArrowUturnLeftIcon } from "@heroicons/vue/24/solid";
import ContactItem from "@src/components/shared/blocks/ContactItem.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import IconButton from "@src/components/ui/inputs/IconButton.vue";
import SearchInput from "@src/components/ui/inputs/SearchInput.vue";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";
import ScrollBox from "@src/components/ui/utils/ScrollBox.vue";
import { useUserStore } from "@src/stores/useUserStore";
import { useRoomActionStore } from "@src/stores/useRoomActionStore";
import { IContact } from "@src/interface/userdata";
import { IRoomAction } from '@src/interface/roomAction';

const props = defineProps<{
  closeModal: () => void;
  conversation: IConversation;
}>();

const roomActionStore = useRoomActionStore();

const userStore = useUserStore();

const currentUser = computed(() => userStore.currentContact)

// html container of the contacts list
const contactContainer: Ref<HTMLElement | undefined> = ref();

// controll the states of contact dropdown menus
const dropdownMenuStates: Ref<boolean[] | undefined> = ref(
  props.conversation.contacts?.map(() => false)
);

// the position of the dropdown menu
const dropdownMenuPosition = ref(["top-6", "right-0"]);

// (event) close all dropdowns
const closeDropdowns = () => {
  dropdownMenuStates.value = props.conversation.contacts?.map(() => false);
};

// (event) open/close the dropdown menu
const handleToggleDropdown = (event: Event, contactIndex: number) => {
  if (contactContainer) {
    let buttonBottom = (
      event.currentTarget as HTMLElement
    ).getBoundingClientRect().bottom;
    let containerBottom = (
      contactContainer.value as HTMLElement
    ).getBoundingClientRect().bottom;

    if (buttonBottom >= containerBottom - 50) {
      dropdownMenuPosition.value = ["bottom-6", "right-0"];
    } else {
      dropdownMenuPosition.value = ["top-6", "right-0"];
    }
  }

  dropdownMenuStates.value = props.conversation.contacts?.map(
    (value, index) => {
      if (contactIndex === index) {
        return true;
      } else {
        return false;
      }
    }
  );
};

// (event) close doprdown menu when clicking outside
const handleClickOutside = (event: Event) => {
  let target = event.target as HTMLElement;

  if (
    target.parentElement &&
    !target.classList.contains("open-menu") &&
    !(target.parentElement as HTMLElement).classList.contains("open-menu")
  ) {
    closeDropdowns();
  }
};

const payload = reactive<IRoomAction>({
  memberId: 0,
  roomChatId: 0
});

const handlePromoteToAdmin = (contact: IContact) => {
  if (!(props.conversation.admins as number[]).includes(contact.id)) {
    payload.memberId = contact.id;
    payload.roomChatId = props.conversation.id;
    roomActionStore.promoteToAdmin(payload);
  }
  props.conversation.admins?.push(payload.memberId);
};

const handleDemoteToMember = (contact: IContact) => {
  if ((props.conversation.admins as number[]).includes(contact.id)) {
    payload.memberId = contact.id;
    payload.roomChatId = props.conversation.id;
    roomActionStore.demoteToMember(payload);
    const arr2 = props.conversation.admins?.filter(x => x !== payload.memberId);
    props.conversation.admins = [];
    props.conversation.admins = arr2;
  }
};

const removeMember = (contact: IContact) => {
  if ((props.conversation.admins as number[]).includes(currentUser.value.id)) {
    payload.memberId = contact.id;
    payload.roomChatId = props.conversation.id;
    roomActionStore.removeMember(payload);
    const arr2 = props.conversation.contacts.filter(x => x !== contact);
    props.conversation.contacts = [];
    props.conversation.contacts = arr2;
  }
};
</script>

<template>
  <div>
    <!--header-->
    <div class="flex justify-between items-center mb-6 px-5">
      <Typography id="modal-title" variant="heading-1" class="default-outline">
        Members
      </Typography>

      <button @click="
        $emit('active-page-change', {
          tabName: 'conversation-info',
          animationName: 'slide-right',
          removeContact: true,
        })
        "
        class="group p-2 border rounded-full border-gray-200 dark:border-white dark:border-opacity-70 focus:outline-none focus:border-indigo-100 focus:bg-indigo-100 hover:bg-indigo-100 hover:border-indigo-100 dark:hover:border-indigo-400 dark:hover:bg-indigo-400 dark:focus:bg-reindigod-400 dark:focus:border-indigo-400 transition-all duration-200 outline-none">
        <ArrowUturnLeftIcon
          class="w-5 h-5 text-black opacity-50 dark:text-white dark:opacity-70 group-hover:text-indigo-500 group-hover:opacity-100 dark:group-hover:text-white" />
      </button>
    </div>

    <!--search-->
    <div class="mb-5 mx-5">
      <SearchInput />
    </div>

    <!--contacts-->
    <div ref="contactContainer">
      <ScrollBox class="max-h-[14.5rem] overflow-y-scroll">
        <ContactItem variant="card" @contact-selected="(contact) =>
          $emit('active-page-change', {
            tabName: 'conversation-info',
            animationName: 'slide-left',
            contact: contact,
          })
          " v-for="(contact, index) in props.conversation.contacts" :contact="contact" :key="index">
          <template v-slot:tag v-if="(props.conversation.admins as number[]).includes(contact.id)">
            <div class="ml-3">
              <Typography variant="body-4" noColor class="text-indigo-400">admin</Typography>
            </div>
          </template>

          <template v-slot:menu
            v-if="currentUser && (props.conversation.admins as number[]).includes(currentUser.id) && contact.id !== currentUser.id">
            <div>
              <!--dropdown menu button-->
              <IconButton title="menu" @click="(event) => handleToggleDropdown(event, index)" class="open-menu w-6 h-6">
                <EllipsisVerticalIcon class="open-menu h-5 w-5 text-black opacity-60 dark:text-white" tabindex="0" />
              </IconButton>

              <!--dropdown menu-->
              <Dropdown :close-dropdown="closeDropdowns" :handle-click-outside="handleClickOutside"
                :show="(dropdownMenuStates as boolean[])[index]" :position="dropdownMenuPosition">
                <DropdownLink @click="handlePromoteToAdmin(contact)"> Promote to admin </DropdownLink>

                <DropdownLink @click="handleDemoteToMember(contact)"> Demote to member </DropdownLink>

                <DropdownLink @click="removeMember(contact)" color="danger"> Remove contact </DropdownLink>
              </Dropdown>
            </div>
          </template>
        </ContactItem>
      </ScrollBox>
    </div>
  </div>
</template>
