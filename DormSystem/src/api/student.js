import {service} from "@/utils/request.js";

export const studentapi = {
    list(conditions) {
        return service.get('/student/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/student/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/student/deleteAll/${ids}`)
    },
    add(student) {
        //student以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/student/add', student)
    },
    selectById(id) {
        return service.get(`/student/selectById/${id}`)
    },
    update(student) {
        return service.put('/student/update', student)
    },
    selCollege() {
        return service.get('/student/selectByCollege')
    },
    selMajor(id) {
        return service.get(`/student/selectByMajor/${id}`)
    }
}