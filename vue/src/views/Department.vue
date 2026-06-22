<template>
  <div>
    <div class="card" style="margin-bottom: 20px">
      <el-input v-model="data.name" :prefix-icon="Search" placeholder="请输入部门名称查询"
                style="width: 200px; margin-right: 5px"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 20px">
      <el-button plain type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="primary" @click="data.selectVisible = !data.selectVisible">批量操作</el-button>
      <el-button v-if="data.selectVisible" type="danger" @click="deleteBatch">删 除</el-button>
    </div>

    <div class="card" style="margin-bottom: 20px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column v-if="data.selectVisible" type="selection" width="50"/>
        <el-table-column v-if="!data.selectVisible" width="50"/>
        <el-table-column label="ID" prop="id" width="80"/>
        <el-table-column label="部门名称" prop="name"/>
        <el-table-column label="操作" width="200">
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

    <el-dialog v-model="data.formVisible" :title="data.form.id ? '编辑部门' : '新增部门'" width="30%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 50px; padding-top:20px">
        <el-form-item label="部门名称" prop="name" label-width="80px">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入部门名称"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: {},
  selectVisible: false,
  ids: [],
  rules: {
    name: [
      {required: true, message: "请输入部门名称", trigger: "blur"}
    ]
  }
})

const formRef = ref()

const load = () => {
  request.get('/department/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data.list;
    data.total = res.data.total;
  })
}

const reset = () => {
  data.name = null
  data.pageNum = 1
  data.pageSize = 10
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
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
  request.post('/department/add', data.form).then(res => {
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
  request.put('/department/update', data.form).then(res => {
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
    del(row.id)
  })
}

const del = (id) => {
  request.delete('/department/delete/' + id).then(res => {
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
    ElMessage("未选中部门")
    return
  }
  ElMessageBox.confirm("删除后无法恢复,是否删除?", "确认").then(() => {
    request.delete('/department/deleteBatch', {data: data.ids}).then(res => {
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
