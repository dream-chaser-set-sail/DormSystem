import {service} from "@/utils/request.js";

export const repairapi = {
    list(conditions) {
        return service.get('/repair/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/repair/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/repair/deleteAll/${ids}`)
    },
    add(repair) {
        //repair以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/repair/add', repair)
    },
    selectById(id) {
        return service.get(`/repair/selectById/${id}`)
    },
    update(repair) {
        return service.put('/repair/update', repair)
    },
    selectByName() {
        return service.get('/repair/selectByName')
    }
}