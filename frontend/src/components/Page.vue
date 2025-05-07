<script setup>
import { ref, defineProps, watch } from 'vue';
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import axios from 'axios';
import RecursiveDirView from './RecursiveDirView.vue';

const props = defineProps({
  workspaceId: {
    type: Number
  }
});

const quillRef = ref(null);
const directory = ref({});

console.log('llmaaaooo ' + props.workspaceId);

var prevDirectory = {}
const workspaceId = ref(props.workspaceId);
const fileName = ref('');

function updateQuill(newDoc) {
  fileName.value = newDoc
  if (quillRef.value) {
    console.log(fileName.value);
    const quill = quillRef.value.getQuill();
    quill.setText(fileName.value);
    // TODO: read document from api here
  };
}

async function updateDirectory() {
  console.log('http://localhost:8080/api/workspace/' + props.workspaceId)
  var response = await axios.get('http://localhost:8080/api/workspace/' + props.workspaceId,
        {'headers': {
            "Authorization": "Bearer " + localStorage.getItem('token')
          }
        }
  )
  console.log(response.data.status)
  /*directory.value = {'isDir': true, 'name': 'ugly', 'contents': [
    {'isDir': true, 'name': 'fgggg', 'contents':
     [{'isDir': false, 'name': 'gadsg.txt'}]
    },
    {'isDir': false, 'name': 'f2.jar'}, 
    {'isDir': false, 'name': 'text.txt'},
    {'isDir': true, 'name': 'haha', 'contents': [
      {'isDir': false, 'name': 'f3.jar'},
      {'isDir': false, 'name': 'f4.jar'},
      {'isDir': false, 'name': 'f5.jar'},
      {'isDir': true, 'name': 'haha2', 'contents': [
        {'isDir': false, 'name': 'f6.jar'},
        {'isDir': false, 'name': 'f7.jar'},
        {'isDir': false, 'name': 'f8.jar'},
      ]}
    ]}
  ]}*/

  directory.value = response.data.status
  console.log(directory.value)
}

watch(() => props.workspaceId, (newId, oldId) => {
  if (newId !== oldId) {
    updateDirectory();
  }
}, { immediate: true });
</script>

<template>
    <div class="page">
        <div class="containerBrowse">
          {{ directory.name }}:
          <RecursiveDirView pref="--" :directory="directory" @doc-selected="updateQuill"></RecursiveDirView>
        </div>
        <quill-editor ref="quillRef" theme="snow"></quill-editor>
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
        border-bottom: 1px solid #ddd;
        height: 80%;
        width: 60%;
        display: flex;
        flex-direction: column;
    }
    .containerBrowse {
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
</style>