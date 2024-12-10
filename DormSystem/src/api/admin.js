import {service} from "@/utils/request.js";

export const adminapi = {
    list(conditions) {
        return service.get('/admin/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete( `/admin/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete( `/admin/deleteAll/${ids}`)
    },
    add(admin) {
        //admin以JSON形式传递到后台，后端使用@serviceBody接收
        return service.post('/admin/add', admin)
    },
    update(admin) {
        return service.put('/admin/update', admin)
    },
    login(admin) {
        return service.post('/login', admin)
    },
    loginInfo() {
        return service.get('/loginInfo')
    },
    updatePass(oldPass, newPass) {
        return service.put(`/admin/updatePass/${oldPass}/${newPass}`)
    },
    // 权限查询
    selectByJurisdiction() {
        return service.get('/admin/selectByJurisdiction')
    }
}