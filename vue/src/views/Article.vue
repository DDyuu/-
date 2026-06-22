<template>
  <div>
    <div class="card" style="margin-bottom: 20px">
      <el-input v-model="data.title" :prefix-icon="Search" placeholder="请输入标题查询"
                style="width: 200px; margin-right: 5px"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 20px">
      <el-button plain type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="primary" @click="data.selectVisible = !data.selectVisible">批量操作</el-button>
      <el-button v-if="data.selectVisible" type="danger" @click="deleteBatch">删 除</el-button>
    </div>

    <!--    表单数据-->
    <div class="card" style="margin-bottom: 20px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column v-if="data.selectVisible" type="selection" width="50"/>
        <el-table-column v-if="!data.selectVisible" width="50"/>
        <el-table-column label="标题" prop="title"/>
        <el-table-column label="封面">
          <template v-slot="scope">
            <el-image v-if="scope.row.cover" :preview-src-list=[scope.row.cover] :src="scope.row.cover"
                      alt="" preview-teleported="true" style="width: 100px; height: 60px;"/>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" show-overflow-tooltip="true"/>
        <el-table-column label="内容" prop="content">
          <template #default="scope">
            <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="date"/>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button circle icon="Edit" type="primary" @click="handleUpdate(scope.row)"/>
            <el-button circle icon="Delete" type="danger" @click="handleDelete(scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px">
        <el-pagination
            v-model:current-page='data.pageNum'
            v-model:page-size='data.pageSize'
            :page-sizes="[5,10,15,20]"
            :total='data.total'
            background
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="load"
            @current-change="load"
        />
      </div>
    </div>

    <!--    修改文章弹窗-->
    <el-dialog v-model="data.formVisible" destroy-on-close title="文章信息" width="50%">
      <el-form ref="formRef" :model="data.form" style="padding-right: 50px; padding-top:20px">
        <el-form-item label="标题" label-width="80px" prop="title">
          <el-input v-model="data.form.title" :disabled="data.form.id" autocomplete="off">标题</el-input>
        </el-form-item>
        <el-form-item label-width="80px">
          <el-upload
              :on-success="handleCoverSuccess"
              action="http://localhost:8080/files/upload"
          >
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="简介" label-width="80px" prop="description">
          <el-input v-model="data.form.description" :rows="3" autocomplete="off" placeholder="请输入简介"
                    type="textarea"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div style="border:1px solid #ccc;width:100%">
            <Toolbar
                :editor="editorRef"
                :mode="mode"
                style="border-bottom: 1px solid #ccc;"
            />
            <Editor
                v-model="data.form.content"
                :defaultConfig="editorConfig"
                :mode="mode"
                style="height: 500px; overflow-y: hidden;"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
      <span>
        <el-button @click="data.viewVisible = false">关 闭</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref, onBeforeUnmount, shallowRef} from "vue";
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'

const data = reactive({
  title: null,
  content: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  selectVisible: false,
  ids: [],
  viewVisible: false,
})

const formRef = ref()

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    data.tableData = res.data.list;
    data.total = res.data.total;
  })
}

const reset = () => {
  data.title = null
  data.pageNum = 1
  data.pageSize = 10
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const handleCoverSuccess = (res) => {
  console.log(res.data);
  data.form.cover = res.data
}

const save = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const add = () => {
  data.formVisible = false
  request.post('/article/add', data.form).then(res => {
    if (res.code === "200") {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  data.formVisible = false
  request.put('/article/update', data.form).then(res => {
    if (res.code === "200") {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm("删除后无法恢复,是否删除?", "确认").then(() => {
    data.form = JSON.parse(JSON.stringify(row))
    del(data.form.id)
    data.form = {}
  })
}

const del = (id) => {
  request.delete('/article/delete/' + id).then(res => {
    if (res.code === "200") {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(row => row.id)
}

const deleteBatch = () => {
  if (data.ids.length === 0) {
    ElMessage("未选中文章")
    return
  }
  ElMessageBox.confirm("删除后无法恢复,是否删除?", "确认").then(() => {
    request.delete('/article/deleteBatch', {data: data.ids}).then(res => {
      if (res.code === "200") {
        ElMessage.success("操作成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

load();

const editorRef = shallowRef()
const mode = 'default'
const editorConfig = {MENU_CONF: {}}
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

const view = (content) =>{
  data.content = content;
  data.viewVisible = true;
}
</script>

<style scoped>
/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .card .el-input {
    width: 150px !important;
  }
  .el-dialog .el-form {
    padding-right: 20px !important;
  }
}
</style>