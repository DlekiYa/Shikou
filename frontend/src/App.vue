<script setup lang="ts">
  import {ref, computed} from 'vue'
  
  import Home from './components/Home.vue'
  import SigninPage from './components/SigninPage.vue';
  import NotFound from './components/NotFound.vue';

  const routes : { [key: string]: any } = {
  '/': Home,
  '/signin': SigninPage
  }

  const currentPath = ref(window.location.pathname)

  window.addEventListener('pathchange', () => {
    currentPath.value = window.location.pathname
  })

  const currentView = computed(() => {
    console.log(currentPath.value)
    return routes[currentPath.value.slice(0) || '/'] || NotFound
  })
</script>

<template>
  <component :is="currentView" />
</template>

<style>
</style>