import axios from 'axios';

// Use the VITE_REMOTE_API environment variable (which should be set to http://localhost:9000)
const API_URL = `${import.meta.env.VITE_REMOTE_API}/events`;

export default {
    getEvents(name = '', date = '') {
        return axios.get(API_URL, {
            params: { name, date }
        });
    },

    getEventById(eventId) {
        return axios.get(`${API_URL}/${eventId}`);
    },

    createEvent(eventData) {
        return axios.post(API_URL, eventData);
    }
}
