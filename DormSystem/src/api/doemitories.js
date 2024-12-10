import {service} from "@/utils/request.js";

export const dormapi = {
    list(conditions) {
        return service.get('/dorm/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/dorm/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/dorm/deleteAll/${ids}`)
    },
    add(dorm) {
        //dorm以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/dorm/add', dorm)
    },
    selectById(id) {
        return service.get(`/dorm/selectById/${id}`)
    },
    update(dorm) {
        return service.put('/dorm/update', dorm)
    },
}