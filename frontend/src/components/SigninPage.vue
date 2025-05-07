<template>
    <div class="signin-container">
      <div class="signin-card">
        <h1 class="signin-title">Sign In</h1>
        
        <form @submit.prevent="handleSubmit" class="signin-form">
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
          
          <button type="submit" class="signin-button" :disabled="loading">
            <span v-if="!loading">Sign In</span>
            <span v-else>Signing In...</span>
          </button>
        </form>
        
        <div class="signup-link">
          Don't have an account? <a href="signup">Sign up</a>
        </div>
        
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
      const response = axios.post('http://localhost:8080/api/auth/signin',
       {'username': form.value.email, 'password': form.value.password}
      );

      console.log(response)
      
      // Replace with actual authentication logic
      // const response = await authService.login(form.value);
      
      // For demo purposes, just check if email contains 'demo'
      if ((await response).status == 200) {
        // Store authentication state (in a real app, use Vuex/Pinia)
        localStorage.setItem('token', (await response).data['token']);
        localStorage.setItem('username', form.value.email)
        
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
  .signin-container {
    font-family: 'Courier New', Courier, monospace;
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 20px;
  }
  
  .signin-card {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
  }
  
  .signin-title {
    text-align: center;
    margin-bottom: 1.5rem;
    color: #333;
  }
  
  .signin-form {
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
  
  .signin-button {
    padding: 0.75rem;
    background-color: #646cff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .signin-button:hover {
    background-color: #535bf2;
  }
  
  .signin-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .signup-link {
    text-align: center;
    margin-top: 1.5rem;
    font-size: 0.9rem;
  }
  
  .signup-link a {
    color: #646cff;
    text-decoration: none;
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