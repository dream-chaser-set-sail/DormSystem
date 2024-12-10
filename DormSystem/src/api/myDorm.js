import {service} from "@/utils/request.js";

export const myDormApi = {
    selectByDormMessage(account) {
        return service.get(`/myDorm/selectByDormMessage/${account}`)
    },
}