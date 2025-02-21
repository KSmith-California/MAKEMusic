<template>
  <div class="host-page">
    <!-- Header -->
    <header class="host-header">
      <h1>Host Dashboard</h1>
      <button class="back-btn" @click="goToLanding">Back to Landing</button>
    </header>

    <!-- Event List Section -->
    <section class="event-list-section">
      <h2>My Assigned Events</h2>
      
      <!-- Show events if available -->
      <ul v-if="events.length > 0" class="event-list">
        <li v-for="event in events" :key="event.eventID" class="event-item">
          <span class="event-name">{{ event.name }}</span>
          <span class="event-date">{{ formatDate(event.eventDate) }}</span>
          <span class="event-time">{{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}</span>
        </li>
      </ul>
      
      <!-- Show message if no events found -->
      <p v-else class="no-events">No events assigned to you.</p>
    </section>
  </div>
</template>

<script>
export default {
  name: 'HostPage',
  data() {
    return {
      events: []
    };
  },
  methods: {
    // Navigates back to the landing page
    goToLanding() {
      this.$router.push({ name: 'landing' });
    },

    // Fetch all events and filter by host ID
    async fetchMyEvents() {
      try {
        const hostId = this.$store.state.user.id;
        const response = await this.$axios.get(`${import.meta.env.VITE_REMOTE_API}/events`);

        // ✅ Filter events where `event.hosts` includes the logged-in host's ID
        this.events = response.data.filter(event => event.hosts && event.hosts.includes(hostId));

        // Debugging: Log the filtered events
        console.log("Filtered Events for Host:", this.events);
      } catch (error) {
        console.error('Error fetching events for host:', error);
      }
    },

    // Format date (e.g., "2025-06-15" → "Jun 15, 2025")
    formatDate(dateStr) {
      const dateObj = new Date(dateStr);
      const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
      return `${monthNames[dateObj.getMonth()]} ${dateObj.getDate()}, ${dateObj.getFullYear()}`;
    },

    // Format time (e.g., "18:00:00" → "6:00 PM")
    formatTime(timeStr) {
      if (!timeStr) return "";
      const [hour, minute] = timeStr.split(':').map(Number);
      const suffix = hour >= 12 ? 'PM' : 'AM';
      const formattedHour = hour % 12 || 12;
      return `${formattedHour}:${minute.toString().padStart(2, '0')} ${suffix}`;
    }
  },

  // Fetch host events when the component loads
  mounted() {
    this.fetchMyEvents();
  }
};
</script>

<style scoped>
/* 🌟 Host Dashboard Styling */
.host-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #00d2ff, #3a47d5); /* Smooth blue gradient */
  color: #fff;
  font-family: 'Courier New', Courier, monospace;
  padding: 2rem;
  text-align: center;
}

/* 🌟 Header */
.host-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
.host-header h1 {
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
  transition: background 0.3s ease;
}
.back-btn:hover {
  background: #fff;
  color: #3a47d5;
}

/* 🌟 Event List Section */
.event-list-section {
  background-color: rgba(0, 0, 0, 0.5);
  padding: 2rem;
  border-radius: 8px;
  max-width: 800px;
  margin: 0 auto;
}

/* 🌟 Event List */
.event-list {
  list-style: none;
  padding: 0;
  text-align: left;
  margin: 0 auto;
  max-width: 600px;
}
.event-item {
  background: rgba(255, 255, 255, 0.2);
  padding: 1rem;
  border-radius: 6px;
  margin-bottom: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.1rem;
}
.event-name {
  font-weight: bold;
}
.event-date,
.event-time {
  font-style: italic;
}

/* 🌟 No Events Message */
.no-events {
  font-size: 1.2rem;
  font-style: italic;
  color: #e0e0e0;
  margin-top: 1rem;
}
</style>
