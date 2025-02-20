<template>
  <div>
    <header class="header">
      <img src="/landingGif.gif" alt="Header GIF" class="header-gif" />
    </header>
    <div class="landing-page">
      <div class="container">
        <!-- Filter Inputs -->
        <div class="filter-container">
          <input
            type="text"
            v-model="nameFilter"
            placeholder="Filter by name"
            class="filter-input"
          />
          <input
            type="date"
            v-model="dateFilter"
            placeholder="Filter by date"
            class="filter-input"
          />
        </div>
        <!-- Event Display -->
        <div class="large-boxes-container">
          <div v-for="(event, index) in filteredEvents" :key="index" class="large-box">
            <p>{{ event.name }}</p>
            <p>{{ event.eventDate }}</p>
            <p>{{ event.startTime }}</p>
            <p>{{ event.endTime }}</p>
          </div>
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
    };
  },
  computed: {
    filteredEvents() {
      return this.events.filter(event => {
        const matchesName = event.name
          .toLowerCase()
          .includes(this.nameFilter.toLowerCase());
        const matchesDate = this.dateFilter
          ? event.eventDate === this.dateFilter
          : true;
        return matchesName && matchesDate;
      });
    },
  },
  methods: {
    retrieveEvents() {
      EventService.getEvents().then(response => {
        this.events = response.data;
      });
    },
  },
  created() {
    this.retrieveEvents();
  },
};
</script>
<style scoped>
    .header {
        width: 100%;
        margin-bottom: 0px;
    }
    .header-gif {
        width: 100%;
        height:auto;
        object-fit: cover;
    }
    .landing-page{
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
    .large-boxes-container {
        display: flex;
        justify-content: center;
        width: 100%;
        gap: 20px;
    }
    .large-box {
        background-color: black;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        font-weight: bold;
        height: 400px;
        width: calc(40% - 10px);
        box-sizing: border-box;
        border-radius: 12px;
    }
    .small-boxes-container  {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        justify-content: center;
        width: 100%;
    }
    .small-box {
        background-color: whitesmoke;
        border-radius: 8px;
        box-sizing: border-box;
        height: 200px;
        width: calc(20% - 15px);
    }
    .text {
        text-align: center;
        font-size: 30px;
        color: black;
        margin: 20px 0;
    }
    .see-more-button {
        margin-top: 20px;
        padding: 10px;
        background-color: #fff;
        color: purple;
        border: 2px solid purple;
        border-radius: 8px;
        cursor: pointer;
        margin-bottom: 200px;
    }
    .see-more-button:hover {
        background-color: purple;
        color: white;
    }
    .footer {
        background-color: black;
        color: white;
        width: 100%;
        text-align: center;
        padding: 40px 20px;
        position: fixed;
        bottom: 0;
        left: 0;
        box-sizing: border-box;
        font-size: 18px;
    }
</style>