<script setup>
import {
    Delete,
    Edit,
    Plus
} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {dormapi} from "@/api/doemitories.js";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from '@/store/token.js'

const token = useTokenStore()
const tatal = ref(0)
const tableData = ref([])
const dormQuery = reactive({
    name : '',
    page : 1,
    limit : 5
})
const form = ref({})
const dialogFormVisible = ref(false)
const title = ref("")
const isAddorUpdata = ref(0)
const ids = ref([])

function loadData(){
    dormapi.list(dormQuery).then(function (obj){
        tableData.value = obj.data
        tatal.value = obj.count
    })
}

function handleDelete(val){
    dormapi.deleteById(val).then(function (obj){
        myUtils.open(obj.code, obj.msg)
        loadData()
    })
}

function showAddDialog(){
    dialogFormVisible.value = true
    form.value = {}
    isAddorUpdata.value = 2
    title.value = "添加宿舍"
}

function handleEdit(val){
    dialogFormVisible.value = true
    dormapi.selectById(val).then(function (obj){
        title.value = "修改宿舍"
        isAddorUpdata.value = 1
        form.value = obj.dataClass
    })
}

function submitUser(){
    switch (isAddorUpdata.value){
        case 1:
            dormapi.update(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false

                loadData()
            })
            break
        case 2:
            dormapi.add(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
    }
}

function onSearch(){
    dormQuery.page = 1
    loadData()
}

function handleSelectionChange(rows){
    ids.value = rows.map(row => row.id)
}

function deleteAll(){
    myUtils.openWindow("批量删除", "你确定要删除吗？", function (){
        dormapi.deleteAll(ids.value).then(function (obj){
            myUtils.open(obj.code, obj.msg)
            loadData()
        })
    })
}

loadData()
</script>

<template>
    <el-card>
        <el-form :inline="true">
            <el-form-item label="楼号" style="width: 200px">
                <el-input v-model="dormQuery.buildingName" placeholder="请输入楼号" clearable/>
            </el-form-item>
            <el-form-item label="宿舍名" style="width: 200px">
                <el-input v-model="dormQuery.roomNumber" placeholder="请输入宿舍名" clearable/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="tableData" ref="multipleTableRef" @selection-change="handleSelectionChange" style="width: 100%">
            <el-table-column type="selection" :selectable="selectable" width="55"/>
            <el-table-column fixed prop="id" label="ID" />
            <el-table-column align="center" header-align = 'center' prop="buildingName" label="楼号" />
            <el-table-column align="center" header-align = 'center' prop="roomNumber" label="房间号" />
            <el-table-column align="center" header-align = 'center' prop="capacity" label="容量" />
            <el-table-column align="center" header-align = 'center' prop="people" label="已住人数" />
            <el-table-column fixed="right" align="center" header-align = 'center' prop="status" label="状态" />
        </el-table>
        <!--    分页-->
        <el-pagination
                background
                :total="tatal"
                v-model:current-page="dormQuery.page"
                v-model:page-size="dormQuery.limit"
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