<template>
  <div>
    <header class="header">
      <img src="/landingGif.gif" alt="Header GIF" class="header-gif" />
      <div class="filter-container">
        <h2 class="filter-title">Find Your Event</h2>
        <div class="filter-row">
          <input type="text" v-model="nameFilter" placeholder="Find your event" class="filter-input large-input" />
          <input type="date" v-model="dateFilter" placeholder="Filter by date" class="filter-input large-input" />
        </div>
        <div class="filter-row">
          <select v-model="djFilter" class="filter-input large-input">
            <option value="">Find your DJ</option>
            <option v-for="dj in uniqueDJs" :key="dj" :value="dj">{{ dj }}</option>
          </select>
          <label class="checkbox-label inline-checkbox">
            <input type="checkbox" v-model="filterMyEvents" />
            My events only
          </label>
        </div>
      </div>
    </header>

    <div class="landing-page">
      <div class="container">
        <!-- 🔍 DEBUG: Show raw event data -->
        <pre class="debug-box">EVENT DATA: {{ events }}</pre>

        <div class="large-boxes-container">
          <div v-for="(event, index) in filteredEvents" :key="index" class="large-box">
            <p>{{ event.name }}</p>
            <p>DATE: {{ formatDate(event.eventDate) }}</p>
            <p>TIME: {{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}</p>
          </div>
        </div>

        <div class="playlist-section">
          <h2 class="playlist-title">{{ playlistName || "Featured Playlists" }}</h2>
          <div v-if="playlists.length" class="playlist-container">
            <div v-for="(track, index) in playlists" :key="index" class="playlist-card">
              <img :src="track.images[0]?.url" alt="Album Cover" class="album-cover" />
              <div class="track-info">
                <h3 class="track-name">{{ track.name }}</h3>
                <p class="track-artist"><strong>Artist:</strong> {{ track.artists }}</p>
                <p class="track-genre"><strong>Genre:</strong> {{ track.genre || "Unknown" }}</p>
              </div>
              <iframe :src="`https://open.spotify.com/embed/track/${track.id}`" class="spotify-player" frameborder="0" allow="encrypted-media"></iframe>
            </div>
          </div>
          <p v-if="playlists.length === 0" class="loading-text">Loading playlists...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EventService from '../services/EventService';

export default {
  name: 'LandingPageView',
  data() {
    return {
      events: [],
      nameFilter: '',
      dateFilter: '',
      djFilter: '',
      filterMyEvents: false,
      userRole: 'DJ',
      userId: 4,
      djUsernames: { 1: 'DJ Cool', 4: 'DJ Funky', 7: 'DJ Smooth' },
      playlists: [],
      token: ''
    };
  },
  computed: {
    isDJOrHost() {
      return this.userRole === 'DJ' || this.userRole === 'HOST';
    },
    uniqueDJs() {
      return [...new Set(this.events.map(event => this.getDJUsername(event.createdBy)))].filter(dj => dj);
    },
    filteredEvents() {
      return this.events.filter(event => {
        const matchesName = event.name.toLowerCase().includes(this.nameFilter.toLowerCase());
        const matchesDate = this.dateFilter ? event.eventDate === this.dateFilter : true;
        const matchesDJ = this.djFilter ? this.getDJUsername(event.createdBy) === this.djFilter : true;
        const matchesMyEvents = !this.filterMyEvents || event.createdBy === this.userId || (event.hosts && event.hosts.includes(this.userId));
        return matchesName && matchesDate && matchesDJ && matchesMyEvents;
      });
    }
  },
  methods: {
    retrieveEvents() {
      console.log("🚀 Calling retrieveEvents()...");

      EventService.getEvents()
        .then(response => {
          console.log("✅ Events received:", response.data);
          this.events = response.data;
        })
        .catch(error => {
          console.error("❌ Error fetching events:", error);
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
    },
  },
  created() {
    this.retrieveEvents();
  }
};
</script>

<style scoped>
/* STYLES */
.debug-box {
  background: rgba(0, 0, 0, 0.1);
  padding: 10px;
  font-size: 14px;
  white-space: pre-wrap;
  color: red;
}
</style>
