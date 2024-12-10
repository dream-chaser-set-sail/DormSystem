<script setup>
import * as echarts from 'echarts'
import {onMounted} from "vue";
import {ref} from "vue";
import {chartsApi} from "@/api/charts.js";
import {noticeapi} from "@/api/notice.js";
import {indexapi} from "@/api/index.js";
import { StarFilled } from '@element-plus/icons-vue'

const chartRef = ref()
const chartsData = ref({})
const yData = ref([])
const xData = ref([])
const dialogContentVisible = ref(false)
// const content = ref("")
const indexTopForm = ref({})
const noticeForms = ref({})
const noticeForm = ref({})

onMounted(() => {
    const myChart = echarts.init(chartRef.value);
    chartsApi.selectByData().then(function (obj) {
        chartsData.value = obj.dataClass
        yData.value = Object.keys(chartsData.value) // 提取对象的所有键（属性名）并将它们放入一个数组中
        xData.value = Object.values(chartsData.value);

        myChart.setOption({
            title: {
                text: '各宿舍楼学生人数统计分析'
            },
            tooltip: {},
            xAxis: {
                data: yData.value
            },
            yAxis: {},
            series: [
                {
                    name: '人数',
                    type: 'bar',
                    data: xData.value
                }
            ]
        });
    })
})

function openDialog(id, name) {
    noticeapi.selectById(id).then(function (obj) {
        noticeForm.value = obj.dataClass
        noticeForm.value.name = name
    })
    dialogContentVisible.value = true
}

function selectByNotice() {
    noticeapi.selectByAllData().then(function (obj) {
        noticeForms.value = obj.data
    })
}

function selectByTopNum () {
    indexapi.selectByTopNum().then(function (obj) {
        indexTopForm.value = obj.dataClass
    })
}

selectByTopNum()
selectByNotice()
</script>

<template>
    <div style="display: flex">
        <div class="left-div">
            <div class="top-div">
                <div class="card">
                    <div class="title">学生数量</div>
                    <div class="title">{{indexTopForm.学生数量}}位</div>
                </div>
                <div class="card">
                    <div class="title">迁宿记录</div>
                    <div class="title">{{indexTopForm.迁宿条数}}条</div>
                </div>
                <div class="card">
                    <div class="title">报修记录</div>
                    <div class="title">{{indexTopForm.报修条数}}条</div>
                </div>
            </div>
            <el-card style="max-width: 100%; height: 400px; margin: 0 10px">
                <div ref="chartRef" style="width: 100%; height: 400px;"></div>
            </el-card>
        </div>
        <div class="right-div">
            <el-card style="width: 100%; height: 100%; border-radius: 8px; overflow: hidden">
                <h1 style="margin: 0">公告栏</h1>
                <div style="height: 540px; overflow: auto;" class="scroll-container">
                    <div v-for="item in noticeForms" :key="item.id" class="infinite-list" @click="openDialog(item.id, item.name)">
                        <h1 style="margin-top: 0">{{item.title}}</h1>
                        <h4 style="margin-bottom: 0">发布人: {{item.name}}</h4>
                        <p style="margin: 0; font-size: 12px">发布时间: {{item.updateTime}}</p>
                    </div>
                </div>
            </el-card>
        </div>
    </div>

    <!--公告内容详情-->
    <el-dialog v-model="dialogContentVisible" width="666">
        <template #header>
            <h1 style="margin: 0">{{noticeForm.title}}</h1>
            <span style="font-size: 12px">{{noticeForm.updateTime}}</span>
            <span style="font-size: 12px; margin-left: 20px">{{noticeForm.name}}</span>
            <el-divider style="margin: 10px 0">
                <el-icon style="color: red"><star-filled /></el-icon>
            </el-divider>
        </template>
        <span v-html="noticeForm.content"></span>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="dialogContentVisible = false">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>
.card {
    width: 200px;
    height: 100px;
    background: repeating-linear-gradient(
            45deg,
            #ffffff,
            #8fd5ec 10px,
            #add8e6 10px,
            #ffffff 20px
    );
    border-radius: 15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    padding: 20px;
    margin: 0 10px;
}

.title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
}
.top-div {
    display: flex;
    margin: 20px 0;
}
.left-div {
    display: flex;
    flex-direction: column;
}
.right-div {
    width: 100%;
}
.infinite-list {
    height: 100px;
    margin: 10px 0;
    border-radius: 15px;
    padding: 5px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    background-color: rgba(143, 213, 236, 0.5);
}

.scroll-container::-webkit-scrollbar {
    display: none; /* 隐藏 Chrome 和 Safari 的滚动条 */
}
.scroll-container {
    -ms-overflow-style: none;  /* 隐藏 IE 和 Edge 的滚动条 */
    scrollbar-width: none;  /* 隐藏 Firefox 的滚动条 */
}
</style>