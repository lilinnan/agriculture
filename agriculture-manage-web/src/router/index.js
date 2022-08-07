import Vue from 'vue'
import VueRouter from 'vue-router'
import Test1 from "../views/admininfo/Test1";
import Test2 from "../views/admininfo/Test2";
import Test3 from "../views/admininfo/Test3";
import Home from "../views/Home";
import Login from "../views/Login";
import AdminInfo from "../views/admininfo/AdminInfo";
import Authority from "../views/authority/Authority";
import Role from "../views/authority/Role";
import AdminUser from "../views/authority/AdminUser";
import Category from "../views/item/Category";
import Brand from "../views/item/Brand";
import Spu from "../views/item/Spu";
import SpuRecycleBin from "../views/item/SpuRecycleBin";
import User from "../views/user/User";
import Logistics from "../views/logistics/Logistics";
import ToBeDeliveredOrder from "../views/order/ToBeDeliveredOrder";
import ToBeReceiveOrder from "../views/order/ToBeReceiveOrder";
import PendingPaymentOrder from "../views/order/PendingPaymentOrder";
import AllOrder from "../views/order/AllOrder";
import CompletedOrder from "../views/order/CompletedOrder";
import Animal from "../views/agriculture/Animal";
import Plant from "../views/agriculture/Plant";
import EntityInfo from "../views/agriculture/EntityInfo";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {path: '/test1', name: 'Test1', component: Test1,},
            {path: '/test2', name: 'Test2', component: Test2,},
            {path: '/test3', name: 'Test3', component: Test3,},
            {path: '/admin-info', name: 'AdminInfo', component: AdminInfo,},
            {path: '/authority', name: 'Authority', component: Authority,},
            {path: '/role', name: 'Role', component: Role,},
            {path: '/admin-user', name: 'AdminUser', component: AdminUser,},
            {path: '/item/category', name: 'Category', component: Category,},
            {path: '/item/brand', name: 'Brand', component: Brand,},
            {path: '/item/spu', name: 'Spu', component: Spu,},
            {path: '/item/spu-recycle-bin', name: 'SpuRecycleBin', component: SpuRecycleBin,},
            {path: '/user', name: 'User', component: User,},
            {path: '/logistics', name: 'Logistics', component: Logistics,},
            {path: '/order/to-be-delivered-order', name: 'ToBeDeliveredOrder', component: ToBeDeliveredOrder,},
            {path: '/order/to-be-receive-order', name: 'ToBeReceiveOrder', component: ToBeReceiveOrder,},
            {path: '/order/pending-payment-order', name: 'PendingPaymentOrder', component: PendingPaymentOrder,},
            {path: '/order/all-order', name: 'AllOrder', component: AllOrder,},
            {path: '/order/completed-order', name: 'CompletedOrder', component: CompletedOrder,},
            {path: '/agriculture/animal', name: 'Animal', component: Animal,},
            {path: '/agriculture/plant', name: 'Plant', component: Plant,},
        ],
    }, {
        path: "/login",
        name: "Login",
        component: Login,
    }
]

const router = new VueRouter({
    routes
})

// 挂载路由导航守卫,to表示将要访问的路径，from表示从哪里来，next是下一个要做的操作 next('/login')强制跳转login
router.beforeEach((to, from, next) => {
    // 访问登录页，放行
    if (to.path === '/login') {
        return next();
    }
    // 获取token
    const authorization = window.localStorage.getItem('Authorization')
    // 没有token, 强制跳转到登录页
    if (!authorization) {
        return next('/login');
    }
    next()
})

export default router
