<template>
    <div class="signup-container">
      <div class="signup-card">
        <h1 class="signup-title">Sign Up</h1>
        
        <form @submit.prevent="handleSubmit" class="signup-form">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              v-model="form.email"
              required
              placeholder="Enter your email"
              class="form-input"
            />
          </div>
          
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              v-model="form.password"
              required
              placeholder="Enter your password"
              class="form-input"
            />
          </div>
          
          <button type="submit" class="signup-button" :disabled="loading">
            <span v-if="!loading">Sign Up</span>
            <span v-else>Signing Up...</span>
          </button>
        </form>
        
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
  import axios from 'axios';
  
  const form = ref({
    email: '',
    password: ''
  });
  
    function redirect(endpoint : string) {
        location.replace(endpoint);
    };

  const loading = ref(false);
  const error = ref('');
  
  const handleSubmit = async () => {
    error.value = '';
    loading.value = true;
    
    try {
      // Simulate API call
      const response = axios.post('http://localhost:8080/api/auth/signup', {'username': form.value.email, 'password': form.value.password});

      // Replace with actual authentication logic
      // const response = await authService.login(form.value);
      
      // For demo purposes, just check if email contains 'demo'
      if ((await response).status == 200) {
        // Store authentication state (in a real app, use Vuex/Pinia)
        localStorage.setItem('isAuthenticated', 'true');
        
        // Redirect to dashboard or intended route
        redirect('/');
      } else {
        throw new Error('Invalid credentials. Try "demo@example.com" with password of 6+ characters.');
      }
    } catch (err: any) {
      error.value = err.message || 'Login failed. Please try again.';
    } finally {
      loading.value = false;
    }
  };
  </script>
  
  <style scoped>
  .signup-container {
    font-family: 'Courier New', Courier, monospace;
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 20px;
  }
  
  .signup-card {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
  }
  
  .signup-title {
    text-align: center;
    margin-bottom: 1.5rem;
    color: #333;
  }
  
  .signup-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-input {
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
  }
  
  .form-input:focus {
    outline: none;
    border-color: #646cff;
  }
  
  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 0.9rem;
  }
  
  .signup-button {
    padding: 0.75rem;
    background-color: #646cff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .signup-button:hover {
    background-color: #535bf2;
  }
  
  .signup-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .error-message {
    margin-top: 1rem;
    padding: 0.75rem;
    background-color: #ffebee;
    color: #d32f2f;
    border-radius: 4px;
    font-size: 0.9rem;
  }
  </style>