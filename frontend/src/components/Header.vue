<template>
    <header class="header">
        <div class="logo">=)</div>
    </header>
</template>

<style scoped>
    .header {
        position: absolute;
        top: 0px;
        left: 0px;
        display: flex;
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
</style>