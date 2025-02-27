import axios from 'axios';

console.log("VITE_REMOTE_API:", import.meta.env.VITE_REMOTE_API);

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API || "http://localhost:9000"
});

export default {
  login(user) {
    return http.post('/login', user);
  },
};
