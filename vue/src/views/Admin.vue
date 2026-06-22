<template>
  <div>
    <div class="card" style="margin-bottom: 20px">
      <el-input v-model="data.name" :prefix-icon="Search" placeholder="请输入名称查询"
                style="width: 200px; margin-right: 5px"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 20px">
      <el-button plain type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="primary" @click="data.selectVisible = !data.selectVisible">批量操作</el-button>
      <el-button v-if="data.selectVisible" type="danger" @click="deleteBatch">删 除</el-button>
      <!--      <el-button type="info">导 入</el-button>-->
      <!--      <el-button type="success">导 出</el-button>-->
    </div>

    <div class="card" style="margin-bottom: 20px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column v-if="data.selectVisible" type="selection" width="50"/>
        <el-table-column v-if="!data.selectVisible" width="50"/>
        <el-table-column label="名称" prop="name"/>
        <el-table-column label="头像" >
          <template v-slot="scope" >
            <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="width: 40px; height: 40px;border-radius: 50%" />
          </template>
        </el-table-column>
        <el-table-column label="账号" prop="account"/>
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

    <el-dialog v-model="data.formVisible" title="新增管理员信息" width="30%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 50px; padding-top:20px">
        <el-form-item label="账号" prop="account" label-width="80px">
        <el-input :disabled="data.form.id" v-model="data.form.account" autocomplete="off">请输入账号</el-input>
        </el-form-item>
        <el-form-item label-width="80px">
          <el-upload
              action="http://localhost:8080/files/upload"
              :show-file-list="true"
              :on-success="handleAvatarSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="姓名" prop="name" label-width="80px">
          <el-input v-model="data.form.name" autocomplete="off">请输入姓名</el-input>
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
    account: [
      {required: true, message: "请输入账号", trigger: "blur"}
    ],
    name: [
      {required: true, message: "请输入姓名", trigger: "blur"}
    ],
  }
})

const formRef = ref()

const load = () => {
  request.get('/admin/selectPage', {
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

const handleAvatarSuccess = (res) => {
  console.log(res.data);
  data.form.avatar = res.data
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
  request.post('/admin/add', data.form).then(res => {
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
  request.put('/admin/update', data.form).then(res => {
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
  request.delete('/admin/delete/' + id).then(res => {
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
    ElMessage("未选中员工")
    return
  }
  ElMessageBox.confirm("删除后无法恢复,是否删除?", "确认").then(() => {
    request.delete('/admin/deleteBatch', {data: data.ids}).then(res => {
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