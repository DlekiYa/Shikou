<script setup>
import CreateWorkspace from './CreateWorkspace.vue';
</script>

<template>
    <div class="sidepanel">
        <CreateWorkspace></CreateWorkspace>
        <div v-for="item in workspaces" class="workspaces">
            <a v-on:click="this.$emit('change-workspace', item.id);">{{ item.name }}</a>
        </div>
    </div>
</template>

<script>
export default {
  name: 'SidePanel',
  props: {
    workspaces: Array,
  }
}
</script>

<style scoped>
    .sidepanel {
        position: absolute;
        top: 5%;
        left: 0px;
        display: flex;
        flex-direction: column;
        gap: 20px;
        padding: 10px 20px;
        background-color: #0F5F0F;
        border-bottom: 1px solid #ddd;
        height: 90%;
        width: 14%;
    }
    .workspaces {
        display: flex;
    }
</style>