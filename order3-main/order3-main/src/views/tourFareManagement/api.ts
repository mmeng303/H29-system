import request from "@/api/api";
import { PayData } from "./util";

export function getTourFareList(data: PayData): Promise<any> {
    return request({
        url: '/api/activity-fee/paymentPage',
        method: 'post',
        data
    })
}

export function exportTourFareList(): Promise<any> {
    return request({
        url: '/api/activity-fee/export',
        method: 'post',
        responseType: 'blob'
    })
}

export function ReportTourFareApi(id: number): Promise<any> {
    return request({
        url: '/api/activity-fee/wordExport/' + id,
        method: 'post',
        responseType: 'blob'
    })
}