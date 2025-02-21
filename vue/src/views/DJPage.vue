<template>
  <div class="dj-page">
    <!-- HEADER with Back Button -->
    <header class="dj-header">
      <h1 class="dj-title">DJ Dashboard</h1>
      <button class="back-btn" @click="goToLanding">Back to Landing</button>
    </header>

    <!-- CHANGE: Removed the entire turntable section and replaced with two images -->
    <section class="dj-images-section">
      <img src="MAKEDJ2.png" alt="DJ Image 1" class="dj-image" />
      <img src="MAKEDJ3.png" alt="DJ Image 2" class="dj-image" />
    </section>

    <!-- CREATE EVENT FORM TOGGLE -->
    <section class="create-event-section">
      <button class="toggle-btn" @click="toggleCreateForm">
        {{ showCreateForm ? 'Hide Create Event Form' : 'Create New Event' }}
      </button>
      <div v-if="showCreateForm" class="create-event-wrapper">
        <CreateEventView @event-created="fetchMyEvents" />
      </div>
    </section>

    <!-- EVENTS LIST -->
    <section class="events-section">
      <h2 class="section-title">My Events</h2>
      <div class="large-boxes-container">
        <div v-for="(event, index) in events" :key="index" class="large-box">
          <p>{{ event.name }}</p>
          <p>DATE: {{ formatDate(event.eventDate) }}</p>
          <p>TIME: {{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}</p>
          <p v-if="event.createdBy">DJ: {{ getDJUsername(event.createdBy) }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import EventService from '@/services/EventService';
import CreateEventView from '@/views/CreateEventView.vue';

export default {
  name: 'DJPage',
  components: { CreateEventView },
  data() {
    return {
      showCreateForm: false,
      events: [],
      userId: 4,    // For testing, set dynamically in production
      userRole: 'DJ',
      djUsernames: {
        1: 'DJ Cool',
        4: 'DJ Funky',
        7: 'DJ Smooth'
      }
    };
  },
  methods: {
    goToLanding() {
      this.$router.push({ name: 'landing' });
    },
    toggleCreateForm() {
      this.showCreateForm = !this.showCreateForm;
    },
    fetchMyEvents() {
      EventService.getEvents().then(response => {
        // Shows only events created by this DJ
        this.events = response.data.filter(event => event.createdBy === this.userId);
      });
    },
    getDJUsername(djId) {
      return this.djUsernames[djId] || `DJ ${djId}`;
    },
    formatDate(dateStr) {
      const dateObj = new Date(dateStr);
      const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
      return `${monthNames[dateObj.getMonth()]} ${dateObj.getDate()}, ${dateObj.getFullYear()}`;
    },
    formatTime(timeStr) {
      if (!timeStr) return "";
      const [hour, minute] = timeStr.split(':').map(Number);
      const suffix = hour >= 12 ? 'PM' : 'AM';
      const formattedHour = hour % 12 || 12;
      return `${formattedHour}:${minute.toString().padStart(2, '0')} ${suffix}`;
    }
  },
  created() {
    this.fetchMyEvents();
  }
};
</script>

<style scoped>
.dj-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #7f00ff, #e100ff);
  color: #fff;
  font-family: 'Courier New', Courier, monospace;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

/* HEADER */
.dj-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.dj-title {
  font-size: 3rem;
  text-shadow: 2px 2px 4px #000;
}
.back-btn {
  background: transparent;
  border: 2px solid #fff;
  color: #fff;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}
.back-btn:hover {
  background: #fff;
  color: #7f00ff;
}

/* CHANGE: DJ Images Section */
.dj-images-section {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 30px;
}
.dj-image {
  width: 500px; /* Adjust as needed for your layout */
  height: auto;
  border-radius: 8px;
  border: 2px solid #fff;
}

/* CREATE EVENT SECTION */
.create-event-section {
  text-align: center;
  margin-bottom: 30px;
}
.toggle-btn {
  background-color: #fff;
  color: #7f00ff;
  padding: 10px 20px;
  font-size: 1.1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
  margin-bottom: 15px;
}
.toggle-btn:hover {
  background-color: #7f00ff;
  color: #fff;
}
.create-event-wrapper {
  max-width: 700px;
  margin: 0 auto 30px auto;
}

/* EVENTS SECTION */
.events-section {
  text-align: center;
  margin-bottom: 30px;
}
.section-title {
  font-size: 2rem;
  margin-bottom: 15px;
  text-shadow: 2px 2px 4px #000;
}
.large-boxes-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}
.large-box {
  background-color: black;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-size: 18px;
  padding: 20px;
  height: 400px;
  width: calc(40% - 10px);
  box-sizing: border-box;
  border-radius: 12px;
  border: 2px solid white;
}
.large-box p {
  margin: 5px 0;
  font-size: 16px;
}
.large-box p:first-child {
  font-size: 20px;
  font-weight: bold;
}
</style>
