import {ElMessage, ElMessageBox} from "element-plus";
import { markRaw } from 'vue'
import { Delete } from '@element-plus/icons-vue'


export const myUtils = {
    // 消息提示
    open(openNum, msg){
        if (openNum == 0){
            ElMessage({
                message: msg,
                type: 'success',
            })
        }else{
            ElMessage.error(msg)
        }
    },

    openWindow(tittle, msg, funcation){
        ElMessageBox.confirm(
            msg,
            tittle,
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
                lockScroll: false, //防止抖动
                icon: markRaw(Delete),
            }).then(funcation)
    }
}