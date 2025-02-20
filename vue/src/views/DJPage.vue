<template>
  <div class="dj-page">
    <header class="dj-header">
      <h1>DJ Dashboard</h1>
      <button class="back-btn" @click="goToLanding">Back to Landing</button>
    </header>
    
    <!-- Turntable Section with Placeholder Song Data -->
    <section class="turntable-section">
      <div class="turntable">
        <div class="record-container">
          <img src="@/assets/record_current.png" alt="Current Song Record" class="record" />
          <p class="song-info">
            Now Playing: {{ currentSong.title }} - {{ currentSong.artist }}
          </p>
        </div>
        <div class="record-container">
          <img src="@/assets/record_next.png" alt="Next Song Record" class="record" />
          <p class="song-info">
            Next Up: {{ nextSong.title }} - {{ nextSong.artist }}
          </p>
        </div>
      </div>
    </section>
    
    <!-- Create Event Form Toggle -->
    <button class="toggle-button" @click="toggleCreateForm">
      {{ showCreateForm ? 'Hide Create Event Form' : 'Create New Event' }}
    </button>
    
    <!-- Create Event Form Component -->
    <div v-if="showCreateForm" class="create-event-wrapper">
      <CreateEventView @event-created="fetchMyEvents" />
    </div>
    
    <!-- Event List Section -->
    <section class="event-list-section">
      <h2>My Events</h2>
      <div v-if="events.length === 0">
        <p>No events created yet.</p>
      </div>
      <ul v-else>
        <li v-for="event in events" :key="event.eventID" class="event-item">
          <span class="event-name">{{ event.name }}</span>
          <span class="event-date">{{ event.eventDate }}</span>
          <span class="event-time">{{ event.startTime }} - {{ event.endTime }}</span>
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
      // Placeholder song data until the song queue is implemented
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
    }
  },
  mounted() {
    this.fetchMyEvents();
  }
};
</script>

<style scoped>
.dj-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #ff0099, #493240);
  color: #fff;
  font-family: 'Courier New', Courier, monospace;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Header */
.dj-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 800px;
  margin-bottom: 2rem;
}
.dj-header h1 {
  font-size: 3rem;
  text-shadow: 2px 2px #000;
}
.back-btn {
  background: transparent;
  border: 2px solid #fff;
  color: #fff;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  cursor: pointer;
  border-radius: 4px;
  transition: background 0.3s ease;
}
.back-btn:hover {
  background: #fff;
  color: #493240;
}

/* Turntable Section */
.turntable-section {
  margin-bottom: 2rem;
  text-align: center;
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
.record {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 5px solid #fff;
  margin-bottom: 0.5rem;
  animation: spin 5s linear infinite;
}
.song-info {
  font-size: 1rem;
  text-shadow: 1px 1px #000;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Toggle Button */
.toggle-button {
  background-color: rgba(255, 255, 255, 0.1);
  border: 2px solid #fff;
  color: #fff;
  font-size: 1.2rem;
  padding: 0.5rem 1rem;
  margin-bottom: 2rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.toggle-button:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

/* Create Event Wrapper */
.create-event-wrapper {
  width: 100%;
  max-width: 700px;
  margin-bottom: 2rem;
}

/* Event List Section */
.event-list-section {
  background-color: rgba(0, 0, 0, 0.4);
  padding: 1rem;
  border-radius: 8px;
  width: 100%;
  max-width: 700px;
  margin: 0 auto;
  text-align: center;
}
.event-list-section h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}
.event-list-section ul {
  list-style: none;
  padding: 0;
}
.event-item {
  padding: 0.5rem;
  border-bottom: 1px solid rgba(255,255,255,0.3);
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
