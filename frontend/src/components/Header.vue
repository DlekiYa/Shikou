<script setup lang="ts">
    function redirect(endpoint : string) {
        location.replace(endpoint)
    }
</script>

<template>
    <header class="header">
        <div class="logo">シ</div>
        <div class="authbox">
            <div class="signin" @click="redirect('/signin')"> sign in</div>
            <div class="signup"> sign up </div>
        </div>
    </header>
</template>

<style scoped>
    .header {
        position: absolute;
        top: 0px;
        left: 0px;
        display: grid;
        grid-template-columns: 85% 15%;
        justify-content: left; /* Centers the logo horizontally */
        align-items: center; /* Centers the logo vertically */
        background-color: #f8f8f8; /* Light background color */
        border-bottom: 2px solid #ddd; /* Optional: Adds a border at the bottom */
        width: 100%;
        height: 5%;
    }

    .logo img {
        height: 50px; /* Adjust the height of the logo */
        width: auto; /* Keeps the aspect ratio */
    }

    .authbox {
        justify-content: center;
        display: grid;
        grid-template-columns: 50% 50%;
    }

    .signin {
        border: 5px solid #105010;
        padding-inline: 10px;
        border-radius: 10px;
        background-color: #105010;
        color: white;
        justify-self: center;
        font-weight: bold;
    }

    .signup {
        border: 2px solid black;
        padding-inline: 10px;
        border-radius: 10px;
        background-color: #e0e0e0;
        color: #105010;
        justify-self: center;
        align-items: center;
        justify-content: center;
    }
</style>