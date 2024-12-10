import {service} from "@/utils/request.js";

export const supervisorapi = {
    list(conditions) {
        return service.get('/supervisor/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/supervisor/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/supervisor/deleteAll/${ids}`)
    },
    add(supervisor) {
        //supervisor以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/supervisor/add', supervisor)
    },
    selectById(id) {
        return service.get(`/supervisor/selectById/${id}`)
    },
    update(supervisor) {
        return service.put('/supervisor/update', supervisor)
    },
    selBuildingName() {
        return service.get('/dorm/selectByBuildingName')
    }
    
}