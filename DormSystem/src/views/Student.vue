<script setup>
import {
    Delete,
    Edit,
    Plus
} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {studentapi} from "@/api/student.js";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from '@/store/token.js'

const token = useTokenStore()
const tatal = ref(0)
const tableData = ref([])
const studentQuery = reactive({
    name : '',
    page : 1,
    limit : 5
})
const form = ref({})
const dialogFormVisible = ref(false)
const title = ref("")
const isAddorUpdata = ref(0)
const ids = ref([])
const college = ref({})
const major = ref({})
const headers = ref({
    // 携带token传递到后台
    Authorization : token.token
})

function loadData(){
    studentapi.list(studentQuery).then(function (obj){
        console.log(obj.data);
        tableData.value = obj.data
        tatal.value = obj.count
    })
}

function handleDelete(val){
    studentapi.deleteById(val).then(function (obj){
        myUtils.open(obj.code, obj.msg)
        loadData()
    })
}

function showAddDialog(){
    dialogFormVisible.value = true
    form.value = {}
    isAddorUpdata.value = 2
    title.value = "添加信息"
}

function handleEdit(val){
    dialogFormVisible.value = true
    studentapi.selectById(val).then(function (obj){
        title.value = "修改信息"
        isAddorUpdata.value = 1
        form.value = obj.dataClass
    })
}

function submitUser(){
    switch (isAddorUpdata.value){
        case 1:
            studentapi.update(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
        case 2:
            studentapi.add(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
    }
}

function onSearch(){
    studentQuery.page = 1
    loadData()
}

function handleSelectionChange(rows){
    ids.value = rows.map(row => row.id)
}

function deleteAll(){
    myUtils.openWindow("批量删除", "你确定要删除吗？", function (){
        studentapi.deleteAll(ids.value).then(function (obj){
            myUtils.open(obj.code, obj.msg)
            loadData()
        })
    })
}

function selCollege(){
    studentapi.selCollege().then(function (obj){
        console.log(obj.data);
        college.value = obj.data
    })
}

function collegeChange(collegeId) {
    if (collegeId) {
        console.log(collegeId);
        studentapi.selMajor(collegeId).then(function (obj) {
            major.value = obj.data
        })
    }else {
        major.value = null;
    }
}

function handleAvatarSuccess(obj) {
    form.value.image = obj.imgName
}

selCollege()
loadData()
</script>

<template>
    <el-card>
        <template #header>
            <div class="header">
                <el-button type="primary" @click="showAddDialog" plain>添加</el-button>
                <el-button type="danger" @click="deleteAll" plain>批量删除</el-button>
            </div>
        </template>
        <el-form :inline="true">
            <el-form-item label="名字" style="width: 200px">
                <el-input v-model="studentQuery.name" placeholder="请输入名字" clearable/>
            </el-form-item>
            <el-form-item label="学院">
                <el-select
                    v-model="studentQuery.college"
                    placeholder="选择学院"
                    clearable
                    style="width: 110px"
                    @change="collegeChange(studentQuery.college)"
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
                    v-model="studentQuery.major"
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
                        v-model="studentQuery.createTime"
                        type="datetime"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        placeholder="开始时间"
                />
                <el-date-picker
                        v-model="studentQuery.updateTime"
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
            <el-table-column align="center" header-align = 'center' width="180" prop="createTime" label="时间" />
            <el-table-column align="center" header-align = 'center' fixed="right" label="操作" width="120">
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
            </el-table-column>
        </el-table>
        <!--    分页-->
        <el-pagination
                background
                :total="tatal"
                v-model:current-page="studentQuery.page"
                v-model:page-size="studentQuery.limit"
                :page-sizes="[5, 10, 15, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                style="margin-top: 20px"
                @change="loadData"
        />
        <!--    添加/修改弹出框-->
        <el-dialog v-model="dialogFormVisible" :title=title width="500">
            <el-form :model="form">
                <el-form-item label="姓名" :label-width="formLabelWidth">
                    <el-input v-model="form.name" autocomplete="off" />
                </el-form-item>
                <el-form-item label="性别" :label-width="formLabelWidth">
                    <el-radio-group v-model="form.gender">
                        <el-radio value="男">男</el-radio>
                        <el-radio value="女">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="年龄" :label-width="formLabelWidth">
                    <el-input v-model="form.age" autocomplete="off" />
                </el-form-item>
                <el-form-item label="邮箱" :label-width="formLabelWidth">
                    <el-input v-model="form.email" autocomplete="off" />
                </el-form-item>
                <el-form-item label="学院">
                    <el-select
                        v-model="form.college"
                        placeholder="选择学院"
                        clearable
                        style="width: 110px"
                        @change="collegeChange(form.college)"
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
                        v-model="form.major"
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
                <el-form-item lobal="头像" :label-width="formLabelWidth">
                    <el-upload
                            class="avatar-uploader"
                            action="/api/upload"
                            :show-file-list="false"
                            :on-success="handleAvatarSuccess"
                            :headers="headers"
                    >
                        <!--通过headers把action的路径上拼上token传到后台 ↑ -->

                        <img v-if="form.image" :src="form.image" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon"><Plus/></el-icon>
                    </el-upload>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary" @click="submitUser(row)">
                        确定
                    </el-button>
                </div>
            </template>
        </el-dialog>
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