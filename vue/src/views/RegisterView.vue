<template>
  <div id="register" class="text-center">
    <video autoplay loop muted playsinline class="background-video">
      <source src="/MotionBackground.mp4" type="video/mp4" />
      Your browser does not support the video tag.
    </video>

    <div class="register-overlay">
      <form @submit.prevent="register">
        <h1>SIGN UP. LOG IN. TAP IN.</h1>
        <div role="alert" v-if="registrationErrors">
          {{ registrationErrorMsg }}
        </div>
        <div class="form-input-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="user.username" required autofocus />
        </div>
        <div class="form-input-group">
          <label for="handle">Handle (@handle)</label>  <!-- NEW: Added handle input -->
          <input type="text" id="handle" v-model="user.handle" required />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="user.password" required />
        </div>
        <div class="form-input-group">
          <label for="confirmPassword">Confirm Password</label>
          <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
        </div>
        <div class="form-input-group">
          <label for="role">Select Role</label>
          <select id="role" v-model="user.role" required>
            <option value="" disabled>Select a role</option>
            <option value="user">User</option>
            <option value="host">Host</option>
            <option value="dj">DJ</option>
          </select>
        </div>
        <button type="submit">Create Account</button>
        <p>
          <router-link :to="{ name: 'login' }">You look familiar. Log in!</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from '../services/AuthService';

export default {
  name: "RegisterView",
  data() {
    return {
      user: {
        username: '',
        handle: '',  // NEW: Users must enter handle manually
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password !== this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService.register(this.user)
          .then((response) => {
            if (response.status === 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response && response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    }
  },
};
</script>
