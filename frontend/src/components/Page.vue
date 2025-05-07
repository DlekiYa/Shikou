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

async function updateQuill(newDoc) {
  fileName.value = newDoc
  if (quillRef.value) {
    console.log(fileName.value);
    const quill = quillRef.value.getQuill();
    
    var response = await axios.post(
      'http://localhost:8080/api/document/path',{
        "path": fileName.value,
        "workspaceId": Number(props.workspaceId)
      },
      { 'headers': {
        "Authorization": "Bearer " + localStorage.getItem('token')
      }})

      console.log({'data': {
        "path": fileName.value,
        "workspaceId": Number(workspaceId.value)
      }})
      //console.log(response.data);
      quill.setText(response.data);
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

  directory.value = {'isDir': true, 'name': 'root', 'children': JSON.parse(response.data.status)}
  console.log(directory.value)
}

watch(() => props.workspaceId, (newId, oldId) => {
  if (newId !== oldId) {
    updateDirectory();
  }
}, { immediate: true });

async function openCreateWindow(event) {
    console.log("woahhhh " + event.target.directory)
}
</script>

<template>
    <div class="page">
        <div class="containerBrowse">
          <RecursiveDirView pref="--" :cumul="directory.name" :directory="directory" @doc-selected="updateQuill"></RecursiveDirView>
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