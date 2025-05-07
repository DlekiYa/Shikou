<template>
    <div class="popup-form-container">
      <!-- Main Button -->
      <button 
        class="popup-button" 
        @click="toggleForm"
      >
        {{ buttonText }}
      </button>
  
      <!-- Popup Form (shown when isFormVisible is true) -->
      <div v-if="isFormVisible" class="popup-form">
        <div class="popup-content">
          <h3>Create a new workspace</h3>
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="name">Name:</label>
              <input 
                type="text" 
                id="name" 
                v-model="name" 
                required 
                placeholder="Type workspace name here"
              />
            </div>
            <div class="form-actions">
              <button type="submit" class="submit-button">Submit</button>
              <button type="button" class="cancel-button" @click="closeForm">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
import { collapseTextChangeRangesAcrossMultipleVersions } from 'typescript';
  export default {
    name: 'PopupFormButton',
    data() {
      return {
        isFormVisible: false,
        name: '',
        buttonText: 'Create workspace'
      }
    },
    methods: {
      toggleForm() {
        this.isFormVisible = !this.isFormVisible;
        this.buttonText = 'Create workspace';
      },
      closeForm() {
        this.isFormVisible = false;
        this.buttonText = 'Open Form';
        this.name = '';
      },
      handleSubmit() {
        // Emit an event with the submitted name
        //this.$emit('submit', this.name);

        var response = axios.post("http://localhost:8080/api/workspace/create", 
        {
            'username': localStorage.getItem('username'),
            'name': this.name
        },
        {
            'headers': {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })

        response.then((x) => {
            console.log(x)
        })
        
        // Reset form and close
        this.name = '';
        this.closeForm();
        
        // Optional: Show a success message
        alert(`Workspace created: ${this.name}`);

        location.reload();
      }
    }
  }
  </script>
  
  <style scoped>
  .popup-form-container {
    position: relative;
    display: inline-block;
  }
  
  .popup-button {
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;
  }
  
  .popup-button:hover {
    background-color: #45a049;
  }
  
  .popup-form {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .popup-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  .form-group input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
  }
  
  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .submit-button {
    padding: 8px 16px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .submit-button:hover {
    background-color: #45a049;
  }
  
  .cancel-button {
    padding: 8px 16px;
    background-color: #f44336;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .cancel-button:hover {
    background-color: #d32f2f;
  }
  </style>