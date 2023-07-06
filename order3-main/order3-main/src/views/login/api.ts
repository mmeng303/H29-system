import request from "@/api/api"
import { LoginForm, LoginResponse } from "./util"

export function loginApi(loginForm: LoginForm): Promise<LoginResponse> {
    return request({
        url: "/api/user/login",
        method: "post",
        data: loginForm
    })
}