<template>
    <el-container style="height: 100vh;">
        <!-- 侧边栏 -->
        <el-aside width="200px" class="sidebar">
            <div class="sidebar-title">宿舍管理系统</div>
            <el-menu default-active="/index" class="menu" router>
                <el-menu-item index="/index"><el-icon><House/></el-icon>主页</el-menu-item>
                <el-menu-item index="/admin" v-if="role === 3"><el-icon><UserFilled/></el-icon>管理员管理</el-menu-item>
                <el-menu-item index="/student" v-if="role === 3"><el-icon><UserFilled/></el-icon>学生管理</el-menu-item>
                <el-menu-item index="/studentSupervisor" v-if="role === 2"><el-icon><UserFilled/></el-icon>学生管理</el-menu-item>
                <el-menu-item index="/dormitoryAdmin" v-if="role === 3"><el-icon><HomeFilled/></el-icon>宿舍管理</el-menu-item>
                <el-menu-item index="/dormitory" v-if="role === 2"><el-icon><HomeFilled/></el-icon>宿舍管理</el-menu-item>
                <el-menu-item index="/supervisor" v-if="role === 3"><el-icon><Avatar/></el-icon>宿管管理</el-menu-item>
                <el-menu-item index="/move"v-if="role === 3 || role === 2"><el-icon><Stamp/></el-icon>学生迁出管理</el-menu-item>
                <el-menu-item index="/repair" v-if="role === 3 || role === 2"><el-icon><Setting/></el-icon>宿舍报修管理</el-menu-item>
                <el-menu-item index="/notice" v-if="role === 3"><el-icon><Bell/></el-icon>公告管理</el-menu-item>
                <el-menu-item index="/myDorm" v-if="role === 1"><el-icon><HomeFilled/></el-icon>我的宿舍</el-menu-item>
                <el-menu-item index="/willMove" v-if="role === 1"><el-icon><Stamp/></el-icon>我要迁宿</el-menu-item>
                <el-menu-item index="/willRepair" v-if="role === 1"><el-icon><Setting/></el-icon>我要报修</el-menu-item>
            </el-menu>
        </el-aside>

        <!-- 主体内容 -->
        <el-container>
            <!-- 头部 -->
            <el-header class="header">
                <div style="display: flex; justify-content: flex-end; align-items: center; height: 100%;">
                    <div>
                        <el-dropdown @command="handleCommand">
                            <span class="el-dropdown-link">
                                <el-tag style="margin-right: 5px; margin-bottom: 1px">{{ adminInfo.admin.name }}</el-tag>
                                <el-avatar size="40" :src="adminInfo.admin.image ? adminInfo.admin.image : avatar"/>
                            </span>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="profile" :icon="User">个人资料</el-dropdown-item>
                                    <el-dropdown-item command="changePass" :icon="Compass">修改密码</el-dropdown-item>
                                    <el-dropdown-item command="logout" :icon="SwitchButton">注销</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </div>
            </el-header>

            <!-- 主体内容区域 -->
            <el-main>
                <!-- 这里可以添加更多的内容或组件 -->
                <router-view></router-view>
            </el-main>
            <el-footer style="width: 100%; height: 7%; background-color: #409EFF7F; text-align: center">Footer</el-footer>
        </el-container>
    </el-container>

    <!--	修改个人信息弹出框-->
    <el-dialog v-model="dialogUserInfoVisible" title="设置个人信息" width="500">
        <el-form :model="user">
            <el-form-item label="姓名" :label-width="formLabelWidth">
                <el-input v-model="user.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="邮箱" :label-width="formLabelWidth">
                <el-input v-model="user.email" autocomplete="off" />
            </el-form-item>
            <el-form-item lobal="头像" :label-width="formLabelWidth">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers"
                >
                    <!--通过headers把action的路径上拼上token传到后台 ↑ -->

                    <img v-if="user.image" :src="user.image" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogUserInfoVisible = false">取消</el-button>
                <el-button type="primary" @click="submitUser(row)">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

    <!--	修改密码弹出框-->
    <el-dialog v-model="dialogUserPassVisible" title="修改密码" width="500">
        <el-form :model="userPass" :rules="rules" ref="form">
            <el-form-item prop="oldPass" label="原密码" :label-width="formLabelWidth">
                <el-input v-model="userPass.oldPass" autocomplete="off" />
            </el-form-item>
            <el-form-item prop="newPass" label="新密码" :label-width="formLabelWidth">
                <el-input v-model="userPass.newPass" autocomplete="off" />
            </el-form-item>
            <el-form-item prop="reNewPass" label="重复密码" :label-width="formLabelWidth">
                <el-input v-model="userPass.reNewPass" autocomplete="off" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogUserPassVisible = false">取消</el-button>
                <el-button type="primary" @click="submitUserPass(form)">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

