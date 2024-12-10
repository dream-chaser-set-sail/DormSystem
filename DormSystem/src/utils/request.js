import axios from "axios";
import {useTokenStore} from "@/store/token.js";
import {router} from "@/router/index.js";
import {myUtils} from "@/utils/myUtils.js";


const baseURL = '/api'
export const service = axios.create({baseURL})

service.interceptors.response.use(
    obj => {
        return obj.data
    },
    error => {
        if (error.response.status === 401){
            myUtils.open(1,'请登录')
            router.push('/login')
        } else {
            myUtils.open(1, '服务异常')
        }
    }
)

service.interceptors.request.use(
// 请求前的回调
(obj) => {
    // 添加token
        const token = useTokenStore();
        // 判断有没有token
        if (token.token) {
            obj.headers.Authorization = token.token
        }
        return obj
    },
    (error => {
        // 请求错误的回调
        Promise.reject(error)
    })
)