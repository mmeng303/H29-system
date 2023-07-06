<template>
    <h2>待接受的邀请</h2>
    <el-divider></el-divider>
    <div class="conetnt">
        <el-table :data="tableData" border stripe>
            <el-table-column label="序号" type="index" align="center" width="120"></el-table-column>
            <el-table-column label="团队名称" prop="teamName" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="realTimeCostFee" align="center">
                <template #default="{ row }">
                    {{ new Date(row.creationTime).toLocaleString() }}
                </template>
            </el-table-column>
            <el-table-column label="操作" prop="startingPoint" align="center">
                <template #default="{ row }">
                    <el-button @click="confirmAddTeam(row)" type="primary" size="small">确认加入</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagin">
            <el-pagination @current-change="pageChange" small background layout="prev, pager, next" :total="total" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import request from "@/api/api";
import { ElMessage } from "element-plus/lib/components/index.js";
const userId = localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : 0;
const page = ref(1);
const limit = ref(10);
const total = ref(0);
const tableData = ref<any[]>([]);
onMounted(async () => {
    await getData();
});

const getData = async () => {
    let reqData: any = {
        page: page.value,
        limit: limit.value,
        userId: userId
    }
    await request({
        url: '/api/team-member/getListOfNoJoin',
        method: 'post',
        data: reqData
    }).then((res: { data: { data: any[]; total: number; }; }) => {
        tableData.value = res.data.data;
        total.value = res.data.total;
    })
}
const confirmAddTeam = async (row: any) => {
    console.log(row);
    await request({
        url: "/api/team-member/join",
        method: "post",
        data: {
            teamId: row.teamId,
            userId: userId
        }
    }).then(res => {
        // @ts-ignores
        if (res.code === 0) {
            getData();
            // @ts-ignore
            ElMessage.success(res.msg);
        } else {
            // @ts-ignore
            ElMessage.error(res.msg);
        }
    })
}
const pageChange = (val: number) => {
    page.value = val;
    getData();
}
</script>

<style scoped>
.content {
    margin-top: 20px;
}
</style>