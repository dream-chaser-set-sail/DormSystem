import {service} from "@/utils/request.js";

export const kepchaApi = {
    captcha() {
        return service.get('/captcha')
    }
}