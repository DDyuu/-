<template>
  <div class="card">
    <el-form ref="formRef" :model="data.form" :rules="data.rules" style="padding-right: 50px; padding-top:20px">
      <div style="margin-bottom:20px; text-align: center;">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/files/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </div>
      <el-form-item label="账号" label-width="80px" prop="account">
        <el-input v-model="data.form.account" autocomplete="off" disabled>请输入账号</el-input>
      </el-form-item>
      <el-form-item label="姓名" label-width="80px" prop="name">
        <el-input v-model="data.form.name" autocomplete="off">请输入姓名</el-input>
      </el-form-item>
      <div v-if="data.form.role !== 'admin'">
      <el-form-item label="性别" label-width="80px">
        <el-radio-group v-model="data.form.sex">
          <el-radio value="male">男</el-radio>
          <el-radio value="female">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="工号" label-width="80px" prop="no">
        <el-input v-model="data.form.no" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="年龄" label-width="80px">
        <el-input-number v-model="data.form.age" autocomplete="off" min="18"></el-input-number>
      </el-form-item>
      <el-form-item label="职位" label-width="80px">
        <el-input v-model="data.form.description" autocomplete="off"></el-input>
      </el-form-item>
      </div>
      <div style="text-align: center">
        <el-button type="primary" class="button" @click="updateUser">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue"
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem("user")),
  form: {},
  rules: {
    account: [
      {required: true, message: "请输入账号", trigger: "blur"}
    ],
    name: [
      {required: true, message: "请输入姓名", trigger: "blur"}
    ],
    no: [
      {required: true, message: "请输入工号", trigger: "blur"}
    ]
  }
})

const handleAvatarSuccess = (res) => {
  console.log(res.data);
  data.form.avatar = res.data
}

const emit = defineEmits(['updateUser'])

if(data.user.role === "admin") {
  data.form = data.user
}else{
  request.get("/employee/selectById",{params:{id:data.user.id}}).then(res => {
    data.form = res.data
  })
}

const updateUser = () => {
  if(data.form.role === "employee") {
    request.put('employee/update', data.form).then(res => {
      if (res.code === "200") {
        ElMessage.success('更新成功')
        localStorage.setItem("user", JSON.stringify(res.data))
        emit('updateUser')
      }
    })
  }else {
    request.put('admin/update', data.form).then(res => {
      if (res.code === "200") {
        ElMessage.success('更新成功')
        localStorage.setItem("user", JSON.stringify(res.data))
        emit('updateUser')
      }
    })
  }
}
</script>

<style scoped>
.card {
  width: 50%;
  padding: 20px 40px;
}

.button{
  padding: 20px 30px;
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .card {
    width: 100%;
    padding: 15px;
  }
  .button {
    padding: 15px 20px;
  }
}
</style>

<style >
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
