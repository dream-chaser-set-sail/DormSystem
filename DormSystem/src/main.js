import { createApp } from 'vue'
import App from './App.vue'

import {router} from "@/router/index.js";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css' //导入element-plus样式
import {createPinia} from 'pinia'
const pinia = createPinia()
//引入持久化插件
import piniaPluginPersistedstate from "pinia-plugin-persistedstate"
//使用持久化插件
pinia.use(piniaPluginPersistedstate)

createApp(App).use(router).use(ElementPlus).use(pinia).mount('#app')
