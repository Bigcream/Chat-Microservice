<script setup lang="ts">
import useStore from "@src/store/store";
import { useAuthUserStore } from '@src/stores/useAuthUserStore'
import {
  ArrowLeftOnRectangleIcon,
  ArrowPathIcon,
  InformationCircleIcon,
} from "@heroicons/vue/24/outline";
import Dropdown from "@src/components/ui/navigation/Dropdown/Dropdown.vue";
import DropdownLink from "@src/components/ui/navigation/Dropdown/DropdownLink.vue";
import { RouterLink } from "vue-router";

const { logout } = useAuthUserStore()
const props = defineProps<{
  showDropdown: boolean;
  handleCloseDropdown: () => void;
  handleShowDropdown: () => void;
  id: string;
}>();

const store = useStore();

// (event) close dropdown menu when clicking outside
const handleCloseOnClickOutside = (event: Event) => {
  if (
    !["user-avatar", "profile-menu-button"].includes(
      (event.target as HTMLButtonElement).id
    )
  ) {
    props.handleCloseDropdown();
  }
};

const submit = () => {
  logout()
};
</script>

<template>
  <div class="relative">
    <!--toggle dropdown button-->
    <button
      :id="props.id + '-button'"
      @click="handleShowDropdown"
      class="bg-white rounded-full active:scale-110 focus:outline-none focus:scale-110 transition duration-200 ease-out"
      :style="{
        'box-shadow': !store.settings.darkMode
          ? '0 .125rem .3125rem rgba(193, 202, 255, 0.5),.125rem 0 .3125rem rgba(193, 202, 255, 0.5),-0.125rem 0 .3125rem rgba(193, 202, 255, 0.5),0 -0.125rem .3125rem rgba(193, 202, 255, 0.5)'
          : '0 .125rem .3125rem rgba(0, 70, 128, 0.5),.125rem 0 .3125rem rgba(0, 70, 128, 0.5),-0.125rem 0 .3125rem rgba(0, 70, 128, 0.5),0 -0.125rem .3125rem rgba(0, 70, 128, 0.5)',
      }"
      :aria-expanded="showDropdown"
      aria-controls="profile-menu"
      aria-label="toggle profile menu"
    >
      <div
        id="user-avatar"
        :style="{ backgroundImage: `url(${store.user?.avatar})` }"
        class="w-7 h-7 rounded-full bg-cover bg-center"
      ></div>
    </button>

    <!--dropdown menu-->
    <Dropdown
      :id="props.id + '-dropdown'"
      :aria-labelledby="props.id + '-button'"
      :show="props.showDropdown"
      :position="[
        'md:bottom-0',
        'md:left-[2.5rem]',
        'md:top-[auto]',
        'bottom-[3.125rem]',
        'left-[-4.8125rem]',
      ]"
      :handle-click-outside="handleCloseOnClickOutside"
      :close-dropdown="props.handleCloseDropdown"
    >
      <DropdownLink
        label="Profile Information"
        :handle-click="props.handleCloseDropdown"
        tabindex="0"
      >
        <InformationCircleIcon
          class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
        />
        Profile Information
      </DropdownLink>

      <DropdownLink
        label="Password Change"
        :handle-click="props.handleCloseDropdown"
      >
        <RouterLink to="/reset/" class="w-full flex items-center justify-start">
          <ArrowPathIcon
            class="h-5 w-5 mr-3 text-black opacity-60 dark:text-white dark:opacity-70"
          />
          Password Change
        </RouterLink>
      </DropdownLink>

      <DropdownLink
        label="Logout"
        :handle-click="props.handleCloseDropdown"
        color="danger"
      >
        <RouterLink
          to="/access/sign-in/"
          class="w-full flex items-center justify-start"
          @click.native="submit"
        >
          <ArrowLeftOnRectangleIcon class="h-5 w-5 mr-3" />
          Logout
        </RouterLink>
      </DropdownLink>
    </Dropdown>
  </div>
</template>
