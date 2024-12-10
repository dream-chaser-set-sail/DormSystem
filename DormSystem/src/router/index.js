import {createRouter, createWebHashHistory} from "vue-router";

// 引入一个一个可能要呈现组件
import Layout from '@/views/Layout.vue'
import Login from '@/views/Login.vue'
import Index from "@/views/Index.vue";
import Student from "@/views/Student.vue";
import Dormitories from "@/views/Dormitories.vue";
import Supervisor from "@/views/Supervisor.vue";
import Move from "@/views/Move.vue";
import Repair from "@/views/Repair.vue";
import Admin from "@/views/Admin.vue";
import DormitoriesAdmin from "@/views/DormitoriesAdmin.vue";
import Notice from "@/views/Notice.vue";
import StudentSupervisor from "@/views/StudentSupervisor.vue";
import MyDorm from "@/views/MyDorm.vue";
import WillMove from "@/views/WillMove.vue";
import WillRepair from "@/views/WillRepair.vue";

export const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {path: '/login', component: Login},
        {
            path: '/', component: Layout, children: [
                {path: '', redirect: '/index'}, // 重定向到index, 当已登陆的用户访问URL: localhost:5173 时, 路径会重定向到index展示
                {path: '/index', component: Index},
                {path: '/student', component: Student},
                {path: '/dormitoryAdmin', component: DormitoriesAdmin},
                {path: '/dormitory', component: Dormitories},
                {path: '/supervisor', component: Supervisor},
                {path: '/move', component: Move},
                {path: '/repair', component: Repair},
                {path: '/admin', component: Admin},
                {path: '/notice', component: Notice},
                {path: '/studentSupervisor', component: StudentSupervisor},
                {path: '/myDorm', component: MyDorm},
                {path: '/willMove', component: WillMove},
                {path: '/willRepair', component: WillRepair},
            ]
        }
    ]
})