<template>
  <div>
    <header class="header">
      <img src="/landingGif.gif" alt="Header GIF" class="header-gif" />
      <!-- CHANGE: Filtering section now overlaps the hero GIF -->
      <div class="filter-container">
        <h2 class="filter-title">Find Your Event</h2>
        <div class="filter-row">
          <!-- "Find your event" input -->
          <input
            type="text"
            v-model="nameFilter"
            placeholder="Find your event"
            class="filter-input large-input"
          />
          <!-- Date filter remains unchanged -->
          <input
            type="date"
            v-model="dateFilter"
            placeholder="Filter by date"
            class="filter-input large-input"
          />
        </div>
        <div class="filter-row">
          <!-- CHANGE: "Find your DJ" dropdown showing DJ names -->
          <select v-model="djFilter" class="filter-input large-input">
            <option value="">Find your DJ</option>
            <option v-for="dj in uniqueDJs" :key="dj" :value="dj">{{ dj }}</option>
          </select>
          <!-- CHANGE: Inline "My events only" checkbox -->
          <label class="checkbox-label inline-checkbox">
            <input type="checkbox" v-model="filterMyEvents" />
            My events only
          </label>
        </div>
      </div>
    </header>

    <div class="landing-page">
      <div class="container">
        <!-- CHANGE: Upcoming events are restored to original layout -->
        <div class="large-boxes-container">
          <div v-for="(event, index) in filteredEvents" :key="index" class="large-box">
            <p>{{ event.name }}</p>
            <p>DATE: {{ formatDate(event.eventDate) }}</p>
            <p>TIME: {{ formatTime(event.startTime) }} - {{ formatTime(event.endTime) }}</p>
            <!-- Removed extra DJ line so it matches original layout -->
          </div>
        </div>

        <!-- Spotify Playlist Section -->
        <div class="playlist-section">
          <h2 class="playlist-title"> {{ playlistName || "Featured Playlists" }} </h2>


          <ul v-if="playlists.length">
            <li v-for="(track, index) in playlists" :key="index">
              <img :src="track.images[0]?.url" alt="Album Cover" width="50" />
              <strong>{{ track.name }}</strong> - {{ track.artists }}
              <p><strong>Genre:</strong> {{ track.genre }}</p>

      <!-- Spotify Embed Player -->
              <iframe
                  :src="`https://open.spotify.com/embed/track/${track.id}`"
                  width="300"
                  height="80"
                  frameborder="0"
                  allow="encrypted-media"
              ></iframe>
            </li>
          </ul>


          <p v-if="playlists.length === 0" class="loading-text">Loading playlists...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EventService from '../services/EventService';
// import { getPlaylists } from "@/services/Spotify"; // Ensure this function exists in spotify.js
import SpotifyService from '../services/SpotifyService';


