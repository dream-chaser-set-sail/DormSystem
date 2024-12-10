import {defineStore} from "pinia";
import Cookies from 'js-cookie'
import {ref} from 'vue'

const remember = ref(false)

const cookiesStorage = {
    // 设置 cookie 项
    setItem(key, state){
        // 自定义Cookie存储

        // key就是定义的useTokenStore的id ==> 'token'
        // state是token对应参数token

        /* setItem: 将 token 存储到 cookies 中。如果 remember 为 true，则 token 的过期时间设置为 7 天；否则，token 不会设置过期时间。
           getItem: 从 cookies 中获取 token，并将其转换为 JSON 字符串格式返回。*/

        console.log(`setItem ${key} -- ${state}`);

        // JSON转JS
        let myState = JSON.parse(state);

        if (remember.value){
            return Cookies.set(key, myState.token, {expires: 7})
        }else {
            return Cookies.set(key, myState.token) // 未设置时间默认放置到session内
        }
    },

    // 获取 cookie 项
    getItem(key) {
        console.log(`getItem ${key}`)
        return JSON.stringify({
            token: Cookies.get(key)
        })
    }
}

export const useTokenStore = defineStore('token', {
    // 存数据
    state() {
        return {
            token: ''
        }
    },

    // 放方法
    actions: {
        setToken(newToken, rem) {
            remember.value = rem
            this.token = newToken
        },
        removeToken() {
            this.token = ''
            remember.value = false
        }
    },

    persist: {
        // enabled: true, // 开启缓存  默认会存储在本地localstorage
        storage: cookiesStorage
    }
})