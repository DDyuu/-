<template>
  <div class="home-container">
    <!-- 背景图 -->
    <div class="background"></div>

    <!-- 内容区 -->
    <div class="content">
      <!-- 时钟 -->
      <div class="clock-card">
        <div class="clock-time">{{ time }}</div>
        <div class="clock-date">{{ date }}</div>
      </div>

      <!-- 文章列表 -->
      <div class="article-section">
        <div class="section-title">最新文章</div>
        <div class="article-grid">
          <div v-for="item in data.tableData" :key="item.id" class="article-card" @click="view(item)">
            <div class="article-title">{{ item.title }}</div>
            <div class="article-desc">{{ item.description }}</div>
            <div class="article-date">{{ formatDate(item.date) }}</div>
          </div>
        </div>
        <div style="margin-top: 20px; display: flex; justify-content: center;">
          <el-pagination
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[4, 8, 12, 16]"
              :total="data.total"
              background
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="load"
              @current-change="load"
          />
        </div>
      </div>
    </div>

    <!-- 查看文章弹窗 -->
    <el-dialog title="文章内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div style="padding: 10px">
        <h2 style="margin-bottom: 10px">{{ data.currentArticle.title }}</h2>
        <div style="color: #999; margin-bottom: 15px; font-size: 14px">
          <span>发布于: {{ data.currentArticle.date }}</span>
          <span v-if="data.currentArticle.description" style="margin-left: 20px; color: #666">{{ data.currentArticle.description }}</span>
        </div>
        <div class="editor-content-view" v-html="data.currentArticle.content"></div>
      </div>
      <template #footer>
        <el-button @click="data.viewVisible = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted, onUnmounted} from "vue";
import request from "@/utils/request.js";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  viewVisible: false,
  currentArticle: {}
})

const time = ref('')
const date = ref('')
let timer = null

const updateClock = () => {
  const now = new Date()
  const h = String(now.getHours()).padStart(2, '0')
  const m = String(now.getMinutes()).padStart(2, '0')
  const s = String(now.getSeconds()).padStart(2, '0')
  time.value = h + ':' + m + ':' + s
  const y = now.getFullYear()
  const mo = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  const weekdays = ['日', '一', '二', '三', '四', '五', '六']
  const w = weekdays[now.getDay()]
  date.value = y + '年' + mo + '月' + d + '日 星期' + w
}

const formatDate = (d) => {
  if (!d) return ''
  const dateObj = new Date(d)
  const mo = String(dateObj.getMonth() + 1).padStart(2, '0')
  const day = String(dateObj.getDate()).padStart(2, '0')
  const h = String(dateObj.getHours()).padStart(2, '0')
  const mi = String(dateObj.getMinutes()).padStart(2, '0')
  return mo + '月' + day + '日 ' + h + ':' + mi
}

const load = () => {
  request.get('/article/selectPage', {
    params: { pageNum: data.pageNum, pageSize: data.pageSize }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

const view = (item) => {
  data.currentArticle = item
  data.viewVisible = true
}

onMounted(() => {
  updateClock()
  timer = setInterval(updateClock, 1000)
  load()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.home-container {
  position: relative;
  min-height: calc(100vh - 80px);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('../assets/background.jpg') no-repeat center center fixed;
  background-size: cover;
  z-index: 0;
  opacity: 1;
  pointer-events: none;
}

.content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 900px;
}

.clock-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 30px 40px;
  text-align: center;
  margin-bottom: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.clock-time {
  font-size: 64px;
  font-weight: bold;
  color: #333;
  letter-spacing: 4px;
  font-family: 'Courier New', monospace;
}

.clock-date {
  font-size: 18px;
  color: #666;
  margin-top: 8px;
}

.article-section {
  background: #ffffff;
  border-radius: 16px;
  padding: 25px 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
}

.article-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.article-card {
  background: white;
  border-radius: 10px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #eee;
}

.article-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.15);
  border-color: #409eff;
}

.article-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-desc {
  font-size: 13px;
  color: #888;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 10px;
}

.article-date {
  font-size: 12px;
  color: #bbb;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .home-container {
    padding: 10px;
  }
  .clock-card {
    padding: 20px;
  }
  .clock-time {
    font-size: 42px;
  }
  .clock-date {
    font-size: 15px;
  }
  .article-section {
    padding: 15px;
  }
  .article-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}

@media (max-width: 576px) {
  .clock-time {
    font-size: 32px;
  }
  .clock-date {
    font-size: 13px;
  }
}
</style>
