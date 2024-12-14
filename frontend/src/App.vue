<script setup lang="ts">
  import { RouterLink, RouterView } from 'vue-router'
  //import HelloWorld from './components/HelloWorld.vue'
  import SidePanel from './components/SidePanel.vue';
  import Header from './components/Header.vue';
  import Page from './components/Page.vue';
</script>

<template>
  <Header></Header>
  <SidePanel></SidePanel>
  <Page></Page>
</template>

<style>
</style>