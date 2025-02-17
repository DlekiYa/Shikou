import './assets/main.css'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

import { createApp, ref, computed } from 'vue'
import { createPinia } from 'pinia'
import {QuillEditor} from '@vueup/vue-quill'

import App from './App.vue'

const app = createApp(App)

app.component('QuillEditor', QuillEditor);

app.use(createPinia())

app.mount('#app')
