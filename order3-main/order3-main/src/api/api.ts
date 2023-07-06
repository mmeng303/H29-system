import axios from "axios";
import router from "@/router/router";
import { ElMessage } from "element-plus/lib/components/index.js";
const request = axios.create({});

request.interceptors.request.use(
    (config) => {
        // 如果有token，就携带token
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.token = token;
        }
        return config;
    }
)
request.interceptors.response.use(
    (response) => {
        
        if (response.data === "") {
            ElMessage.error("没有权限");
            router.push({
                replace: true,
                path: "/login"
            })
            return
        }
        return response.data;
    },
    (error) => {
        // 状态码为401重定向到登录页面清除token
        if (error.response.status == 401) {
            router.push({
                replace: true,
                path: "/login"
            })
            ElMessage.error("登录过期，请重新登录");
            return Promise.reject(error);
        }
        // 403权限不够
        if (error.response.status == 403) {
            ElMessage.error("权限不够");
            return Promise.reject(error);
        }
        // 500也退出到登录页面
        if (error.response.status == 500) {
            ElMessage.error("服务器错误");
        }
        return Promise.reject(error);
    }
)
export default request;