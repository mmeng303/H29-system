<template>
    <h2>
        团费管理
    </h2>
    <!-- 分割线 -->
    <el-divider></el-divider>
    <div class="menu">
        <el-button @click="exportTourFare" type="primary">导出</el-button>
    </div>
    <div class="content">
        <el-table :data="tableData" border stripe>
            <el-table-column label="序号" type="index" align="center" width="120"></el-table-column>
            <el-table-column label="活动名称" prop="activityName" align="center" width="180"></el-table-column>
            <el-table-column label="活动AA费用" prop="aaFee" align="center" width="180"></el-table-column>
            <el-table-column label="个人费用" prop="activityFee" align="center" width="180"></el-table-column>
            <el-table-column label="总计费用" prop="realTimeCostFee" align="center" width="180"></el-table-column>
            <el-table-column label="操作" prop="startingPoint" align="center">
                <template #default="{ row }">
                    <el-button @click="reportTourFare(row)" type="primary" size="small">生成报告</el-button>
                    <!-- 删除 -->
                    <el-button type="danger" size="small" @click="deleteTourFare(row)">删除</el-button>
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
import { exportTourFareList, getTourFareList, ReportTourFareApi } from './api';
import { PayData, tourFareData } from './util';


const userId = localStorage.getItem('userId') ? Number(localStorage.getItem('userId')) : 0;
const page = ref(1);
const limit = ref(10);
const total = ref(0);
const tableData = ref<tourFareData[]>([]);
onMounted(async () => {
    await getData();
});

const getData = async () => {
    let reqData: PayData = {
        memberId: userId,
        page: page.value,
        limit: limit.value,
    }
    await getTourFareList(reqData).then(res => {
        tableData.value = res.data.data;
        total.value = res.data.total;
    })
}
const exportTourFare = async () => {
    await exportTourFareList().then(res => {
        console.log(res);
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' })
        const downloadElement = document.createElement('a')
        const href = window.URL.createObjectURL(blob) // 创建下载的链接
        downloadElement.href = href
        downloadElement.download = `团费.xlsx` // 下载后文件名
        document.body.appendChild(downloadElement)
        downloadElement.click() // 点击下载
        document.body.removeChild(downloadElement) // 下载完成移除元素
        window.URL.revokeObjectURL(href) // 释放掉blob对象
    })
}
const reportTourFare = async (row: tourFareData) => {
    await ReportTourFareApi(row.id).then((res: BlobPart) => {
        console.log(res);
        const blob = new Blob([res], { type: 'application/msword' })
        const downloadElement = document.createElement('a')
        const href = window.URL.createObjectURL(blob) // 创建下载的链接
        downloadElement.href = href
        downloadElement.download = `${row.activityName}报告.docx` // 下载后文件名
        document.body.appendChild(downloadElement)
        downloadElement.click() // 点击下载
        document.body.removeChild(downloadElement) // 下载完成移除元素
        window.URL.revokeObjectURL(href) // 释放掉blob对象
    })
}
const deleteTourFare = (row: tourFareData) => {
    console.log(row);
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