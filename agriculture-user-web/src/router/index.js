import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Shop from "../views/shop/Shop";
import MailCheck from "../views/shop/MailCheck";
import FindPassword from "../views/shop/FindPassword";
import SpuDetail from "../views/shop/SpuDetail";
import UserInfo from "../views/shop/UserInfo";
import Buy from "../views/shop/Buy";
import PayResult from "../views/shop/PayResult";
import UserOrder from "../views/shop/UserOrder";
import OrderDetail from "../views/shop/OrderDetail";
import store from "../store"
import Blank from "../views/shop/Blank";
import Search from "../views/shop/Search";
import Main from "@/views/shop/Main";
import Plant from "@/views/shop/agriculture/Plant";
import Animal from "@/views/shop/agriculture/Animal";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        component: Home,
        children: [
            {
                path: '/',
                name: 'Main',
                component: Main,
                children: [
                    {
                        path: '/',
                        name: 'Plant',
                        component: Plant,
                    }, {
                        path: '/animal',
                        name: 'Animal',
                        component: Animal,
                    },
                ]
            }, {
                path: '/shop',
                name: 'Shop',
                component: Shop,
            }, {
                path: '/mail-check',
                name: 'MailCheck',
                component: MailCheck,
            }, {
                path: '/find-password',
                name: 'FindPassword',
                component: FindPassword,
            }, {
                path: '/spu-detail',
                name: 'SpuDetail',
                component: SpuDetail,
            }, {
                path: '/user-info',
                name: 'UserInfo',
                component: UserInfo,
            }, {
                path: '/buy',
                name: 'Buy',
                component: Buy,
            }, {
                path: '/pay-result',
                name: 'PayResult',
                component: PayResult,
            }, {
                path: '/user-order',
                name: 'UserOrder',
                component: UserOrder,
            }, {
                path: '/order-detail',
                name: 'OrderDetail',
                component: OrderDetail,
            }, {
                path: '/blank',
                name: 'Blank',
                component: Blank,
            }, {
                path: '/search',
                name: 'Search',
                component: Search,
            }
        ]
    },
]

const router = new VueRouter({
    routes
})

router.afterEach((to, from) => {
    setTimeout(() => {
        document.documentElement.scrollTop = 0
    }, 0)
});


// 挂载路由导航守卫,to表示将要访问的路径，from表示从哪里来，next是下一个要做的操作 next('/login')强制跳转login
router.beforeEach((to, from, next) => {
    store.commit("setLoginDialog", false);
    // 访问非受限资源，允许
    if (to.path === '/' ||
        to.path === '/spu-detail' ||
        to.path === '/mail-check' ||
        to.path === '/find-password' ||
        to.path === '/shop' ||
        to.path === '/animal' ||
        to.path === '/pay-result') {
        return next();
    }
    // 获取token
    const authorization = window.localStorage.getItem('Authorization')
    // 没有token, 请求登录
    if (!authorization) {
        store.commit("setLoginDialogClose", true);
        store.commit("setLoginDialog", true);
        if (from.path === '/') {
            next('/')
        }
        return;
    }
    next()
})


export default router
