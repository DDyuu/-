import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', redirect: '/login'},
        {
            path: '/manager', component: () => import('../views/Manager.vue'), children: [
                {path: 'home', name: 'home', meta: {title: "主页"}, component: () => import('../views/Home.vue')},
                {path: 'test', name: 'test', meta: {title: "测试页"}, component: () => import('../views/Test.vue')},
                {path: 'data', name: 'data', meta: {title: "数据"}, component: () => import('../views/Data.vue')},
                {path: 'employee', name: 'employee', meta: {title: "员工信息"}, component: () => import('../views/Employee.vue')},
                {path: 'admin', name: 'admin', meta: {title: "管理员信息"}, component: () => import('../views/Admin.vue')},
                {path: 'person', name: 'person', meta: {title: "个人信息"}, component: () => import('../views/Person.vue')},
                {path: 'modifyPassword', name: 'modifyPassword', meta: {title: "修改密码"}, component: () => import('../views/ModifyPassword.vue')},
                {path: 'article', name: 'article', meta: {title: "发布文章"}, component: () => import('../views/article.vue')},
                {path: 'department', name: 'department', meta: {title: "部门管理"}, component: () => import('../views/Department.vue')},
            ]
        },
        {path: '/login', name: 'login', meta:{title:"登录界面"}, component: () => import('../views/Login.vue')},
        {path: '/register', name: 'register', meta:{title:"注册界面"}, component: () => import('../views/Register.vue')},
        {path: '/404', name: 'not-found', meta:{title:"404-NotFound"}, component: () => import('../views/404.vue')},
        {path: '/:pathMatch(.*)', redirect: '/404'},
    ]
})

// 需要admin权限的路由
const adminRoutes = ['article', 'department', 'admin', 'employee', 'data']

router.beforeEach((to, from, next) => {
    document.title = to.meta.title;
    const user = JSON.parse(localStorage.getItem("user"))
    if (to.path.startsWith('/manager/') && adminRoutes.includes(to.name)) {
        if (!user || user.role !== 'admin') {
            next('/manager/home')
            return
        }
    }
    next()
})

export default router
