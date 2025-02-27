import axios from 'axios';

const http = axios.create({
    baseURL: import.meta.env.VITE_REMOTE_API
});

console.log("🔍 API Base URL:", http.defaults.baseURL);

export default {
    getEvents() {
        console.log("🔄 Fetching events from:", http.defaults.baseURL + "/events");
        return http.get('/events')
          .then(response => {
            console.log("✅ API Response:", response.data);
            return response;
          })
          .catch(error => {
            console.error("❌ API Request Failed:", error);
          });
    }
};
