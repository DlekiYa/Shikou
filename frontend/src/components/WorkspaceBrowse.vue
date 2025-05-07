<script setup>
import {ref} from 'vue';
import axios from 'axios';

const directory = ref(null);

async function updateDirectory() {
    var response = await axios.get('http://localhost:8080/api/document/' + props.workspaceId)
    console.log(response.data)
}
</script>

<template>
    <div class="containerBrowse">
        <li v-if="directory" v-for="item in directory">
            <div class="item">{{ item }}</div>
        </li>
    </div>
</template>

<script>
export default {
  name: 'WorkspaceBrowse',
  props: {
    workspaceId: Number,
  }
}
</script>

<style>

</style>