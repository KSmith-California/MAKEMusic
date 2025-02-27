import axios from 'axios';
import { createApp } from 'vue';
import CapstoneApp from './App.vue';
import router from './router';
import { createStore } from './store';

/* Sets the base URL for server API communication */
axios.defaults.baseURL = import.meta.env.VITE_REMOTE_API;
console.log("🌍 VITE_REMOTE_API:", axios.defaults.baseURL);

/* Sets authentication token */
let currentToken = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

const store = createStore(currentToken, currentUser);
const app = createApp(CapstoneApp);
app.use(store);
app.use(router);
app.mount('#app');