</template>

<script setup>
import {
    House,
    User,
    UserFilled,
    HomeFilled,
    SwitchButton,
    Avatar,
    Stamp,
    Setting,
    Compass,
    Bell,
    CaretBottom,
    BellFilled,
    Comment
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import {router} from "@/router/index.js";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from "@/store/token.js"
import {adminInfoStore} from '@/store/adminInfo.js'
import {adminapi} from "@/api/admin.js";

const role = ref()
const routers = router;
const form = ref()
const user = ref({})
const userPass = ref({})
const token = useTokenStore()
const headers = ref({
    Authorization : token.token
})
const dialogUserPassVisible = ref(false)
const dialogUserInfoVisible = ref(false)
const adminInfo = adminInfoStore()
const rules = ref({
    oldPass:[
        {required: true, message: '请输入原密码', trigger: 'blur'},
    ],

    newPass:[
        { min: 4, message: '密码长度最小是4位', trigger: 'blur' },
        {validator: NewPassValid, trigger: 'blur'}
    ],

    reNewPass:[
        { min: 4, message: '密码长度最小是4位', trigger: 'blur' },
        {validator: reNewPassValid, trigger: 'blur'}
    ]
})

// 处理下拉菜单的命令
function handleCommand(command) {
    if (command === 'profile') {
        Object.assign(user.value, adminInfo.admin)
        dialogUserInfoVisible.value = true
    } else if (command === 'logout') {
        adminInfo.removeAdminInfo()
        token.removeToken()
        routers.push('/login')
    } else if (command === 'changePass') {
        userPass.value = {}
        dialogUserPassVisible.value = true
    } else {
        routers.push('/admin/' + command)
    }
}

function getUserInfo(){
    adminapi.loginInfo().then(function (obj){
        adminInfo.setAdminInfo(obj.dataClass)
        ifAuthority(obj.dataClass.account)
    })
}

function handleAvatarSuccess(obj){
    user.value.image = obj.imgName
}

function submitUser(){
    adminapi.update(user.value).then(function (obj) {
        myUtils.open(obj.code, obj.msg)
        dialogUserInfoVisible.value = false
        location.reload()
        getUserInfo()
    })
}

function reNewPassValid(rule, value, callback){
    if (value === ''){
        callback(new Error('请输入密码'))
    }else if (value !== userPass.value.newPass){
        callback(new Error('两次输入的密码不一致'))
    }else {
        callback()
    }
}

function NewPassValid(rule, value, callback){
    if (value === ''){
        callback(new Error('请输入新密码'))
    } else if (value === userPass.value.oldPass){
        callback(new Error('新密码不能和原密码重复'))
    } else {
        callback()
    }
}

function submitUserPass(form){
    form.validate((valid) => {
        if (valid) {
            adminapi.updatePass(userPass.value.oldPass, userPass.value.newPass).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                if (obj.code == 0){
                    dialogUserPassVisible.value = false;
                    location.reload()
                }
            })
        } else {
            myUtils.open(1, "验证失败")
        }
    })
}

function ifAuthority(account) {
    let roleNum = account.substring(0,3);
    switch (roleNum) {
        case 'STU':
            role.value = 1
            break
        case 'SRA':
            role.value = 2
            break
        case 'ADM':
            role.value = 3
            break
        default:
            myUtils.open(1, '身份有误，请重试')
            break
    }

    console.log(role.value);
}

getUserInfo()
</script>

<style scoped>
/* 蓝白风格样式 */
.sidebar {
    background-color: #f0f4ff;
    border-right: 1px solid #e4e7ed;
}

.sidebar-title {
    padding: 20px;
    font-size: 20px;
    font-weight: bold;
    color: #2c3e50;
}

.menu {
    background-color: #f0f4ff;
}

.menu .el-menu-item:hover {
    background-color: #e6f7ff;
}

.header {
    background-color: rgba(64, 158, 255, 0.5);
    color: white;
}

.el-main {
    padding: 10px;
    background-color: #ffffff;
}
</style>
