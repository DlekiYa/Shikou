<script setup>
import { ref, defineProps, onUpdated } from 'vue';
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import WorkspaceBrowse from './WorkspaceBrowse.vue';

const quillRef = ref(null);

const props = defineProps({
  workspace: {
    type: String
  }
});
onUpdated(async () => {
    console.log(quillRef.value)
  if (quillRef.value) {
    console.log(props.workspace)
    const quill = quillRef.value.getQuill();
    quill.setText(String(props.workspace));
    // TODO: read document from api here
  }});

</script>

<template>
    <div class="page">
        <WorkspaceBrowse :workspaceId="workspaceId"></WorkspaceBrowse>
        <quill-editor theme="snow" ref="quillRef"></quill-editor>
    </div>
</template>

<script>
export default {
  name: 'Page',
  props: {
    workspaceId: Number,
  }
}
</script>

<style scoped>
    .page {
        position: absolute;
        top: 5%;
        left: 20%;
        border-bottom: 1px solid #ddd; /* Optional: Adds a border at the bottom */
        height: 80%;
        width: 60%;
    }
</style>