export default {
  name: 'LandingPageView',
  data() {
    return {
      events: [],
      nameFilter: '',
      dateFilter: '',
      djFilter: '', // Filters by DJ username
      filterMyEvents: false, // "My events only" checkbox
      userRole: 'DJ', // For testing; set dynamically in production
      userId: 4,      // For testing; set dynamically in production
      // Mapping of DJ IDs to usernames
      djUsernames: {
        1: 'DJ Cool',
        4: 'DJ Funky',
        7: 'DJ Smooth'
      },
      playlists: [], // Store Spotify playlists
      token: ''
    };
  },
  computed: {
    isDJOrHost() {
      return this.userRole === 'DJ' || this.userRole === 'HOST';
    },
    // Return unique DJ names for the dropdown using the mapping
    uniqueDJs() {
      return [...new Set(this.events.map(event => this.getDJUsername(event.createdBy)))].filter(dj => dj);
    },
    filteredEvents() {
      return this.events.filter(event => {
        const matchesName = event.name.toLowerCase().includes(this.nameFilter.toLowerCase());
        const matchesDate = this.dateFilter ? event.eventDate === this.dateFilter : true;
        const matchesDJ = this.djFilter ? this.getDJUsername(event.createdBy) === this.djFilter : true;
        // When "My events only" is checked, show events where the user is the creator or is assigned as a host
        const matchesMyEvents = !this.filterMyEvents ||
          (event.createdBy === this.userId) ||
          (event.hosts && event.hosts.includes(this.userId));
        return matchesName && matchesDate && matchesDJ && matchesMyEvents;
      });
    }
  },
  methods: {
    retrieveEvents() {
      EventService.getEvents().then(response => {
        this.events = response.data;
      });
    },
    // Maps a DJ ID to a DJ username using djUsernames mapping
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
    async fetchPlaylists() {
      this.playlists = [];
    },
    async fetchPlaylists() {
      try {
        // Step 1: Get token
        const tokenResponse = await SpotifyService.getToken();
        const token = tokenResponse.data.access_token;

        // Step 2: Fetch playlist using the token
        const playlistResponse = await SpotifyService.getPlaylist(token);
        console.log("Fetched Playlist Data:", playlistResponse.data); // Debugging

        // Step 3: Extract playlist name
        this.playlistName = playlistResponse.data.name;

        // Step 4: Fetch genres for each track’s artist
        this.playlists = await Promise.all(playlistResponse.data.tracks.items.map(async (track) => {
            const artistId = track.track.artists[0].id;

            // Fetch artist details to get the genre
            let genre = "Unknown";
            try {
                const artistResponse = await SpotifyService.getArtistGenres(token, artistId);
                genre = artistResponse.data.genres.length ? artistResponse.data.genres.join(", ") : "Unknown";
            } catch (err) {
                console.error(`Error fetching genre for artist ${artistId}:`, err);
            }

            return {
                id: track.track.id,
                name: track.track.name,
                images: track.track.album.images,
                artists: track.track.artists.map(artist => artist.name).join(", "),
                genre: genre  // Add genre to each track
            };
        }));

    } catch (error) {
        console.error("Error fetching Spotify playlists:", error);
    }
  }
    
  },
  created() {
    this.retrieveEvents();
    this.fetchPlaylists(); // Fetch Spotify playlists when the component is created
    
    

  }
};
</script>

<style scoped>
/* HEADER */
.header {
  position: relative; /* Required for absolute positioning of filter-container */
  width: 100%;
  margin-bottom: 0px;
}
.header-gif {
  width: 100%;
  height: auto;
  object-fit: cover;
}

/* Landing Page Container (unchanged) */
.landing-page {
  background-color: rgb(141, 103, 175);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  min-height: 100vh;
  box-sizing: border-box;
  font-family: Arial, Helvetica, sans-serif;
}
.container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: flex-start;
  align-items: center;
  max-width: 1200px;
  width: 100%;
  flex-grow: 1;
}

/* UPDATED Filtering Section */
.filter-container {
  position: absolute;
  bottom: 50px; /* Overlap position */
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  background-color: rgba(0, 0, 0, 0.6); /* Semi-transparent dark background */
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 1000px;
}
.filter-title {
  width: 100%;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #fff;
}
.filter-row {
  display: flex;
  justify-content: center;
  gap: 15px;
  width: 100%;
}
.filter-input {
  padding: 12px;
  font-size: 16px;
  width: 250px;
  border-radius: 8px;
  border: 1px solid white;
}
.large-input {
  width: 300px;
}
.inline-checkbox {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  margin-left: 10px;
}

/* Event Boxes (unchanged) */
.large-boxes-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
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

/* Playlist Section */
.playlist-section {
  margin-top: 40px;
  width: 100%;
  text-align: center;
}
.playlist-title {
  color: white;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}
.playlist-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;
}
.playlist-card {
  background-color: white;
  padding: 10px;
  border-radius: 10px;
  text-align: center;
  width: 150px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
}
.playlist-image {
  width: 150px;
  height: 150px;
  border-radius: 10px;
  object-fit: cover;
}
.loading-text {
  color: white;
  font-size: 16px;
  margin-top: 10px;
}
</style>