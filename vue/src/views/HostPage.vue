<template>
  <div class="host-page">
    <header class="host-header">
      <h1>Host Dashboard</h1>
    </header>
    <section class="event-list">
      <h2>My Assigned Events</h2>
      <div v-if="events.length === 0">
        <p>No events assigned to you.</p>
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

export default {
  name: 'HostPage',
  data() {
    return {
      events: []
    };
  },
  methods: {
    async fetchMyEvents() {
      try {
        const hostId = this.$store.state.user.id;
        const response = await EventService.getEvents();
        // Filter events that contain the host's user id in a hypothetical "hosts" array
        // Adjust based on how your backend returns host assignments
        this.events = response.data.filter(event => {
          return event.hosts && event.hosts.includes(hostId);
        });
      } catch (error) {
        console.error('Error fetching events for host:', error);
      }
    }
  },
  mounted() {
    this.fetchMyEvents();
  }
};
</script>

<style scoped>
.host-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #00d2ff, #3a47d5);
  color: #fff;
  font-family: 'Courier New', Courier, monospace;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.host-header h1 {
  font-size: 3rem;
  text-shadow: 2px 2px #000;
  margin-bottom: 1rem;
  text-align: center;
}

.event-list {
  background-color: rgba(0, 0, 0, 0.4);
  padding: 1rem;
  border-radius: 8px;
  width: 100%;
  max-width: 700px;
  margin: 0 auto;
  text-align: center;
}

.event-list h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.event-list ul {
  list-style: none;
  padding: 0;
}

.event-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255,255,255,0.2);
  padding: 0.5rem 0;
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
