import {service} from "@/utils/request.js";

export const indexapi = {
    selectByTopNum() {
        return service.get('/index/selectByStuNum')
    }
}