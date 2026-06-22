import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

for(const [key,component] of Object.entries(ElementPlusIconVue)){
    app.component(key, component)
}

app.mount('#app')
