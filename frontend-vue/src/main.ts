import { createApp } from "vue";
import App from "./App.vue";
import store from "./store/userStore"; // Import the Vuex store
import router from "./router"; // Import the Vue Router instance
import "./main.css"; // Import global styles

const app = createApp(App);

app.use(store); // Use the Vuex store
app.use(router); // Use the Vue Router
app.mount("#app"); // Mount the Vue app
