<template>
  <div>
    <!-- 头部开始 -->
    <div class="header">
      <div class="header-left">
        <img src="../assets/java.svg" alt="" style="width: 30px; height: 30px;" />
        <span class="header-title">后台管理系统</span>
      </div>

      <div style="flex: 1"></div>

      <div class="header-right">
        <img :src="data.user.avatar" alt="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" class="avatar-img" />
        <span class="user-name">{{ data.user.name }}</span>
      </div>
    </div>
    <!-- 头部结束 -->

    <!-- 下面部分开始 -->
    <div class="main-container">

      <!-- 左侧导航开始 -->
      <div class="sidebar">
        <el-menu :default-active="router.currentRoute.value.path" router :default-openeds="['1']"
                 class="sidebar-menu">
          <el-menu-item index="/manager/home">
            <el-icon>
              <House/>
            </el-icon>
            系统首页
          </el-menu-item>

          <el-menu-item v-if="data.user.role === 'admin'" index="/manager/data">
            <el-icon>
              <DataLine/>
            </el-icon>
            数据统计
          </el-menu-item>

          <el-menu-item v-if="data.user.role === 'admin'" index="/manager/article">
            <el-icon>
              <Document/>
            </el-icon>
            文章管理
          </el-menu-item>

          <el-menu-item v-if="data.user.role === 'admin'" index="/manager/department">
            <el-icon>
              <OfficeBuilding/>
            </el-icon>
            部门管理
          </el-menu-item>

          <el-sub-menu v-if="data.user.role === 'admin'" index="1">
            <template #title>
              <el-icon><User/></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/Admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/manager/person">
            <el-icon>
              <UserFilled/>
            </el-icon>
            个人信息
          </el-menu-item>
          <el-menu-item index="/manager/modifyPassword">
            <el-icon>
              <Lock/>
            </el-icon>
            修改密码
          </el-menu-item>
          <el-menu-item @click="logout">
            <el-icon>
              <SwitchButton/>
            </el-icon>
            退出登录
          </el-menu-item>
        </el-menu>
      </div>
      <!-- 左侧导航结束 -->

      <!-- 右侧主体开始 -->
      <div class="main-content">
        <RouterView @updateUser="updateUser"/>
      </div>
      <!-- 右侧主体结束 -->
    </div>
    <!-- 下面部分结束 -->
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "../router/index.js";
import {House, DataLine, Document, OfficeBuilding, User, UserFilled, Lock, SwitchButton} from "@element-plus/icons-vue"

const data = reactive({
  user: JSON.parse(localStorage.getItem("user"))
})

const logout =() => {
  localStorage.removeItem("user");  //清除当前用户账户数据
  location.href = "/login";
}

const updateUser = () =>{
  data.user = JSON.parse(localStorage.getItem("user"));
}
</script>

<style>
.el-menu .is-active {
  background-color: #e6ecf7 !important;
}

.el-menu .is-active > .el-sub-menu__title {
  background-color: white !important;
}

/* ========== 布局样式 ========== */
.header {
  height: 60px;
  background-color: #1677ff;
  width: 100%;
  display: flex;
  align-items: center;
  position: relative;
  z-index: 2;
}

.header-left {
  width: 200px;
  display: flex;
  margin-left: 20px;
  align-items: center;
}

.header-title {
  font-size: 24px;
  color: white;
  margin-left: 5px;
  white-space: nowrap;
}

.header-right {
  width: fit-content;
  display: flex;
  align-items: center;
  padding-right: 10px;
}

.avatar-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-name {
  color: white;
  margin-left: 5px;
  font-size: 18px;
  white-space: nowrap;
}

.main-container {
  display: flex;
}

.sidebar {
  width: 200px;
  background: #fff;
  position: relative;
  z-index: 2;
  flex-shrink: 0;
}

.sidebar-menu {
  border-width: 200px;
  border-right: 1px solid gray;
  min-height: calc(100vh - 60px);
}

.main-content {
  flex: 1;
  width: 0;
  background-color: transparent;
  padding: 10px;
  min-width: 0;
}

/* ========== 响应式 ========== */
@media (max-width: 1024px) {
  .header-title {
    font-size: 18px;
  }
  .user-name {
    font-size: 14px;
  }
  .avatar-img {
    width: 32px;
    height: 32px;
  }
}

@media (max-width: 768px) {
  .header-left {
    width: auto;
    margin-left: 10px;
  }
  .header-title {
    font-size: 16px;
  }

  .sidebar {
    width: 160px;
  }
  .sidebar-menu {
    min-height: calc(100vh - 50px);
  }

  .main-content {
    padding: 6px;
  }
}

@media (max-width: 576px) {
  .main-container {
    flex-direction: column;
  }
  .sidebar {
    width: 100%;
    overflow-x: auto;
  }
  .sidebar-menu {
    min-height: auto;
    border-right: none;
    border-bottom: 1px solid gray;
  }
  .header {
    height: 50px;
  }
  .header-title {
    font-size: 14px;
  }
  .user-name {
    display: none;
  }
  .avatar-img {
    width: 28px;
    height: 28px;
  }
}
</style>