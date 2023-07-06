import request from "@/api/api";
import { QueryTeamListData, QueryTeamListResponse } from "./util";
import { Activity } from '@/views/activityManagement/util';


export function QueryTeamListApi(queryTeamListData: QueryTeamListData): Promise<QueryTeamListResponse> {
    return request({
        url: "/api/team/list",
        method: "post",
        data: queryTeamListData
    })
}

export function AddTeamApi(teamName: string, teamId?: number | undefined): Promise<any> {
    return request({
        url: "/api/team/saveOrUpdateTeam",
        method: "post",
        data: {
            teamName,
            teamId
        }
    })
}

export function QueryTeamNameApi(teamName: string, page: number, limit: number): Promise<any> {
    return request({
        url: "/api/team/list",
        method: "post",
        data: {
            teamName,
            page,
            limit
        }
    })
}

export function ReleaseTeamApi(params: Activity): Promise<any> {
    return request({
        url: '/api/activity/release',
        method: 'post',
        data: params
    })
}

export function QueryUserApi(): Promise<any> {
    return request({
        url: "/api/user/listTeamMember",
        method: "post",
    })
}

export function InviteMemberApi(teamId: number, userId: number): Promise<any> {
    return request({
        url: "/api/team-member/invite",
        method: "post",
        data: {
            teamId,
            userId
        }
    })
}