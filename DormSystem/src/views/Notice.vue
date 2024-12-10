<script setup>
import {
    Delete,
    Check,
    Close,
    Edit,
    Plus
} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {myUtils} from "@/utils/myUtils.js";
import {useTokenStore} from '@/store/token.js'
import {noticeapi} from "@/api/notice.js";
import WangEditor from "@/components/WangEditor.vue";
import {adminInfoStore} from "@/store/adminInfo.js";

const adminInfo = adminInfoStore()
const token = useTokenStore()
const tatal = ref(0)
const tableData = ref([])
const noticeQuery = reactive({
    name : '',
    page : 1,
    limit : 5
})
const form = ref({})
const dialogFormVisible = ref(false)
const dialogContentVisible = ref(false)
const title = ref("")
const isAddorUpdata = ref(0)
const ids = ref([])
const content = ref("")

function loadData(){
    noticeapi.list(noticeQuery).then(function (obj){
        tableData.value = obj.data
        tatal.value = obj.count
    })
}

function handleDelete(val){
    noticeapi.deleteById(val).then(function (obj){
        myUtils.open(obj.code, obj.msg)
        loadData()
    })
}

function openDialog(row) {
    dialogContentVisible.value = true
    content.value = row
}

function showAddDialog(){
    dialogFormVisible.value = true
    form.value = {}
    isAddorUpdata.value = 2
    title.value = "发布公告"
}

function handleEdit(val){
    dialogFormVisible.value = true
    noticeapi.selectById(val).then(function (obj){
        title.value = "修改信息"
        isAddorUpdata.value = 1
        form.value = obj.dataClass
        form.value.persistentTimeArray = obj.dataClass.persistentTime.split(",");
        console.log(form.value.persistentTimeArray);
    })
}

function submitUser(){
    switch (isAddorUpdata.value){
        case 1:
            noticeapi.update(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
        case 2:
            form.value.accountNum = adminInfo.admin.account
            noticeapi.add(form.value).then(function (obj){
                myUtils.open(obj.code, obj.msg)
                dialogFormVisible.value = false
                loadData()
            })
            break
    }
}

function onSearch(){
    noticeQuery.page = 1
    loadData()
}

function handleSelectionChange(rows){
    ids.value = rows.map(row => row.id)
}

function deleteAll(){
    myUtils.openWindow("批量删除", "你确定要删除吗？", function (){
        noticeapi.deleteAll(ids.value).then(function (obj){
            myUtils.open(obj.code, obj.msg)
            loadData()
        })
    })
}

function onEidtorChange(content) {
    console.log(content);
    form.value.content = content
}

function changeDeleted(row){
    noticeapi.updateDeleted(row.id,row.deleted).then(function (obj){
        myUtils.open(obj.code, obj.msg)
    })
}

loadData()
</script>

<template>
    <el-card>
        <template #header>
            <div class="header">
                <el-button type="primary" @click="showAddDialog" plain>发布公告</el-button>
                <el-button type="danger" @click="deleteAll" plain>批量删除</el-button>
            </div>
        </template>
        <el-form :inline="true">
            <el-form-item label="标题" style="width: 200px">
                <el-input v-model="noticeQuery.title" placeholder="请输入要查找的标题" clearable/>
            </el-form-item>
            <el-form-item label="更新时间">
                <el-date-picker
                    v-model="noticeQuery.createTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="noticeQuery.updateTime"
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
            <el-table-column align="center" header-align = 'center' prop="title" label="公告标题" />
            <el-table-column align="center" header-align = 'center' prop="content" label="公告内容">
                <template #default=" {row} ">
                    <span class="cell-ellipsis" @click="openDialog(row.content)">{{ '...' }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" header-align = 'center' prop="name" label="发布人" />
            <el-table-column align="center" header-align = 'center' prop="persistentTime" label="有效时间" />
            <el-table-column align="center" header-align = 'center' prop="deleted" label="逻辑删除">
                <template #default="scope">
                    <el-switch
                        v-model="scope.row.deleted"
                        inline-prompt
                        active-text="未删除"
                        inactive-text="已删除"
                        size="large"
                        :active-value="1"
                        :inactive-value="0"
                        @change="changeDeleted(scope.row)"
                    />
                </template>
            </el-table-column>
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
                    <el-button type="primary" @click="handleEdit(row.id)" :icon="Edit" circle />
                </template>
            </el-table-column>
        </el-table>
        <!--    分页-->
        <el-pagination
                background
                :total="tatal"
                v-model:current-page="noticeQuery.page"
                v-model:page-size="noticeQuery.limit"
                :page-sizes="[5, 10, 15, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                style="margin-top: 20px"
                @change="loadData"
        />
        <!--    添加/修改弹出框-->
        <el-dialog v-model="dialogFormVisible" :title=title width="800">
            <el-form :model="form">
                <el-form-item label="标题" :label-width="formLabelWidth">
                    <el-input v-model="form.title" autocomplete="off" />
                </el-form-item>
                <el-form-item label="有效期" :label-width="formLabelWidth">
                    <el-date-picker
                        v-model="form.persistentTimeArray"
                        type="daterange"
                        value-format="YYYY-MM-DD"
                        range-separator="至"
                        start-placeholder="Start date"
                        end-placeholder="End date"
                    />
                </el-form-item>
                <el-form-item label="公告内容" :label-width="formLabelWidth">
                    <WangEditor :init-value="form.content" v-if="dialogFormVisible"
                                @close="dialogFormVisible = false" @getEditorContent="onEidtorChange"></WangEditor>
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
        <!--公告内容详情-->
        <el-dialog v-model="dialogContentVisible" width="666">
            <span v-html="content"></span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="dialogContentVisible = false">确定</el-button>
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

.cell-ellipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 200px; /* 设置适当的最大宽度 */
    cursor: pointer; /* 鼠标指针样式 */
}
</style>