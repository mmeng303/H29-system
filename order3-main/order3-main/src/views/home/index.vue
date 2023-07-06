<template>
    <div class="home">
        <nav>
            <div class="nav_title">
                管理中心
            </div>
            <div class="nav_icon" @click="switchLedtCollapse">
                <el-icon v-if="leftCollapse">
                    <Expand />
                </el-icon>
                <el-icon v-else>
                    <Fold />
                </el-icon>
            </div>
            <div class="nav_user">
                <el-dropdown class="drop" @command="userCommand">
                    <span class="el-dropdown-link">
                        {{ userNickName }}
                        <el-icon class="el-icon--right">
                            <arrow-down />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="edit">修改信息</el-dropdown-item>
                            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </nav>
        <div class="main">
            <el-menu :collapse="leftCollapse" :router="true" class="left_menu" background-color="#545c64"
                text-color="#ffffff" :default-active="leftMenuValue">
                <el-menu-item index="/userManagement" v-if="authority == 1">
                    <el-icon>
                        <User />
                    </el-icon>
                    <span>
                        用户管理
                    </span>
                </el-menu-item>
                <el-menu-item index="/teamManagement" v-if="authority == 1">
                    <el-icon>
                        <Grid />
                    </el-icon>
                    <span>团队管理</span>
                </el-menu-item>
                <el-menu-item index="/userTeamManagement" v-if="authority == 2">
                    <el-icon>
                        <Grid />
                    </el-icon>
                    <span>已加团队</span>
                </el-menu-item>
                <el-menu-item index="/toBeAccepted" v-if="authority == 2">
                    <el-icon>
                        <Grid />
                    </el-icon>
                    <span>待接邀请</span>
                </el-menu-item>
                <el-menu-item index="/activityManagement">
                    <el-icon>
                        <DataAnalysis />
                    </el-icon>
                    <span>活动管理</span>
                </el-menu-item>
                <el-menu-item index="/tourFareManagement" v-if="authority == 1">
                    <el-icon>
                        <DataLine />
                    </el-icon>
                    <span>团费管理</span>
                </el-menu-item>
            </el-menu>
            <main>
                <router-view v-slot="{ Component }">
                    <KeepAlive>
                        <component :is="Component" :key="$route.name" v-if="$route.meta.keepAlive" />
                    </KeepAlive>
                    <component :is="Component" :key="$route.name" v-if="!$route.meta.keepAlive" />
                </router-view>
            </main>
        </div>
        <el-dialog v-model="editUserInfoDialog" title="修改用户信息" :append-to-body="true" :close-on-click-modal="false"
            :close-on-press-escape="false">
            <el-form :model="editUserForm" label-width="120px" ref="editUser" :rules="editUserRule">
                <el-form-item label="请输入名字" prop="name">
                    <el-input class="input" size="mini" v-model="editUserForm.name"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input class="input" size="mini" type="password" v-model="editUserForm.password"
                        placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="passwordAgain">
                    <el-input class="input" size="mini" type="password" v-model="editUserForm.passwordAgain"
                        placeholder="请再次输入密码"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="editUserInfoDialog = false">取 消</el-button>
                <el-button type="primary" @click="confirmEditUserInfo">确 定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import router from '@/router/router';
import { onMounted, ref, reactive } from 'vue'
import { FormInstance, FormRules } from 'element-plus/lib/components/form/index.js';
import ElMessage from 'element-plus/lib/components/message/index.js';
import { UserInfo } from '@/util';
import { EditUserInfoApi } from './api';
// store
const authority = localStorage.getItem('authority') ? Number(localStorage.getItem('authority')) : 0


// ref
const leftCollapse = ref(false)
const leftMenuValue = ref('/overview')
const userNickName = ref('')
const editUserInfoDialog = ref(false)
const editUserForm = ref<UserInfo>({
    id: 0,
    username: '',
    authority: 0,
    name: '',
});
const checkPassword = (_rule: any, value: string, callback: any) => {
    if (value === '') {
        callback(new Error('请再次输入密码'));
    } else if (value !== editUserForm.value.password) {
        callback(new Error('两次输入密码不一致!'));
    } else {
        callback();
    }
};
const editUserRule = reactive<FormRules>({
    name: [
        { required: true, message: '请输入名字', trigger: 'blur' },
        { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
    ],
    passwordAgain: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
        { validator: checkPassword, trigger: 'blur' }
    ]
})
const editUser = ref<FormInstance>()
// methods
const switchLedtCollapse = () => {
    leftCollapse.value = !leftCollapse.value;
}
const userCommand = async (command: string) => {
    switch (command) {
        case 'edit':
            editUserInfoDialog.value = true;
            break;
        case 'logout':
            localStorage.clear();
            router.push('/login');
            break;
    }
}
const confirmEditUserInfo = () => {
    editUser.value?.validate(async (valid: boolean) => {
        if (valid) {
            await EditUserInfoApi({
                authority: editUserForm.value.authority,
                id: editUserForm.value.id as number,
                name: editUserForm.value.name,
                username: editUserForm.value.username,
                password: editUserForm.value.password as string,
            }).then(res => {
                console.log(res);
            })
        } else {
            ElMessage.warning('请检查输入内容');
            return false;
        }
    });
}

// 生命周期
onMounted(() => {
    leftMenuValue.value = router.currentRoute.value.path;

    userNickName.value = localStorage.getItem("username") ? localStorage.getItem("username")! : '';
    editUserForm.value.name = userNickName.value;
    editUserForm.value.username = userNickName.value;
    editUserForm.value.authority = authority;
    editUserForm.value.id = Number(localStorage.getItem('userId'));
})
</script>

<style scoped>
.home {
    display: flex;
    flex-direction: column;
    height: 100vh;
}

.main {
    display: flex;
    height: 100%;
}

.top_menu {
    border-bottom: none;
}

.title {
    font-size: 18px;
    padding: 0 1.4rem;
    color: white;
}

.left_menu:not(.el-menu--collapse) {
    width: 10rem;
}

.left_menu /deep/ .el-sub-menu__title {
    padding-right: 43px;
}

main {
    padding: 1rem;
    width: 100%;
    height: 100%;
}

.home nav {
    width: 100vw;
    height: 60px;
    background-color: #434b52;
    display: flex;
    align-items: center;
}

.home nav>div {
    letter-spacing: 2px;
    font-size: 1.1rem;
    cursor: pointer;
    transition: all .3s;
    color: white;
}

.nav_title {
    width: 10rem;
    text-align: center;
    height: 60px;
    line-height: 60px;
    font-weight: bold;
}

.nav_icon {
    width: 4rem;
    text-align: center;
    height: 60px;
    line-height: 60px;
}

.home nav>div:hover {
    background-color: #31373c;
}

.nav_user {
    margin-left: auto;
    height: 60px;
    padding: 0 2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
}

.drop {
    color: white;
    width: 100%;
    height: 60px;
    line-height: 60px;
}

.drop:focus {
    outline: none;
}

.drop /deep/ .el-dropdown-link:focus {
    outline: none;
}
</style>