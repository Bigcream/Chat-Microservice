import router from "@src/router";
import "@src/style.css";
import { createPinia } from "pinia";
import { createApp, getCurrentInstance } from "vue";
import vClickOutside from "click-outside-vue3";
import { useCookies } from "vue3-cookies";
import App from "@src/App.vue";
const { cookies } = useCookies();
const pinia = createPinia();
const app = createApp(App);
app.use(pinia).use(router).use(vClickOutside).mount("#app");
app.config.globalProperties.$cookies = cookies
