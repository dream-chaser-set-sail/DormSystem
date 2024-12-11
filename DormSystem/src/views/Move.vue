<script setup>
import {
    Delete,
    Check,
    Close,
    Edit,
    Plus
} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {moveapi} from "@/api/move.js";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from '@/store/token.js'
import {adminInfoStore} from "@/store/adminInfo.js";
import {moveInfoStore} from "@/store/move.js";

const token = useTokenStore()
const tatal = ref(0)
const tableData = ref([])
const moveQuery = reactive({
    name : '',
    page : 1,
    limit : 5
})
const form = ref({})
const dialogFormVisible = ref(false)
const title = ref("")
const isAddorUpdata = ref(0)
const ids = ref([])
const moveDorm = ref({})
const moveName = ref({})

function loadData(){
    moveapi.list(moveQuery).then(function (obj){
        tableData.value = obj.data
        tatal.value = obj.count
    })
}

function handleDelete(val){
    moveapi.deleteById(val).then(function (obj){
        myUtils.open(obj.code, obj.msg)
        loadData()
    })
}

function showAddDialog(){
    dialogFormVisible.value = true
    form.value = {}
    isAddorUpdata.value = 2
    title.value = "添加迁出记录"
}

function handleEdit(val){
    moveapi.selectById(val).then(function (obj){
        isAddorUpdata.value = 1
        form.value = obj.dataClass
        submitUser()
    })
}

function handleEditNo(val) {
    moveapi.selectById(val).then(function (obj){
        isAddorUpdata.value = 3
        form.value = obj.dataClass
        submitUser()
    })
}

function submitUser(){
    switch (isAddorUpdata.value){
        case 1:
            moveapi.update(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                loadData()
            })
            break
        case 2:
            form.value.account = adminInfoStore().admin.account
            moveapi.add(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
        case 3:
            moveapi.updateByNo(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                loadData()
            })
            break
    }
}

function onSearch(){
    moveQuery.page = 1
    loadData()
}

function handleSelectionChange(rows){
    ids.value = rows.map(row => row.id)
}

function deleteAll(){
    myUtils.openWindow("批量删除", "你确定要删除吗？", function (){
        moveapi.deleteAll(ids.value).then(function (obj){
            myUtils.open(obj.code, obj.msg)
            loadData()
        })
    })
}

function selectByDorm(){
    moveapi.selectByDorm().then(function (obj){
        moveDorm.value = obj.data
    })
}

function selectByName(){
    moveapi.selectByName().then(function (obj){
        moveName.value = obj.data
    })
}

selectByName()
selectByDorm()
loadData()
</script>

<template>
    <el-card>
        <template #header>
            <div class="header">
                <el-button type="primary" @click="showAddDialog" plain>添加迁出记录</el-button>
                <el-button type="danger" @click="deleteAll" plain>批量删除</el-button>
            </div>
        </template>
        <el-form :inline="true">
            <el-form-item label="姓名" style="width: 200px">
                <el-input v-model="moveQuery.name" placeholder="请输入姓名" clearable/>
            </el-form-item>
            <el-form-item label="更新时间">
                <el-date-picker
                    v-model="moveQuery.createTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="moveQuery.updateTime"
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
            <el-table-column fixed prop="id" label="ID" />
            <el-table-column align="center" header-align = 'center' prop="name" label="学生姓名" />
            <el-table-column align="center" header-align = 'center' prop="beforeDormName" label="迁出宿舍">
                <template v-slot="scope">
                    {{ scope.row.beforeDormName === null ? '未分配宿舍' : scope.row.beforeDormName }}
                </template>
            </el-table-column>
            <el-table-column align="center" header-align = 'center' prop="moveDormName" label="迁入宿舍">
                <template v-slot="scope">
                    {{ scope.row.moveDormName === null ? '未分配宿舍' : scope.row.moveDormName }}
                </template>
            </el-table-column>
            <el-table-column align="center" header-align = 'center' prop="createTime" label="申请时间" />
            <el-table-column align="center" header-align = 'center' prop="status" label="状态" />
            <el-table-column align="center" header-align = 'center' fixed="right" label="操作">
                <template #default="{ row }">
                    <el-popconfirm
                            @confirm="handleDelete(row.id)"
                            width="160"
                            title="你确定要删除吗？">
                        <template #reference>
                            <el-button type="danger" :icon="Delete" circle />
                        </template>
                    </el-popconfirm>
                    <el-button type="primary" @click="handleEdit(row.id)" :icon="Check" circle />
                    <el-button type="warning" @click="handleEditNo(row.id)" :icon="Close" circle />
                </template>
            </el-table-column>
        </el-table>
        <!--    分页-->
        <el-pagination
                background
                :total="tatal"
                v-model:current-page="moveQuery.page"
                v-model:page-size="moveQuery.limit"
                :page-sizes="[5, 10, 15, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                style="margin-top: 20px"
                @change="loadData"
        />
        <!--    添加/修改弹出框-->
        <el-dialog v-model="dialogFormVisible" :title=title width="500">
            <el-form :model="form">
                <el-form-item label="学生姓名">
                    <el-select
                        v-model="form.name"
                        placeholder="选择学生"
                        clearable
                    >
                        <el-option
                            v-for="item in moveName"
                            :key="item.id"
                            :label="item.name"
                            :value="item.name"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="迁入宿舍">
                    <el-select
                        v-model="form.moveDormId"
                        placeholder="选择宿舍"
                        clearable
                    >
                        <el-option
                            v-for="item in moveDorm"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
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