<template>
    
    

    <div>
        <div class="landing-page">

        <div class="container">

            <div class="large-boxes-container">
                <div class="large-box">EVENT</div>
                <div class="large-box">EVENT</div>
            </div>

            <div class="text">
                <p> Popular Songs </p>
            </div>  

            <div class="small-boxes-container">
                <div v-for="(box, index) in visibleBoxes" :key="index" class="small-box"></div>
            </div>

        </div>
    
            <button v-on:click="toggleBoxes" class="see-more-button"> {{ isExpanded ? 'See Less' : 'See More' }}</button>

            <footer class="footer">
                <h1>Upcoming Events</h1>
        

        </footer>

    

        </div>
    </div>
</template>

<script>
export default {
    name: 'LandingPageView',
    data() {
    return {
      totalBoxes: 16, // Total number of small boxes available
      visibleCount: 8, // Initial number of boxes to show
      increment: 8,   // Number of boxes to show on each click
      isExpanded: false
    }
  },
  computed: {
    visibleBoxes() {
      return Array(this.visibleCount).fill({});
    }
  },
  methods: {
    toggleBoxes() {
      if (this.isExpanded) {
        this.visibleCount = 8;
      } else {
        this.visibleCount = this.totalBoxes;
      }
      this.isExpanded = !this.isExpanded;
      
    }
  }

}
</script>

<style scoped>

    .header {
        display: flex;
        align-items: center;
        padding: 10px;
    }

    .video {
        height: 400px;
        width: auto;
        margin-right: 15px;
    }

    .landing-page{
        background-color: rgb(141, 103, 175);
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;
        min-height: 100vh;
        box-sizing: border-box;
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
