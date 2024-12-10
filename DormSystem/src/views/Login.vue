<script setup>
import {User, Lock, PriceTag} from '@element-plus/icons-vue'
import {reactive, ref} from 'vue'
import {adminapi} from "@/api/admin.js";
import {myUtils} from "@/utils/myUtils.js";
import {router} from '@/router/index.js'
import {useTokenStore} from "@/store/token.js";
import {kepchaApi} from "@/api/kepcha.js";

const remember = ref(false)
const url = ref()
const routers = router
const token = useTokenStore()
const form = ref({
    account: '',
    name: '',
    password: '',
    code: ''
})
const rules = ref({
    name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 4, message: '密码长度最小是4位', trigger: 'blur' },
        { pattern: /^\d+$/, message: '密码必须由数字组成', trigger: 'blur' },
    ]
})

function login(){
    adminapi.login(form.value).then(function (obj){
        myUtils.open(obj.code, obj.msg)
        if (obj.code == 0){
            token.setToken(obj.dataClass, remember.value)
            routers.push('/index')
        }else {
            captcha()
        }
    })
}

function captcha() {
    kepchaApi.captcha().then(function (obj){
        url.value = obj
    })
}

captcha()
</script>

<template>
    <div class="body-login">
        <el-card style="width: 70%">
            <div class="login-body">
                <div style="width: 500px; height: 500px; flex: 1.5">
                    <img src="../assets/img/LDorm.png" style="width: 100%; height: 100%; object-fit: cover;">
                </div>
                <div style="flex: 1;">
                    <el-card style="width: 100%; height: 100%" shadow="none">
                        <template #header>
                            <div style="text-align: center; font-size: 25px">
                                <span style="font-family: 黑体">宿舍管理系统</span>
                            </div>
                        </template>
                        <el-form class="form-login" ref="formRef" size="large" autocomplete="off" :model="form" :rules="rules">
                            <el-form-item prop="account">
                                <el-input :prefix-icon="User" placeholder="请输入账号" v-model="form.account"></el-input>
                            </el-form-item>
                            <el-form-item prop="name">
                                <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="form.name"></el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                                          v-model="form.password"></el-input>
                            </el-form-item>
                            <el-form-item prop="code">
                                <el-input name="code" :prefix-icon="PriceTag" style="width: 210px;" type="text" placeholder="请输入验证码"
                                          v-model="form.code"></el-input>
                                <el-image style="width: 140px; height: 40px" :src="url"/>
                            </el-form-item>
                            <el-form-item class="flex">
                                <div class="flex" style="flex: 1">
                                    <el-checkbox v-model="remember">记住我</el-checkbox>
                                </div>
                            </el-form-item>
                            <el-form-item>
                                <el-button class="button" type="primary" auto-insert-space @click="login" style="width: 100%">登录</el-button>
                            </el-form-item>
                        </el-form>
                    </el-card>
                </div>
            </div>
        </el-card>
    </div>


</template>

<style scoped>
.body-login {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    margin-top: 100px;
}
.login-body {
    display: flex;
}
</style>