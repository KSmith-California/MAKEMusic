<template>
  <div class="dj-page">
    <!-- HEADER with Back Button -->
    <header class="dj-header">
      <h1 class="dj-title">DJ Dashboard</h1>
      <button class="back-btn" @click="goToLanding">Back to Landing</button>
    </header>

    <!-- DJ Images Section -->
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

    <!-- CREATE PLAYLIST FORM TOGGLE -->
    <section class="create-playlist-section">
      <button class="toggle-btn" @click="toggleCreatePlaylistForm">
        {{ showCreatePlaylistForm ? 'Hide Create Playlist Form' : 'Create New Playlist' }}
      </button>
      <div v-if="showCreatePlaylistForm" class="create-playlist-wrapper">
        <!-- Playlist Name Input -->
        <input
          v-model="playlistNameInput"
          type="text"
          placeholder="Enter playlist name"
          class="playlist-name-input"
        />

        <select v-model="selectedGenre" class="genre-select">
  <option value="">All Genres</option>
  <option v-for="genre in availableGenres" :key="genre" :value="genre">
    {{ genre }}
  </option>
</select>

        <!-- Song Search Input -->
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search for a song..."
          @input="searchSongs"
          class="search-input"
        />
        
        <div v-if="searchResults.length">
          <div v-for="track in searchResults" :key="track.id" class="track-card">
            <!-- Checkbox for Song Selection -->
            <input
              type="checkbox"
              :value="track.id"
              v-model="selectedTracks"
              :id="track.id"
            />
            <label :for="track.id">
              <img :src="track.album.images[0]?.url" alt="Album Cover" class="album-cover" />
              <p>{{ track.name }} - {{ track.artists[0].name }}</p>
            </label>
          </div>
        </div>
        <button @click="createNewPlaylist" class="create-btn">Create Playlist</button>
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

    <!-- SPOTIFY PLAYLIST -->
    <div class="playlist-section">
      <h2 class="playlist-title">{{ playlistName }}</h2>
      <div class="playlist-container">
        <div v-for="(track, index) in playlists" :key="index" class="playlist-card">
          <!-- Album Cover -->
          <img :src="track.images[0]?.url" alt="Album Cover" class="album-cover" />

          <!-- Track Info -->
          <div class="track-info">
            <h3 class="track-name">{{ track.name }}</h3>
            <p class="track-artist"><strong>Artist:</strong> {{ track.artists }}</p>
            <p class="track-genre"><strong>Genre:</strong> {{ track.genre }}</p>
          </div>

          <!-- Embedded Player -->
          <iframe
            :src="`https://open.spotify.com/embed/track/${track.id}`"
            class="spotify-player"
            frameborder="0"
            allow="encrypted-media"
          ></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EventService from '@/services/EventService';
import CreateEventView from '@/views/CreateEventView.vue';
import SpotifyService from '../services/SpotifyService';

