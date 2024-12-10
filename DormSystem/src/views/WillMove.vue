<script setup>
import {ref} from "vue";
import {moveapi} from "@/api/move.js";
import {moveInfoStore} from "@/store/move.js";

const moveInfo = moveInfoStore()
const form = ref({})
const moveDorm = ref({})
const active = ref(0)
const disabled = ref(false)

function selectByDorm(){
    moveapi.selectByDorm().then(function (obj){
        moveDorm.value = obj.data
    })
}

function submitForm() {
    moveapi.add(form.value).then(function (obj) {
        console.log(obj);
        if (obj.msg == '操作成功') {
            if (active.value++ > 2) active.value = 0
            moveInfo.setMoveInfo(form.value)
            moveInfo.setActiveInfo(active.value)
            disabled.value = true
        }
    })
}

function resetForm() {
    form.value = {}
}

function brushFrom() {
    form.value = moveInfo.move
    active.value = moveInfo.active
    if (active.value != 0) {
        disabled.value = true
    }
}

function yes() {
    moveInfo.removeMoveInfo()
    location.reload()
}

brushFrom()
selectByDorm()
</script>

<template>
<el-card>
    <div style="margin-left: 300px; margin-bottom: 50px; margin-top: 26px">
      <el-steps style="max-width: 600px" :active="active" finish-status="success">
          <el-step title="我要迁宿" />
          <el-step title="审核中" />
          <el-step title="审核结束" />
      </el-steps>
    </div>
    <el-form :model="form" :label-width="340">
        <el-form-item label="学生姓名">
            <el-input v-model="form.name" placeholder="确认迁宿请在此输入名字" style="width: 570px;" clearable/>
        </el-form-item>
        <el-form-item label="迁入宿舍" style="width: 910px">
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