<template>
  <div id="login">
    <!-- NEW: Background video element -->
    <video autoplay loop muted playsinline class="background-video">
      <source src="/MotionBackground.mp4" type="video/mp4" />
      Your browser does not support the video tag.
    </video>

    <!-- NEW: Overlay container for form content -->
    <div class="login-overlay">
      <form @submit.prevent="login">
        <h1>SIGN IN. LOG IN. TAP IN.</h1>
        <div role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div role="alert" v-if="$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="user.username" required autofocus />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="user.password" required />
        </div>
        <button type="submit">Sign in</button>
        <p>
          <router-link :to="{ name: 'register' }">You look new. Sign Up!</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "LoginView",
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService.login(this.user)
        .then(response => {
          if (response.status === 200) {
            // Store token and user in Vuex/localStorage
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);

            // Role-based redirection
            const userRole = response.data.user.authorities[0].name;
            if (userRole === "ROLE_DJ") {
              this.$router.push({ name: "djPage" });
            } else if (userRole === "ROLE_HOST") {
              this.$router.push({ name: "hostPage" });
            } else {
              this.$router.push({ name: "landing" });
            }
          }
        })
        .catch(error => {
          const response = error.response;
          if (response && response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>
/* NEW: Style for background video */
.background-video {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1; /* Ensure the video is behind the form */
}

#login {
  position: relative;
  height: 100vh;
  font-family: Arial, Helvetica, sans-serif;
  color: burlywood;
}

/* NEW: Overlay container so that form content is readable */
.login-overlay {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  background: rgba(255, 255, 255, 0.8); /* Semi-transparent background for readability */
  padding: 2rem;
  border-radius: 8px;
  margin: 50px auto;
}

/* Original form styling remains unchanged */
.form-input-group {
  margin-bottom: 1rem;
}
label {
  margin-right: 0.5rem;
}
</style>
