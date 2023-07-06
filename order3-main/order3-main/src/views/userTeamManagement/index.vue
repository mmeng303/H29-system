<template>
    <h2>
        团队管理
    </h2>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <el-table :data="tableData" border stripe>
        <el-table-column label="序号" type="index" align="center" width="120"></el-table-column>
        <el-table-column label="团队名称" prop="teamName" align="center"></el-table-column>
        <el-table-column label="创建时间" prop="realTimeCostFee" align="center">
            <template #default="{ row }">
                {{ new Date(row.creationTime).toLocaleString() }}
            </template>
        </el-table-column>
        <!-- <el-table-column label="操作" prop="startingPoint" align="center">
            <template #default="{ row }">
                <el-button @click="viewActivity(row)" type="primary" size="small">查看团员</el-button>
            </template>
        </el-table-column> -->
    </el-table>
    <div class="pagin">
        <el-pagination @current-change="pageChange" small background layout="prev, pager, next" :total="total" />
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import request from "@/api/api";
const tableData = ref<any[]>([]);
const userId = localStorage.getItem('userId')
// const activityDialog = ref(false)
const page = ref(1)
const limit = ref(10)
const total = ref(0)
onMounted(async () => {
    await getData()
});

const getData = async () => {
    await request({
        url: '/api/team-member/getListOfJoin',
        method: 'post',
        data: {
            userId: Number(userId),
            page: page.value,
            limit: limit.value
        }
    }).then(res => {
        console.log(res)
        tableData.value = res.data.data
    }).catch(err => {
        console.log(err)
    })
}
// const viewActivity = () => {
//     activityDialog.value = true
// }
const pageChange = (val: any) => {
    page.value = val
    getData()
}
</script>

<style scoped></style>