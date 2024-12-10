<script setup>
import {ref} from "vue";
import {repairInfoStore} from "@/store/repair.js";
import {repairapi} from "@/api/repair.js";

const repairInfo = repairInfoStore()
const form = ref({})
const active = ref(0)
const disabled = ref(false)

function submitForm() {
    repairapi.add(form.value).then(function (obj) {
        console.log(obj);
        if (obj.msg == '添加成功') {
            if (active.value++ > 2) active.value = 0
            repairInfo.setRepairInfo(form.value)
            repairInfo.setActiveInfo(active.value)
            disabled.value = true
        }
    })
}

function resetForm() {
    form.value = {}
}

function brushFrom() {
    form.value = repairInfo.repair
    active.value = repairInfo.active
    if (active.value != 0) {
        disabled.value = true
    }
}

function yes() {
    repairInfo.removeRepairInfo()
    location.reload()
}

brushFrom()
</script>

<template>
<el-card>
    <div style="margin-left: 300px; margin-bottom: 50px; margin-top: 26px">
      <el-steps style="max-width: 600px" :active="active" finish-status="success">
          <el-step title="我要报修" />
          <el-step title="维修中" />
          <el-step title="维修结束" />
      </el-steps>
    </div>
    <el-form :model="form" :label-width="340">
        <el-form-item label="学生姓名">
            <el-input v-model="form.name" placeholder="请在此输入名字" style="width: 570px;" clearable/>
        </el-form-item>
        <el-form-item label="报修物品" style="width: 910px">
            <el-input v-model="form.repair" placeholder="请在此输入需要维修的器具" style="width: 570px;" clearable/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button type="primary" @click="resetForm" :disabled="disabled">重置</el-button>
            <el-button v-if="active == 3" type="primary" @click="yes">确认</el-button>
        </el-form-item>
    </el-form>
</el-card>
</template>

<style scoped>

</style>