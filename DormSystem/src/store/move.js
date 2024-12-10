import {defineStore} from "pinia";

export const moveInfoStore = defineStore('moveInfo', {
    state() {
        return {
            move : {},
            active: 0
        }
    },

    actions: {
        setMoveInfo(move){
            this.move = move
        },
        setActiveInfo(active){
            this.active = active
        },
        removeMoveInfo(){
            this.move = {}
            this.active = 0
        }
    },

    persist: {
        enabled: true, // 开启缓存  默认会存储在本地localstorage
    }
})