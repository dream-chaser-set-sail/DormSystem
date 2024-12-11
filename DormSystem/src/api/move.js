import {service} from "@/utils/request.js";

export const moveapi = {
    list(conditions) {
        return service.get('/move/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/move/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/move/deleteAll/${ids}`)
    },
    add(move) {
        //move以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/move/add', move)
    },
    selectById(id) {
        return service.get(`/move/selectById/${id}`)
    },
    update(move) {
        return service.put('/move/update', move)
    },
    selectByDorm() {
        return service.get('/move/selectByDorm')
    },
    selectByName() {
        return service.get('/move/selectByName')
    },
    updateByNo(move) {
        return service.put('/move/updateByNo', move)
    },
    selectByStatus(name) {
        return service.get(`/move/selectByStatus/${name}`)
    },
    updateStateInEND(id, name) {
        return service.get(`/move/updateStateInEND/${id}/${name}`)
    }
}