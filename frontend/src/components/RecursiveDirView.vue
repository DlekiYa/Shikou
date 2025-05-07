<script>
export default {
  name: 'RecursiveDirView',
  props: {
    directory: Object,
    pref: String
  }
}
</script>

<template>
    <div v-if="directory" v-for="item in directory.contents">
        <div v-if="item.isDir">{{ pref + item.name }}:</div>
        <RecursiveDirView @doc-selected="(kek)=>{
            this.$emit('doc-selected', kek)
        }" v-if="item.isDir" :pref="pref + '--'" :directory="item" class="directory">
        </RecursiveDirView>
        <div v-else class="file" v-on:click="this.$emit('doc-selected', item.name)"> {{ pref }}{{ item.name }}</div>
    </div>
</template>
