import { UserInfo } from "@/util";


export interface LoginForm {
    password: string;
    username: string;
}

export interface LoginResponse {
    code: number;
    data: any;
    msg: string;
}

export interface Response {
    token: string;
    user: UserInfo;
}