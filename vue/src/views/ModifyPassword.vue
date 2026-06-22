<template>
  <div class="card">
    <el-form ref="formRef" :model="data.form" :rules="data.rules" >
      <div style="margin-bottom: 10%;font-size: 250%;font-weight: bold;text-align: center;">
        修改密码
      </div>
      <el-form-item prop="oldPassword" label="原密码:" label-width="100px">
        <el-input v-model="data.form.oldPassword"  placeholder="请输入原密码" prefix-icon="Lock"
                  size="large"/>
      </el-form-item>
      <div style="display: flex"></div>
      <el-form-item prop="password" label="新密码:" label-width="100px">
        <el-input v-model="data.form.password"  placeholder="请输入新密码" prefix-icon="Lock"
                  size="large" show-password/>
      </el-form-item>
      <el-form-item prop="confirmNewPassword" label="确认新密码:" label-width="100px">
        <el-input v-model="data.form.confirmNewPassword"  placeholder="请确认新密码" prefix-icon="Lock"
                  size="large" @keydown.enter.native="modify" show-password/>
      </el-form-item>
      <div style="text-align: center">
        <el-button  size="large" type="primary" @click="modify" class="button">修改</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";

const formRef = ref()

const validatePass = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请再次确认密码'))
  }else if(value !== data.form.password){
    callback(new Error('两次输入的新密码不一致'))
  }else{
    callback()
  }
}

const data = reactive({
  user: JSON.parse(localStorage.getItem("user")),
  form: {},
  rules: {
    oldPassword: [
      {required: true, message: "请输入原密码", trigger: "blur"},
    ],
    password: [
      {required: true, message: "请输入新密码", trigger: "blur"},
    ],
    confirmNewPassword: [
      { validator: validatePass, trigger: "blur" }
    ]
  }
})

const modify = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id = data.user.id;
      data.form.role = data.user.role;
      request.put("/updatePassword", data.form).then(res => {
        if (res.code === "200") {
          ElMessage.success("修改成功")
          localStorage.removeItem("user")
          setTimeout(() => {
            location.href = "/login"
          },500)
        }else{
          ElMessage.error(res.msg);
        }
      })
    }
  })
}
</script>

<style scoped>

.card {
  width: 50%
}

.button{
  padding: 20px 30px;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .card {
    width: 100%;
  }
  .button {
    padding: 15px 20px;
  }
}
</style>