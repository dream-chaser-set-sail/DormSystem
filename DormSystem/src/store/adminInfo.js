import {defineStore} from "pinia";

export const adminInfoStore = defineStore('adminInfo', {
    state() {
        return {
            admin : {}
        }
    },

    actions: {
        setAdminInfo(admin){
            this.admin = admin
        },
        removeAdminInfo(){
            this.admin = {}
        }
    },

    persist: {
        enabled: true, // 开启缓存  默认会存储在本地localstorage
    }
})