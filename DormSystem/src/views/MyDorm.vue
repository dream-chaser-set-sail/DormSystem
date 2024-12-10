<script setup>
import {ref} from "vue";
import {myDormApi} from "@/api/myDorm.js";
import {adminInfoStore} from "@/store/adminInfo.js";

const adminInfo = adminInfoStore()
const form = ref({})
const url = 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'

function selectByDormMessage() {
    myDormApi.selectByDormMessage(adminInfo.admin.account).then(function (obj) {
        console.log(obj);
        form.value = obj.dataClass
    })
}

selectByDormMessage()
</script>

<template>
    <el-card style="padding: 0; margin-bottom: 130px">
        <H1 style="margin: 0; display: inline-block">{{ form.dormName }}</H1>
        <span style="margin-left: 20px">宿舍容量: {{form.capacityNum}}</span>
        <span style="margin-left: 20px">居住人数: {{form.peopleNum}}</span>
        <span style="margin-left: 20px">状态: {{form.status}}</span>
    </el-card>
    <div style="padding: 10px 80px; display: flex; justify-content: center;">
        <el-card style="max-width: 230px; margin: 0 20px" v-for="item in form.students" :key="item.id">
            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
                <el-image style="width: 100px; height: 100px" :src="item.image" :fit="fit" />
                <h2 style="margin: 0">{{item.name}}</h2>
                <h4 style="margin: 0">{{item.collegeName +' - '+ item.majorName}}</h4>
            </div>
        </el-card>
    </div>
</template>

<style scoped>

</style>