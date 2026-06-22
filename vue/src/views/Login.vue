<template>
  <div class="login-container">
      <div class="login-card">
        <el-form ref="formRef" :model="data.form" :rules="data.rules" class="login-form">
          <div style="margin-bottom: 10%;font-size: 250%;font-weight: bold;text-align: center;">
            后台管理系统
          </div>
          <el-form-item prop="account">
            <el-input v-model="data.form.account" class="login-input" placeholder="请输入账号" prefix-icon="User"
                      size="large"/>
          </el-form-item>
          <div style="display: flex"></div>
          <el-form-item prop="password">
            <el-input v-model="data.form.password" class="login-input" placeholder="请输入密码" prefix-icon="Lock"
                      size="large" @keydown.enter.native="login" show-password/>
          </el-form-item>
          <el-form-item prop="role">
            <el-select v-model="data.form.role" size="large">
              <el-option value="admin" label="管理员" />
              <el-option value="employee" label="员工" />
            </el-select>
          </el-form-item>
          <el-form-item style="">
            <el-button class="login-button" size="large" type="primary" @click="login">登 录</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-top: 5%;align-self: flex-end;margin-right: 5%">还没有账号?请<a href="/register">注册</a></div>
      </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js"
import {ElMessage} from "element-plus";

const data = reactive({
  form: {role: "admin"},
  rules: {
    account: [
      {required: true, message: "请输入账号", trigger: "blur"},
    ],
    password: [
      {required: true, message: "请输入密码", trigger: "blur"},
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      request.post("/login", data.form).then(res => {
        if (res.code === "200") {
          localStorage.setItem("user", JSON.stringify(res.data));
          ElMessage.success("登录成功!")
          location.href = "/manager/home"
        } else {
          ElMessage.error(res.msg);
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background-image: url("../assets/background.jpg");
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: row-reverse;
}

.login-card {
  background-color: rgb(255, 255, 255, 0.5);
  animation: fadeIn 1s;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);

  width: 25%;
  min-width: 300px;
  height: 60%;
  margin-top: auto;
  margin-bottom: auto;
  margin-right: 10%;

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

.login-form {
  width: 70%;
  display: flex;
  flex-direction: column;
  gap:2%;
}

.login-input {
  height: 120%;
  padding-bottom: 5%;
}

.login-button {
  width: 100%;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .login-container {
    justify-content: center;
  }
  .login-card {
    width: 85%;
    min-width: unset;
    height: auto;
    margin-right: 0;
    padding: 30px 0;
  }
  .login-form {
    width: 80%;
  }
}

</style>