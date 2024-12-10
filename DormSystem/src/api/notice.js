import {service} from "@/utils/request.js";

export const noticeapi = {
    list(conditions) {
        return service.get('/notice/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/notice/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/notice/deleteAll/${ids}`)
    },
    add(notice) {
        //notice以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/notice/add', notice)
    },
    selectById(id) {
        return service.get(`/notice/selectById/${id}`)
    },
    update(notice) {
        return service.put('/notice/update', notice)
    },
    updateDeleted(id, deleted) {
        return service.delete(`/notice/deleted/${id}/${deleted}`)
    },
    selectByAllData() {
        return service.get('/notice/selectByAllData')
    }
}