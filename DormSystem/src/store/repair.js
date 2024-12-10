import {defineStore} from "pinia";

export const repairInfoStore = defineStore('repairInfo', {
    state() {
        return {
            repair : {},
            active: 0
        }
    },

    actions: {
        setRepairInfo(move){
            this.repair = move
        },
        setActiveInfo(active){
            this.active = active
        },
        removeRepairInfo(){
            this.repair = {}
            this.active = 0
        }
    },

    persist: {
        enabled: true, // 开启缓存  默认会存储在本地localstorage
    }
})