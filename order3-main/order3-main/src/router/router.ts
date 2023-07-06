import { createRouter, createWebHistory } from "vue-router"


interface RouteConfig {
    path: string;
    name: string;
    component: any;
    meta?: any;
    children?: any;
}

const routes: Array<RouteConfig> = [
    {
        path: '/',
        name: 'Home',
        component: () => import("@/views/home/index.vue"),
        meta: {
            title: "首页",
            keepAlive: false,
        },
        children: [
            // userManagement
            {
                path: '/userManagement',
                name: 'UserManagement',
                component: () => import("@/views/userManagement/index.vue"),
                meta: {
                    title: "用户管理",
                    keepAlive: false,
                }
            },
            // teamManagement
            {
                path: '/teamManagement',
                name: 'TeamManagement',
                component: () => import("@/views/teamManagement/index.vue"),
                meta: {
                    title: "团队管理",
                    keepAlive: false,
                }
            },
            // activityManagement
            {
                path: '/activityManagement',
                name: 'ActivityManagement',
                component: () => import("@/views/activityManagement/index.vue"),
                meta: {
                    title: "活动管理",
                    keepAlive: false,
                }
            },
            // tourFareManagement
            {
                path: '/tourFareManagement',
                name: 'TourFareManagement',
                component: () => import("@/views/tourFareManagement/index.vue"),
                meta: {
                    title: "团费管理",
                    keepAlive: false,
                }
            },
            // userTeamManagement
            {
                path: '/userTeamManagement',
                name: 'UserTeamManagement',
                component: () => import("@/views/userTeamManagement/index.vue"),
                meta: {
                    title: "用户团队管理",
                    keepAlive: false,
                }
            },
            // toBeAccepted
            {
                path: '/toBeAccepted',
                name: 'ToBeAccepted',
                component: () => import("@/views/toBeAccepted/index.vue"),
                meta: {
                    title: "待接邀请",
                    keepAlive: false,
                }
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/login/index.vue"),
        meta: {
            title: "登录",
            keepAlive: true,
        }
    },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
    // 如果是登录页面，直接放行
    document.title = to.meta.title as string;
    if (to.path == "/login") {
        next();
        return;
    }
    // 如果不是登录页面，判断是否有token
    const token = localStorage.getItem("token");
    if (token) {
        if (to.path == "/") {
            let authority = localStorage.getItem("authority");
            if (authority == "1") {
                next("/userManagement");
            } else {
                next("/userTeamManagement")
            }
            return;
        }
        next();
        return;
    }
    // 如果没有token，跳转到登录页面
    next("/login");
})


export default router