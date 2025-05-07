<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Header from './Header.vue';
import SidePanel from './SidePanel.vue';
import Page from './Page.vue';

// Reactive references
const token = ref(localStorage.getItem('token'));
const needLoginFlag = ref(false);
const workspaces = ref([]); // Reactive workspaces array
const current_workspaceId = ref(-1)

console.log(token.value);

// Fetch workspaces when component mounts
onMounted(async () => {
  if (!token.value) {
    console.log("TOKEN IS NULL");
    needLoginFlag.value = true;
  } else {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/user/${localStorage.getItem('username')}`,
        {'headers': {
            "Authorization": "Bearer " + localStorage.getItem('token')
          }
        }
      );
      console.log(response.data.workspaces)
      workspaces.value = response.data.workspaces; // Update reactive reference
    } catch (error) {
      console.error("Error fetching workspaces:", error);
      needLoginFlag.value = true;
    }
  }
});

// Props definition
const handleWorkspaceChange = (workspaceData) => {
    console.log('changed workspace to ' + workspaceData)
    current_workspaceId.value = workspaceData
};

</script>

<template>
    <div class="homepage">
        <Header></Header>
        <SidePanel :workspaces="workspaces" @change-workspace="handleWorkspaceChange"></SidePanel>
        <div v-if="needLoginFlag" class="needlogin">
            Please login
        </div>
        <Page v-else :workspaceId="current_workspaceId"></Page>
    </div>
</template>

<style>
    .homepage {
        font-family: 'Courier New', Courier, monospace;
        font-size: medium;
    }
    .needlogin {
        position: absolute;
        top:50%;
        left: 50%;
    }
</style>