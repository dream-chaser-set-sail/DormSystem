import {service} from "@/utils/request.js";

export const chartsApi = {
    selectByData() {
        return service.get("/charts/selectByData")
    }
}