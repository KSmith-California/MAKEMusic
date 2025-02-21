<template>
  <div class="dj-page">
    <header class="dj-header">
      <h1 class="dj-title">DJ Dashboard</h1>
      <button class="back-btn" @click="goToLanding">Back to Landing</button>
    </header>

    <!-- Turntable Section with Placeholder Song Data -->
    <section class="turntable-section">
      <h2>Now Playing</h2>
      <div class="turntable">
        <div class="record-container">
          <div class="record-box">
            <p>Current Song Image</p>
          </div>
          <p class="song-info">
            {{ currentSong.title }}<br />by {{ currentSong.artist }}
          </p>
        </div>
        <div class="record-container">
          <div class="record-box">
            <p>Next Song Image</p>
          </div>
          <p class="song-info">
            {{ nextSong.title }}<br />by {{ nextSong.artist }}
          </p>
        </div>
      </div>
    </section>

    <!-- Events Section with Create Event Form -->
    <section class="events-section">
      <button class="toggle-btn" @click="toggleCreateForm">
        {{ showCreateForm ? 'Hide Create Event Form' : 'Create New Event' }}
      </button>
      <div v-if="showCreateForm" class="create-event-wrapper">
        <CreateEventView @event-created="fetchMyEvents" />
      </div>
      <h2>My Events</h2>
      <div v-if="events.length === 0">
        <p>No events created yet.</p>
      </div>
      <ul v-else class="event-list">
        <li v-for="event in events" :key="event.eventID" class="event-item">
          <span class="event-name">{{ event.name }}</span>
          <span class="event-date">{{ formatDate(event.eventDate) }}</span>
          <span class="event-time">
            {{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}
          </span>
        </li>
      </ul>
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
      // Placeholder song data; replace with real data later
      currentSong: {
        title: 'Placeholder Song',
        artist: 'Placeholder Artist'
      },
      nextSong: {
        title: 'Next Placeholder Song',
        artist: 'Next Placeholder Artist'
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
    async fetchMyEvents() {
      try {
        const djId = this.$store.state.user.id;
        const response = await EventService.getEvents('', '');
        // Filter events created by the current DJ
        this.events = response.data.filter(event => event.createdBy === djId);
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    },
    formatDate(dateStr) {
      // Create a Date object from the string (assumed ISO format: YYYY-MM-DD)
      const dateObj = new Date(dateStr);
      const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
      return `${monthNames[dateObj.getMonth()]} ${dateObj.getDate()}, ${dateObj.getFullYear()}`;
    },
    formatTime(timeStr) {
      // Split the time string and convert to a 12-hour format
      const parts = timeStr.split(':');
      let hour = parseInt(parts[0]);
      const minute = parts[1];
      const suffix = hour >= 12 ? 'PM' : 'AM';
      hour = hour % 12;
      if (hour === 0) hour = 12;
      return `${hour}:${minute} ${suffix}`;
    }
  },
  mounted() {
    this.fetchMyEvents();
  }
};
</script>

<style scoped>
/* Overall DJ Page Styling */
.dj-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #7f00ff, #e100ff);
  color: #fff;
  font-family: 'Courier New', Courier, monospace;
  padding: 2rem;
  display: flex;
  flex-direction: column;
}

/* Header */
.dj-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
.dj-title {
  font-size: 3rem;
  text-shadow: 2px 2px 4px #000;
}
.back-btn {
  background: transparent;
  border: 2px solid #fff;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}
.back-btn:hover {
  background: #fff;
  color: #7f00ff;
}

/* Turntable Section */
.turntable-section {
  background-color: rgba(0, 0, 0, 0.3);
  margin: 0 2rem 2rem;
  padding: 2rem;
  border-radius: 8px;
  text-align: center;
}
.turntable-section h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px #000;
}
.turntable {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}
.record-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.record-box {
  width: 150px;
  height: 150px;
  background-color: rgba(255, 255, 255, 0.2);
  border: 2px dashed #fff;
  border-radius: 50%;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
.song-info {
  font-size: 1rem;
  text-shadow: 1px 1px 2px #000;
}

/* Events Section */
.events-section {
  background-color: rgba(0, 0, 0, 0.5);
  margin: 0 2rem 2rem;
  padding: 2rem;
  border-radius: 8px;
  text-align: center;
}
.toggle-btn {
  background-color: #fff;
  border: none;
  color: #7f00ff;
  padding: 0.5rem 1rem;
  font-size: 1.1rem;
  margin-bottom: 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}
.toggle-btn:hover {
  background-color: #7f00ff;
  color: #fff;
}
.create-event-wrapper {
  margin-bottom: 2rem;
}
.event-list {
  list-style: none;
  padding: 0;
  max-width: 600px;
  margin: 0 auto;
  text-align: left;
}
.event-item {
  border-bottom: 1px solid rgba(255,255,255,0.3);
  padding: 0.75rem 0;
  display: flex;
  justify-content: space-between;
  font-size: 1.2rem;
}
.event-name {
  font-weight: bold;
}
.event-date,
.event-time {
  font-style: italic;
}
</style>
