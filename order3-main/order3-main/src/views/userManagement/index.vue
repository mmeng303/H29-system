<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { QueryUserListApi, AddUserApi } from "./api";
import { UserInfo } from '@/util';
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus/lib/components/index.js';
const limit = ref(10)
const page = ref(1)
const tableData = ref<UserInfo[]>([])
const searchValue = ref<string>('')
const userInfo = ref<UserInfo>({
    id: 0,
    name: '',
    username: '',
    password: '',
    authority: 2,
})
const userInfoDialog = ref(false)
const teamDialogTitle = ref('新增用户')
const total = ref(0)
const authority = localStorage.getItem('authority') ? Number(localStorage.getItem('authority')) : 2
onMounted(async () => {
    await getTableData();
})

const getTableData = async () => {
    let data = await QueryUserListApi({
        limit: limit.value,
        page: page.value,
        name: searchValue.value == '' ? undefined : searchValue.value
    })
    console.log(data);
    tableData.value = data.data.data;
    total.value = data.data.total;
}
const editUser = (user: UserInfo) => {
    userInfoDialog.value = true
    teamDialogTitle.value = '编辑用户'
    userInfo.value.id = user.id
    userInfo.value.name = user.name
    userInfo.value.username = user.username
    userInfo.value.password = user.password
    userInfo.value.authority = user.authority
}

const deleteUser = (user: UserInfo) => {
    console.log(user);
}
const searchUser = async () => {
    await getTableData();
}
const addUser = async () => {
    userInfoDialog.value = true
}
const cancelUser = () => {
    userInfoDialog.value = false
}
const confirmUser = async () => {
    if (userInfo.value.id == 0) userInfo.value.id = undefined
    await AddUserApi(userInfo.value).then(res => {
        if (res.code == 0) {
            userInfoDialog.value = false
            getTableData()
            userInfo.value = {
                id: 0,
                name: '',
                username: '',
                password: '',
                authority: 2,
            }
        } else {
            ElMessage.error(res.msg)
        }
    })
    userInfoDialog.value = false
}
const pageChange = (e:any) => {
    page.value = e
    getTableData()
}
</script>


<template>
    <h2>
        用户管理
    </h2>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <div class="menu">
        <div>
            <el-text>用户名称:</el-text>
            <el-input style="margin-left: 4px;" v-model="searchValue" size="small">
                <template #append>
                    <el-button @click="searchUser" :icon="Search" />
                </template>
            </el-input>
        </div>
        <el-button @click="addUser" type="primary">新增用户</el-button>
    </div>
    <div class="content">
        <el-table :data="tableData" border style="width: 100%">
            <el-table-column type="index" align="center" label="序号" width="180">
            </el-table-column>
            <el-table-column prop="id" align="center" label="用户id" width="180">
            </el-table-column>
            <el-table-column prop="username" align="center" label="用户名" width="240">
            </el-table-column>
            <el-table-column prop="name" align="center" label="昵称" width="240">
            </el-table-column>
            <el-table-column prop="password" align="center" label="密码" width="240">
            </el-table-column>
            <el-table-column prop="authority" align="center" label="权限" width="180">
                <template #default="scope">
                    <el-tag v-if="scope.row.authority == 1" type="success">团长</el-tag>
                    <el-tag v-else type="info">团员</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="editUser(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="deleteUser(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagin">
            <el-pagination @current-change="pageChange" small background layout="prev, pager, next" :total="total" />
        </div>
    </div>
    <el-dialog v-model="userInfoDialog" :title="teamDialogTitle">
        <el-form label-width="120px">
            <el-form-item label="用户名">
                <el-input v-model="userInfo.username"></el-input>
            </el-form-item>
            <el-form-item label="昵称">
                <el-input v-model="userInfo.name"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="userInfo.password"></el-input>
            </el-form-item>
            <el-form-item label="权限">
                <el-select v-model="userInfo.authority" placeholder="请选择权限">
                    <el-option label="团长" :value="1" :disabled="authority == 1"></el-option>
                    <el-option label="团员" :value="2"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelUser">取消</el-button>
                <el-button type="primary" @click="confirmUser">
                    确认
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>


<style scoped>
.content {
    margin-top: 20px;
}

.menu {
    display: flex;
    align-items: center;
}

.menu>div {
    display: flex;
    align-items: center;
    margin-right: 20px;
}

.menu>div span {
    white-space: nowrap;
}
</style>