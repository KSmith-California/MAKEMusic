<template>
  <div class="create-event">
    <h1>Create Event</h1>
    <form @submit.prevent="submitEvent">
      <div class="form-group">
        <label for="name">Event Name:</label>
        <input type="text" id="name" v-model="event.name" required />
      </div>
      <div class="form-group">
        <label for="handle">Search by @Handle:</label>
        <input type="text" id="handle" v-model="searchQuery" @input="searchUsers" placeholder="Search users..." />
        <ul v-if="searchResults.length">
          <li v-for="user in searchResults" :key="user.id" @click="selectUser(user)">
            @{{ user.handle }} ({{ user.username }})
          </li>
        </ul>
      </div>
      <button type="submit">Create Event</button>
    </form>
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <div v-if="successMessage" class="success">{{ successMessage }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      event: {
        name: '',
      },
      searchQuery: '',
      searchResults: [],
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async searchUsers() {
      if (this.searchQuery.length < 2) return;
      try {
        const response = await axios.get(`${import.meta.env.VITE_REMOTE_API}/users/search?query=${this.searchQuery}`);
        this.searchResults = response.data;
      } catch (error) {
        this.errorMessage = 'Failed to search users.';
      }
    },
    selectUser(user) {
      this.event.selectedUser = user;
      this.searchQuery = `@${user.handle}`;
      this.searchResults = [];
    }
  }
};
</script>
