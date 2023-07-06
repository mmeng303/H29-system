<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus/lib/components/index.js';
import { SettlementApi, PaymentApi, QueryActivityList } from './api';
import { Activity, Payment, QueryActivityListParams } from './util';
import { ReleaseTeamApi } from '../teamManagement/api';
const searchValue = ref<string>('')
const searchActivity = async () => {
    await getTableData()
}
const page = ref(1)
const limit = ref(10)
const total = ref(0)
const activityDialog = ref(false)
const activityTitle = ref('新增活动')
const activityInfo = ref<Activity>({
    id: 0,
    teamId: 0,
    activityName: '',
    startTime: '',
    mainContent: '',
    startingPoint: '',
    isCompleted: 0,
})
const payMode = ref(1)
const tableData = ref<Activity[]>([])
const detailDialog = ref(false)
const activityDetail = ref<Activity>({
    id: 0,
    teamId: 0,
    activityName: '',
    startTime: '',
    mainContent: '',
    startingPoint: '',
    isCompleted: 0,
})
let authority = localStorage.getItem('authority') ? Number(localStorage.getItem('authority')) : 2
const payDialog = ref(false)
const money = ref(0)
const userId = localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : 0
const getTableData = async () => {
    let reqData: QueryActivityListParams = {
        page: page.value,
        limit: limit.value,
        userId: userId,
        activityName: searchValue.value,
    }
    await QueryActivityList(reqData).then(res => {
        if (res.code === 0) {
            console.log(res);
            if(authority ===1){

                tableData.value = res.data.data
            }else{
                tableData.value = res.data
            }
            total.value = res.data.total
        } else {
            ElMessage.error('获取数据失败')
        }
    })
}

onMounted(async () => {
    await getTableData();
})
const cancelActivity = () => {
    activityDialog.value = false
}

const confirmActivity = async () => {
    await ReleaseTeamApi(activityInfo.value).then(res => {
        if (res.code === 0) {
            ElMessage.success('编辑成功')
            cancelActivity()
            getTableData()
        } else {
            ElMessage.error('编辑失败')
        }
    })
}
const openActivityInfo = async (activity: Activity) => {
    console.log(activity);
    activityDetail.value = activity
    detailDialog.value = true
}
const payActivity = async (activity: Activity) => {
    console.log(activity);
    activityDetail.value = activity
    payDialog.value = true
}
const confirmPay = async () => {
    let reqData: Payment = {
        activityId: activityDetail.value.id,
        memberId: userId,
        fee: money.value,
        isFee: payMode.value,
    }
    await PaymentApi(reqData).then(res => {
        if (res.code === 0) {
            ElMessage.success('缴费成功')
            payDialog.value = false
            getTableData()
        } else {
            ElMessage.error('缴费失败')
        }
    })
}
const settlement = (activity: Activity) => {
    ElMessageBox.confirm('是否结算该活动？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        await SettlementApi(activity.id).then(res => {
            if (res.code === 0) {
                ElMessage.success('结算成功')
                getTableData()
            } else {
                ElMessage.error('结算失败')
            }
        })
    }).catch(() => {
        ElMessage.info('已取消操作')
    })
}
const editActivity = async (activity: Activity) => {
    console.log(activity);
    activityInfo.value = activity
    activityDialog.value = true
    activityTitle.value = '编辑活动'
}
const pageChange = async (val: number) => {
    page.value = val
    await getTableData()
}
</script>


<template>
    <h2>
        活动管理
    </h2>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <div class="menu">
        <div>
            <el-text>活动名称:</el-text>
            <el-input style="margin-left: 4px;" v-model="searchValue" size="small">
                <template #append>
                    <el-button @click="searchActivity" :icon="Search" />
                </template>
            </el-input>
        </div>
    </div>
    <div class="content">
        <el-table :data="tableData" border stripe>
            <el-table-column type="index" label="序号" align="center" width="80">
            </el-table-column>
            <el-table-column prop="activityName" align="center" label="活动名称">
            </el-table-column>
            <el-table-column prop="startTime" align="center" label="创建时间">
                <template #default="scope">
                    {{ new Date(scope.row.startTime).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="380" align="center">
                <template #default="{ row }">
                    <el-button type="primary" size="small" @click="openActivityInfo(row)">详情</el-button>
                    <el-button type="primary" size="small" @click="payActivity(row)">缴纳团费</el-button>
                    <el-button v-if="authority==1" type="primary" size="small" @click="editActivity(row)">修改</el-button>
                    <el-button v-if="authority==1" type="success" size="small" @click="settlement(row)">结算</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagin">
            <el-pagination @current-change="pageChange" small background layout="prev, pager, next" :total="total" />
        </div>
    </div>
    <el-dialog v-model="activityDialog" :title="activityTitle">
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
                <el-button @click="cancelActivity">取消</el-button>
                <el-button type="primary" @click="confirmActivity">
                    确认
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog v-model="detailDialog" title="详情">
        <el-descriptions :column="1">
            <el-descriptions-item label="活动名称:">{{ activityDetail.activityName }}</el-descriptions-item>
            <el-descriptions-item label="起点:">{{ activityDetail.startingPoint }}</el-descriptions-item>
            <el-descriptions-item label="主要内容:">{{ activityDetail.mainContent }}</el-descriptions-item>
            <el-descriptions-item label="创建时间:">{{ new Date(activityDetail.startTime).toLocaleString()
            }}</el-descriptions-item>
            <el-descriptions-item label="是否结算:">
                <el-tag v-if="activityDetail.isCompleted == 0" type="danger">
                    未结算
                </el-tag>
                <el-tag v-else type="success">
                    已结算
                </el-tag>
            </el-descriptions-item>
        </el-descriptions>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="detailDialog = false">取消</el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog v-model="payDialog" title="缴费" width="30%">
        <el-form label-width="120px">
            <el-form-item label="缴费模式:">
                <el-radio-group v-model="payMode">
                    <el-radio :label="1">团队AA缴纳</el-radio>
                    <el-radio :label="2">团队独立缴费</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="缴纳费用:">
                <el-input v-model="money" size="small"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="confirmPay" type="primary">确定</el-button>
                <el-button @click="detailDialog = false">取消</el-button>
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