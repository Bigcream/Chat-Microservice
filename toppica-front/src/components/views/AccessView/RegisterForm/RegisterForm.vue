<script setup lang="ts">
import type { Ref } from "vue";
import { computed, reactive, ref, provide } from "vue";
import { RouterLink } from "vue-router";

import SlideTransition from "@src/components/ui/transitions/SlideTransition.vue";
import Typography from "@src/components/ui/data-display/Typography.vue";
import PasswordSection from "@src/components/views/AccessView/RegisterForm/PasswordSection.vue";
import PersonalSection from "@src/components/views/AccessView/RegisterForm/PersonalSection.vue";

defineEmits(["activeSectionChange"]);

// determines what form section to use.
const activeSectionName = ref("personal-section");

// determines what direction the slide animation should use.
const animation = ref("slide-left");

// Acount info
const account: Ref<string> = ref("");
const firstName: Ref<string> = ref("");
const lastName: Ref<string> = ref("");
const pwd: Ref<string> = ref("");
const rePwd: Ref<string> = ref("");

  // provide("account", account.value);
provide('account', {
  account,
  setAccount: (val: string) => account.value = val
});
provide('firstName', {
  firstName,
  setFirstName: (val: string) => firstName.value = val
});
provide('lastName', {
  lastName,
  setLastName: (val: string) => lastName.value = val
});
provide('pwd', {
  pwd,
  setLastName: (val: string) => pwd.value = val
});
provide('rePwdd', {
  rePwd,
  setLastName: (val: string) => rePwd.value = val
});

// get the active section component from the section name
const PERSONAL_SECTION = "personal-section";
const PASSWORD_SECTION = "password-section";

const ActiveSection = computed((): any => {
  if (activeSectionName.value === PERSONAL_SECTION) {
    return PersonalSection;
  } else if (activeSectionName.value === PASSWORD_SECTION) {
    return PasswordSection;
  }
});

// (event) to move between modal pages
const changeActiveSection = (event: {
  sectionName: string;
  animationName: string;
}) => {
  animation.value = event.animationName;
  activeSectionName.value = event.sectionName;
};
</script>

<template>
  <div
    class="p-5 md:basis-1/2 xs:basis-full flex flex-col justify-center items-center"
  >
    <div class="w-full md:px-[26%] xs:px-[10%]">
      <!--header-->
      <div class="mb-6 flex flex-col">
        <img
          src="@src/assets/vectors/logo-gradient.svg"
          class="w-[1.375rem] h-[1.125rem] mb-5 opacity-70"
        />
        <Typography variant="heading-2" class="mb-4"
          >Get started with Avian</Typography
        >
        <Typography variant="body-3" class="text-opacity-75 font-light">
          Sign in to start using messaging!
        </Typography>
      </div>

      <!--form section-->
      <SlideTransition :animation="animation">
        <component
          @active-section-change="changeActiveSection"
          :is="ActiveSection"
        />
      </SlideTransition>

      <!--bottom text-->
      <div class="flex justify-center">
        <Typography variant="body-2"
          >Already have an account ?
          <RouterLink to="/access/sign-in/" class="text-indigo-400 opacity-100">
            Sign in
          </RouterLink>
        </Typography>
      </div>
    </div>
  </div>
</template>
