import {service} from "@/utils/request.js";

export const studentSupervisorApi = {
    list(conditions) {
        return service.get('/studentSupervisor/list', {params: conditions})
    },
    selCollege() {
        return service.get('/studentSupervisor/selectByCollege')
    },
    selMajor(id) {
        return service.get(`/studentSupervisor/selectByMajor/${id}`)
    }
}