export default {
  name: 'DJPage',
  components: { CreateEventView },
  data() {
    return {
      showCreateForm: false,
      showCreatePlaylistForm: false,
      events: [],
      userId: 4, // For testing, set dynamically in production
      userRole: 'DJ',
      djUsernames: {
        1: 'DJ Cool',
        4: 'DJ Funky',
        7: 'DJ Smooth'
      },
      playlists: [],
      token: '',
      playlistName: '',
      playlistNameInput: '', // Track the playlist name input
      searchQuery: '',
      searchResults: [],
      selectedTracks: [], // Array of selected track IDs
      availableGenres: [],
      selectedGenre: ''
    };
  },
  methods: {
    goToLanding() {
      this.$router.push({ name: 'landing' });
    },
    toggleCreateForm() {
      this.showCreateForm = !this.showCreateForm;
    },
    toggleCreatePlaylistForm() {
      this.showCreatePlaylistForm = !this.showCreatePlaylistForm;
    },
    async fetchMyEvents() {
      try {
        const response = await EventService.getEvents();
        this.events = response.data.filter(event => event.createdBy === this.userId);
      } catch (error) {
        console.error("Error fetching events:", error);
      }
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
    async fetchPlaylists() {
      try {
        // Get token
        const tokenResponse = await SpotifyService.getToken();
        this.token = tokenResponse.data.access_token;

        // Fetch playlist using the token
        const playlistResponse = await SpotifyService.getPlaylist(this.token);
        console.log("Fetched Playlist Data:", playlistResponse.data); // Debugging
        this.playlistName = playlistResponse.data.name;
        this.playlists = playlistResponse.data.tracks.items.map((track) => ({
          id: track.track.id,
          name: track.track.name,
          images: track.track.album.images,
          artists: track.track.artists.map(artist => artist.name).join(", "),
          genre: "Unknown" // Placeholder for genre
        }));
      } catch (error) {
        console.error("Error fetching Spotify playlists:", error);
      }
    },
    async searchSongs() {
      if (this.searchQuery.trim() === "") {
        this.searchResults = [];
        return;
    }

      try {
        const tokenResponse = await SpotifyService.getToken();
        const token = tokenResponse.data.access_token;

        // If a genre is selected, add it to the query
        let query = this.searchQuery;
        if (this.selectedGenre) {
      query += ` genre:"${this.selectedGenre}"`;
    }

        const searchResponse = await SpotifyService.searchTracks(query, token);
        const tracks = searchResponse.data.tracks.items;

        this.searchResults = tracks.map(track => ({
          id: track.id,
          name: track.name,
          album: track.album,
          artists: track.artists,
          genre: this.selectedGenre || "Unknown"
        }));
    }     catch (error) {
        console.error("Error searching for songs:", error);
  }
},
    async createNewPlaylist() {
      if (!this.selectedTracks.length) {
        alert("Please select at least one song to create the playlist.");
        return;
      }

      // Use the inputted playlist name or fallback to a default name
      const playlistName = this.playlistNameInput.trim() || "My Custom Playlist";

      try {
        // Get token
        const tokenResponse = await SpotifyService.getToken();
        this.token = tokenResponse.data.access_token;

        // Create the playlist
        const playlistResponse = await SpotifyService.createPlaylist(
          this.userId,
          playlistName,
          this.token
        );

        // Add selected tracks to the new playlist
        await SpotifyService.addTracksToPlaylist(playlistResponse.data.id, this.selectedTracks, this.token);

        this.playlistName = playlistName; // Set playlist name
        alert("Playlist created successfully!");
      } catch (error) {
        console.error("Error creating playlist:", error);
      }
    },

    async fetchGenres() {
    try {
      console.log("Fetching token...");
      const tokenResponse = await SpotifyService.getToken();
      const token = tokenResponse.data.access_token;
      console.log("Token received:", token);

      console.log("Fetching available genres...");
      const genreResponse = await SpotifyService.getAvailableGenres(token);
      console.log("Genre Response:", genreResponse.data);

      this.availableGenres = genreResponse.data.genres || []; // Prevents undefined issues
    } catch (error) {
      console.error("Error fetching genres:", error);
    }
  },

  },

  

  created() {
    this.fetchMyEvents();
    this.fetchPlaylists();
    this.fetchGenres();
    
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

/* CREATE PLAYLIST SECTION */

.create-playlist-section {
  text-align: center;
  margin-bottom: 30px;
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
  

/*SPOTIFY  */

.playlist-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.playlist-card {
  display: flex;
  align-items: center;
  background: #1e1e1e;
  padding: 15px;
  border-radius: 10px;
  width: 600px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.album-cover {
  width: 80px;
  height: 80px;
  border-radius: 5px;
  margin-right: 15px;
}

.track-info {
  flex-grow: 1;
}

.track-name {
  font-size: 16px;
  color: #fff;
}

.track-artist,
.track-genre {
  font-size: 14px;
  color: #bbb;
}

.spotify-player {
  width: 300px;
  height: 80px;
}

</style>
