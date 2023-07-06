<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { QueryTeamListApi, AddTeamApi, QueryTeamNameApi, ReleaseTeamApi, QueryUserApi, InviteMemberApi } from './api';
import { Team } from "./util"
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus/lib/components/index.js';
import { Activity } from '@/views/activityManagement/util';
import { UserInfo } from '@/util';
const tableData = ref<Team[]>([])
const teamInfoDialog = ref(false)
const teamDialogTitle = ref('新增团队')
const teamInfo = ref<Team>({
    creationTime: 0,
    isDelete: 0,
    leaderId: 0,
    teamId: 0,
    teamName: '',
    updateTime: 0,
})
const page = ref(1)
const limit = ref(10)
const total = ref(0)
const searchValue = ref<string>('')
const activityInfo = ref<Activity>({
    id: 0,
    teamId: 0,
    activityName: '',
    startTime: '',
    mainContent: '',
    startingPoint: '',
    isCompleted: 0,
})
const releaseTeamDialog = ref(false)
const releaseTeamId = ref(0)

const inviteDialog = ref(false)
const userData = ref<UserInfo[]>([])
const inviteUserId = ref(0)
const inviteTeamId = ref(0)
onMounted(async () => {
    await getTableData();
})

const getTableData = async () => {
    let data = await QueryTeamListApi({
        limit: limit.value,
        page: page.value,
    })
    tableData.value = data.data.data;
    total.value = data.data.total;
}

const releaseTeam = (team: Team) => {
    console.log(team);
    releaseTeamDialog.value = true;
    releaseTeamId.value = team.teamId;
}
const ivniteTeam = async (team: Team) => {
    inviteDialog.value = true;
    inviteTeamId.value = team.teamId;
    await QueryUserApi().then(res => {
        userData.value = res.data
        if (res.data.length > 0)
            inviteUserId.value = res.data[0].id
    })
}
const editUserInfo = (team: Team) => {
    teamInfoDialog.value = true
    teamDialogTitle.value = '编辑团队'
    teamInfo.value.teamId = team.teamId
    teamInfo.value.teamName = team.teamName
}
const deleteTeam = (team: Team) => {
    console.log(team);
    ElMessageBox.confirm('此操作将永久删除该团队, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        console.log('确定');
    }).catch(() => {
        console.log('取消');
    });
}
const addTeam = () => {
    teamInfoDialog.value = true
    teamDialogTitle.value = '新增团队'
}
const cancelTeam = () => {
    teamInfoDialog.value = false
    teamInfo.value = {
        creationTime: 0,
        isDelete: 0,
        leaderId: 0,
        teamId: 0,
        teamName: '',
        updateTime: 0,
    }
}
const confirmTeam = async () => {
    await AddTeamApi(
        teamInfo.value.teamName,
        teamInfo.value.teamId == 0 ? undefined : teamInfo.value.teamId,
    ).then(res => {
        console.log(res);
        if (res.code == 0) {
            ElMessage.success(res.msg)
            cancelTeam()
            getTableData()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const searchTeam = async () => {
    if (searchValue.value == '') {
        getTableData()
        return
    }
    await QueryTeamNameApi(
        searchValue.value,
        page.value,
        limit.value,
    ).then(res => {
        if (res.code == 0) {
            tableData.value = res.data.data;
            total.value = res.data.total;
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const cancelRelease = () => {
    releaseTeamDialog.value = false
    activityInfo.value = {
        id: 0,
        teamId: 0,
        activityName: '',
        startTime: '',
        mainContent: '',
        startingPoint: '',
        isCompleted: 0,
    }
}
const confirmRelease = async () => {
    let reqData: Activity = {
        id: 0,
        teamId: releaseTeamId.value,
        activityName: activityInfo.value.activityName,
        startTime: activityInfo.value.startTime,
        mainContent: activityInfo.value.mainContent,
        startingPoint: activityInfo.value.startingPoint,
        isCompleted: 0,
    }
    await ReleaseTeamApi(reqData).then(res => {
        if (res.code == 0) {
            ElMessage.success(res.msg)
            cancelRelease()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const cancelInvite = () => {
    inviteDialog.value = false
}

const confirmInvite = async () => {
    await InviteMemberApi(inviteTeamId.value, inviteUserId.value).then(res => {
        if (res.code == 0) {
            ElMessage.success(res.msg)
            cancelInvite()
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const pageChange = (val: number) => {
    page.value = val
    getTableData()
}
</script>


<template>
    <h2>
        团队管理
    </h2>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <div class="menu">
        <div>
            <el-text>团队名称:</el-text>
            <el-input style="margin-left: 4px;" v-model="searchValue" size="small">
                <template #append>
                    <el-button @click="searchTeam" :icon="Search" />
                </template>
            </el-input>
        </div>
        <el-button @click="addTeam" type="primary">新增团队</el-button>
    </div>
    <div class="content">
        <el-table :data="tableData" border stripe>
            <el-table-column type="index" label="序号" align="center" width="80">
            </el-table-column>
            <el-table-column prop="teamName" align="center" label="团队名称">
            </el-table-column>
            <el-table-column prop="creationTime" align="center" label="创建时间">
                <template #default="scope">
                    {{ new Date(scope.row.creationTime).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="380" align="center">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="releaseTeam(scope.row)">发布</el-button>
                    <el-button type="primary" size="small" @click="editUserInfo(scope.row)">修改</el-button>
                    <el-button type="primary" size="small" @click="ivniteTeam(scope.row)">邀请</el-button>
                    <el-button type="danger" size="small" @click="deleteTeam(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagin">
            <el-pagination @current-change="pageChange" small background layout="prev, pager, next" :total="total" />
        </div>
    </div>
    <el-dialog v-model="teamInfoDialog" :title="teamDialogTitle">
        <el-form>
            <el-form-item label="团队名称">
                <el-input v-model="teamInfo.teamName"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelTeam">取消</el-button>
                <el-button type="primary" @click="confirmTeam">
                    确认
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog v-model="releaseTeamDialog" title="发布团队">
        <el-form label-width="120px">
            <el-form-item label="活动名称:">
                <el-input v-model="activityInfo.activityName"></el-input>
            </el-form-item>
            <el-form-item label="起点:">
                <el-input v-model="activityInfo.startingPoint"></el-input>
            </el-form-item>
            <el-form-item label="主要内容:">
                <el-input v-model="activityInfo.mainContent"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelRelease">取消</el-button>
                <el-button type="primary" @click="confirmRelease">
                    确认
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog v-model="inviteDialog" title="邀请团员" width="320px">
        <el-select size="small" v-model="inviteUserId">
            <el-option v-for="item in userData" :key="item.id" :label="item.username" :value="item.id" />
        </el-select>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelInvite">取消</el-button>
                <el-button type="primary" @click="confirmInvite">
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

.pagin {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}
</style>