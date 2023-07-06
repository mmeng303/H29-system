<template>
    <div class="login_box" ref="login_box">
        <el-card class="box-card">
            <h2>欢迎！请先登录!</h2>
            <el-form ref="loginFormDom" :rules="loginFormRules" :model="loginFormData" label-width="120px"
                class="loginForm">
                <el-form-item label="用户名：">
                    <el-input class="input" size="small" v-model="loginFormData.username"></el-input>
                </el-form-item>
                <el-form-item label="密码：">
                    <el-input type="password" class="input" v-model="loginFormData.password" size="small"
                        :suffix-icon="View" @keydown.enter="login"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-button @click="clearFunction">清除</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
    <!-- <img src="@/assets/bg.jpg" class="bgLogin" alt="登录背景图片"> -->
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { loginApi } from './api';
import { LoginForm } from './util';
import router from '@/router/router';
import { View } from "@element-plus/icons-vue"
import { onMounted } from 'vue';
import { ElMessage, FormInstance } from 'element-plus/lib/components/index.js';
// ref
const loginFormDom = ref<FormInstance>();
const loginFormData = ref<LoginForm>({
    username: "",
    password: ""
});
const loginFormRules = ref({
    username: [
        { required: true, message: "请输入用户名", trigger: "blur" },
        { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" }
    ],
    password: [
        { required: true, message: "请输入密码", trigger: "blur" },
        { min: 6, max: 10, message: "长度在 6 到 10 个字符", trigger: "blur" }
    ]
});
const login_box = ref();
// reactive

// methods
const login = async () => {
    // 校验表单
    loginFormDom.value?.validate(async (valid: any) => {
        if (valid) {
            // 校验成功
            const resData = await loginApi(loginFormData.value)
            if (resData.code != 0) {
                if (resData.code == 401) {
                    ElMessage.warning("用户名或密码错误");
                    return;
                }
                ElMessage.warning("登录失败");
                console.log(resData);
                return;
            }
            localStorage.setItem("token", resData.data.token);
            localStorage.setItem("username", resData.data.user.username);
            localStorage.setItem("userId", resData.data.user.id);
            localStorage.setItem("authority", resData.data.user.authority);
            ElMessage.success("登录成功");
            router.push({ path: "/" });
        } else {
            // 校验失败
            ElMessage.warning("请输入正确的用户名和密码");
            return;
        }
    });
}
const clearFunction = () => {
    loginFormDom.value?.resetFields();
    loginFormData.value.username = "";
    loginFormData.value.password = "";
}

onMounted(() => {
    // 设置背景图片
})
</script>

<style scoped>
.login_box {
    width: 100vw;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background-repeat: no-repeat;
    background-position: center;
    /* 填满 */
    background-size: cover;
}

.box-card {
    display: flex;
    align-items: center;
    flex-direction: column;
}

.box-card h2 {
    text-align: center;
}

.loginForm {
    margin-top: 1rem;
}

.input {
    width: 20rem;
}

.bgLogin {
    position: absolute;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: -1;
}
</style>