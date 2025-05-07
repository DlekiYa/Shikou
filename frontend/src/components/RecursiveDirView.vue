<script>
export default {
  name: 'RecursiveDirView',
  props: {
    directory: Object,
    pref: String,
    cumul: String
  }, methods: {
    openCreateWindow: (event) => {
        console.log("woahhhh " + event)
    }
  }
}


</script>

<template>
    <div class="create_button" v-on:click="openCreateWindow(cumul)">{{ directory.name }}:   +</div>
    <div v-if="directory" v-for="item in directory.children">
        <div v-if="item.isDir">{{ pref + item.name }}:</div>
        <RecursiveDirView :cumul="cumul + '/' + item.name"
            @doc-selected="(kek)=>{
            this.$emit('doc-selected', kek)
        }" v-if="item.isDir" :pref="pref + '--'" :directory="item" class="directory">
        </RecursiveDirView>
        <div v-else class="file" v-on:click="this.$emit('doc-selected', item.name)"> {{ pref }}{{ item.name }}</div>
    </div>
</template>

<style>
    .create_button {
        border-width: 5px;
        border-color: black;
    }
</style>