<template>
  <div class="register-container">
    <div class="register-card">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" class="register-form">
        <div style="margin-bottom: 10%;font-size: 250%;font-weight: bold;text-align: center;">
          注册
        </div>
        <el-form-item prop="account">
          <el-input v-model="data.form.account" class="register-input" placeholder="请输入账号" prefix-icon="User"
                    size="large"/>
        </el-form-item>
        <div style="display: flex"></div>
        <el-form-item prop="password">
          <el-input v-model="data.form.password" class="register-input" placeholder="请输入密码" prefix-icon="Lock"
                    size="large" show-password/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="data.form.confirmPassword" class="register-input" placeholder="请确认密码" prefix-icon="Lock"
                    size="large" @keydown.enter.native="register" show-password/>
        </el-form-item>
        <el-form-item style="">
          <el-button class="register-button" size="large" type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
      <div style="margin-top: 5%;align-self: flex-end;margin-right: 5%">已有账号?请<a href="/login">登录</a></div>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";

const validatePass = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请再次确认密码'))
  }else if(value !== data.form.password){
    callback(new Error('两次输入的密码不一致'))
  }else{
    callback()
  }
}

const data = reactive({
  form: {},
  rules: {
    account: [
      {required: true, message: "请输入账号", trigger: "blur"},
    ],
    password: [
      {required: true, message: "请输入密码", trigger: "blur"},
    ],
    confirmPassword: [
      { validator: validatePass, trigger: "blur" }
    ]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      data.form.id = localStorage.getItem("user.id")
      request.post("/register", data.form).then(res => {
        if (res.code === "200") {
          ElMessage.success("注册成功!")
          location.href = "/login"
        } else {
          ElMessage.error(res.msg);
        }
      })
    }
  })
}
</script>

<style scoped>
.register-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background-image: url("../assets/background.jpg");
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  justify-content: center;
}

.register-card {
  background-color: rgb(255, 255, 255, 0.5);
  animation: fadeIn 1s;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);

  width: 25%;
  min-width: 300px;
  height: 60%;
  margin-top: auto;
  margin-bottom: auto;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.register-form {
  width: 70%;
  display: flex;
  flex-direction: column;
  gap:2%;
}

.register-input {
  height: 120%;
  padding-bottom: 5%;
}

.register-button {
  width: 100%;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .register-container {
    justify-content: center;
    padding: 0 20px;
  }
  .register-card {
    width: 85%;
    min-width: unset;
    height: auto;
    padding: 30px 0;
  }
  .register-form {
    width: 80%;
  }
}

</style>