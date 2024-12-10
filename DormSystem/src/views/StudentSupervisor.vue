<script setup>
import {reactive, ref} from "vue";
import {studentSupervisorApi} from "@/api/studentSupervisor.js";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from '@/store/token.js'
import {adminapi} from "@/api/admin.js";
import {adminInfoStore} from "@/store/adminInfo.js";

const token = useTokenStore()
const tatal = ref(0)
const tableData = ref([])
const studentSupervisorQuery = reactive({
    account: '',
    name : '',
    page : 1,
    limit : 10
})
const form = ref({})
const title = ref("")
const ids = ref([])
const college = ref({})
const major = ref({})
const adminInfo = adminInfoStore()

function loadData(){
    studentSupervisorApi.list(studentSupervisorQuery).then(function (obj){
        console.log(obj.data);
        tableData.value = obj.data
        tatal.value = obj.count
    })
}

function onSearch(){
    studentSupervisorQuery.page = 1
    loadData()
}

function handleSelectionChange(rows){
    ids.value = rows.map(row => row.id)
}

function deleteAll(){
    myUtils.openWindow("批量删除", "你确定要删除吗？", function (){
        studentSupervisorApi.deleteAll(ids.value).then(function (obj){
            myUtils.open(obj.code, obj.msg)
            loadData()
        })
    })
}

function selCollege(){
    studentSupervisorApi.selCollege().then(function (obj){
        console.log(obj.data);
        college.value = obj.data
    })
}

function collegeChange(collegeId) {
    if (collegeId) {
        console.log(collegeId);
        studentSupervisorApi.selMajor(collegeId).then(function (obj) {
            major.value = obj.data
        })
    }else {
        major.value = null;
    }
}

function setAccount() {
    studentSupervisorQuery.account = adminInfo.admin.account
}

setAccount()
selCollege()
loadData()
</script>

<template>
    <el-card>
        <el-form :inline="true">
            <el-form-item label="名字" style="width: 200px">
                <el-input v-model="studentSupervisorQuery.name" placeholder="请输入名字" clearable/>
            </el-form-item>
            <el-form-item label="学院">
                <el-select
                    v-model="studentSupervisorQuery.college"
                    placeholder="选择学院"
                    clearable
                    style="width: 110px"
                    @change="collegeChange(studentSupervisorQuery.college)"
                >
                    <el-option
                        v-for="item in college"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="专业">
                <el-select
                    v-model="studentSupervisorQuery.major"
                    placeholder="选择专业"
                    clearable
                    style="width: 110px"
                >
                    <el-option
                        v-for="item in major"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="更新时间">
                <el-date-picker
                        v-model="studentSupervisorQuery.createTime"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="开始时间"
                />
                <el-date-picker
                        v-model="studentSupervisorQuery.updateTime"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="结束时间"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="tableData" ref="multipleTableRef" @selection-change="handleSelectionChange" style="width: 100%">
            <el-table-column type="selection" :selectable="selectable" width="55"/>
            <el-table-column fixed prop="account" label="账号" width="125"/>
            <el-table-column align="center" header-align = 'center' prop="image" label="头像" >
                <template #default="scope">
                    <img :src="scope.row.image" style="max-height: 40px; max-width: 120px">
                </template>
            </el-table-column>
            <el-table-column align="center" header-align = 'center' prop="name" label="姓名" />
            <el-table-column align="center" header-align = 'center' prop="gender" label="性别" />
            <el-table-column align="center" header-align = 'center' prop="age" label="年龄" />
            <el-table-column align="center" header-align = 'center' prop="email" label="邮箱" />
            <el-table-column align="center" header-align = 'center' prop="collegeName" label="学院" />
            <el-table-column align="center" header-align = 'center' prop="majorName" label="专业" />
            <el-table-column align="center" header-align = 'center' prop="dormId" label="宿舍">
                <template v-slot="scope">
                    {{ scope.row.dormName === null ? '未分配宿舍' : scope.row.dormName }}
                </template>
            </el-table-column>
            <el-table-column fixed="right" align="center" header-align = 'center' width="180" prop="createTime" label="时间" />
<!--            <el-table-column align="center" header-align = 'center' fixed="right" label="操作" width="120">
                <template #default="{ row }">
                    <el-popconfirm
                            @confirm="handleDelete(row.id)"
                            width="160"
                            title="你确定要删除吗？">
                        <template #reference>
                            <el-button type="danger" :icon="Delete" circle />
                        </template>
                    </el-popconfirm>
                    <el-button type="primary" @click="handleEdit(row.id)" :icon="Edit" circle />
                </template>
            </el-table-column>-->
        </el-table>
        <!--    分页-->
        <el-pagination
                background
                :total="tatal"
                v-model:current-page="studentSupervisorQuery.page"
                v-model:page-size="studentSupervisorQuery.limit"
                :page-sizes="[5, 10, 15, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                style="margin-top: 20px"
                @change="loadData"
        />
    </el-card>
</template>

<style>
.avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>

<style scoped>
.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
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
    width: 178px;
    height: 178px;
    text-align: center;
}
.block {
    padding: 30px 0;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
    flex: 1;
}
.block:last-child {
    border-right: none;
}
.block .demonstration {
    display: block;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    margin-bottom: 20px;
}
</style>