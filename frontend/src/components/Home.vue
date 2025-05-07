<script setup lang="ts">
    import Header from './Header.vue';
    import SidePanel from './SidePanel.vue';
    import Page from './Page.vue';
    import axios, { Axios } from 'axios';

    var token = localStorage.getItem('token');

    console.log(token);

    var needLoginFlag = false;

    if (token == null) {
        console.log("TOKEN IS NULL");
        needLoginFlag = true
    } else {
        var response = axios.get(
            'http://localhost:8080/api/user/' + localStorage.getItem('username'),
            {'headers':
             {'Authorization': `Bearer ${localStorage.getItem('token')}`}
            }
        )
    }
</script>

<template>
    <div class="homepage">
        <Header></Header>
        <SidePanel></SidePanel>
        <div v-if="needLoginFlag" class="needlogin">
            Please login
        </div>
        <Page v-else></Page>
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