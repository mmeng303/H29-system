import request from "@/api/api";
import { EditUserInfo } from "./util";

export function EditUserInfoApi(user: EditUserInfo): Promise<any> {
    return request({
        url: '/api/user/saveOrUpdateUser',
        method: 'post',
        data: user
    })
}

export function logoutUserApi() {

}