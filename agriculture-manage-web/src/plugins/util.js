import Vue from 'vue';
import jwt_decode from "jwt-decode";

Vue.prototype.$search = function getDataFromApi(url, option, key, other) {
    let sort = "";
    let sortBy = option.sortBy;
    let sortDesc = option.sortDesc;
    for (let i = 0; i < sortBy.length; i++) {
        sort += sortDesc[i] ? "+" : "-";
        sort += sortBy[i];
    }
    let params = {
        pageNum: option.page,
        pageSize: option.itemsPerPage,
    };
    if (sort !== "") {
        Object.assign(params, {sort: sort});
    }
    if (key !== undefined) {
        Object.assign(params, {key: key});
    }
    if (other !== undefined) {
        Object.assign(params, other);
    }
    return this.$http.get(url, {
        params: params
    })
}


Vue.prototype.$toDecimal2 = function (x) {
    x /= 100
    x = x + '';
    if (x.indexOf('.') === -1) {
        return x + '.00';
    }
    let n = 3 - (x.length - x.indexOf('.'));
    for (let i = 0; i < n; i++) {
        x += '0';
    }
    return x;
}


const all = [
    {
        name: "我的信息", icon: "mdi-pine-tree-box", url: "", data: [
            {
                name: "我的信息", url: "/admin-info", data: []
            }
        ]
    }, {
        name: "农业信息", icon: "mdi-pine-tree-box", url: "", data: [
            {
                name: "植物信息",
                url: "/agriculture/plant",
                authority: ['AGRICULTURE_ENTITY_INSERT', 'AGRICULTURE_ENTITY_DELETE', 'AGRICULTURE_ENTITY_UPDATE', 'AGRICULTURE_ENTITY_SELECT',
                    'AGRICULTURE_CLASSIFY_INSERT', 'AGRICULTURE_CLASSIFY_DELETE', 'AGRICULTURE_CLASSIFY_UPDATE', 'AGRICULTURE_CLASSIFY_SELECT',
                    'AGRICULTURE_DETAIL_INSERT', 'AGRICULTURE_DETAIL_DELETE', 'AGRICULTURE_DETAIL_UPDATE', 'AGRICULTURE_DETAIL_SELECT'],
                data: []
            }, {
                name: "动物信息",
                url: "/agriculture/animal",
                authority: ['AGRICULTURE_ENTITY_INSERT', 'AGRICULTURE_ENTITY_DELETE', 'AGRICULTURE_ENTITY_UPDATE', 'AGRICULTURE_ENTITY_SELECT',
                    'AGRICULTURE_CLASSIFY_INSERT', 'AGRICULTURE_CLASSIFY_DELETE', 'AGRICULTURE_CLASSIFY_UPDATE', 'AGRICULTURE_CLASSIFY_SELECT',
                    'AGRICULTURE_DETAIL_INSERT', 'AGRICULTURE_DETAIL_DELETE', 'AGRICULTURE_DETAIL_UPDATE', 'AGRICULTURE_DETAIL_SELECT'],
                data: []
            },
        ]
    }, {
        name: "权限管理", icon: "mdi-pine-tree-box", url: "", data: [
            {
                name: "权限信息", url: "/authority", authority: ['AUTHORITY_SELECT'], data: []
            }, {
                name: "角色信息",
                url: "/role",
                authority: ['ROLE_INSERT', 'ROLE_DELETE', 'ROLE_UPDATE', 'ROLE_SELECT'],
                data: []
            }, {
                name: "管理员信息",
                url: "/admin-user",
                authority: ['ADMIN_USER_INSERT', 'ADMIN_USER_DELETE', 'ADMIN_USER_UPDATE', 'ADMIN_USER_SELECT'],
                data: []
            }
        ]
    }, {
        name: "商品管理", icon: "mdi-pine-tree-box", url: "", data: [
            {
                name: "分类信息",
                url: "/item/category",
                authority: ['CATEGORY_INSERT', 'CATEGORY_DELETE', 'CATEGORY_UPDATE', 'CATEGORY_SELECT'],
                data: []
            }, {
                name: "品牌信息",
                url: "/item/brand",
                authority: ['BRAND_INSERT', 'BRAND_DELETE', 'BRAND_UPDATE', 'BRAND_SELECT'],
                data: []
            }, {
                name: "商品管理",
                url: "/item/spu",
                authority: ['SPU_INSERT', 'SPU_DELETE', 'SPU_UPDATE', 'SPU_SELECT'],
                data: []
            }, {
                name: "商品回收站",
                url: "/item/spu-recycle-bin",
                authority: ['RECYCLE_BIN_DELETE', 'RECYCLE_BIN_RESTORE', 'RECYCLE_BIN_SELECT'],
                data: []
            }
        ]
    }, {
        name: "用户管理", icon: "mdi-pine-tree-box", url: "/user", authority: ['USER_UPDATE', 'USER_SELECT'], data: []
    }, {
        name: "物流管理",
        icon: "mdi-pine-tree-box",
        url: "/logistics",
        authority: ['LOGISTICS_COMPANY_INSERT', 'LOGISTICS_COMPANY_DELETE', 'LOGISTICS_COMPANY_UPDATE', 'LOGISTICS_COMPANY_SELECT'],
        data: []
    }, {
        name: "订单管理", icon: "mdi-pine-tree-box", url: "", data: [
            {
                name: "待支付", url: "/order/pending-payment-order", authority: ['ORDER_UPDATE', 'ORDER_SELECT'], data: []
            }, {
                name: "待发货", url: "/order/to-be-delivered-order", authority: ['ORDER_UPDATE', 'ORDER_SELECT'], data: []
            }, {
                name: "待收货", url: "/order/to-be-receive-order", authority: ['ORDER_UPDATE', 'ORDER_SELECT'], data: []
            }, {
                name: "已完成", url: "/order/completed-order", authority: ['ORDER_UPDATE', 'ORDER_SELECT'], data: []
            }, {
                name: "全部订单", url: "/order/all-order", authority: ['ORDER_UPDATE', 'ORDER_SELECT'], data: []
            }
        ]
    }
]


Vue.prototype.$getLeftMenu = function () {
    const item = window.localStorage.getItem('Authorization');
    if (!item) {
        return [];
    }
    let jwtDecode;
    try {
        jwtDecode = jwt_decode(item);
    } catch (e) {
        return [];
    }
    const authorities = jwtDecode['authorities'];
    //结果数组
    let result = [];
    for (let level1 of all) {
        if (level1.data.length === 0) {
            if (contains(authorities, level1.authority)) {
                result.push(level1);
            }
            continue;
        }
        let level1Data = [];
        for (let level2 of level1.data) {
            if (level2.data.length === 0) {
                if (contains(authorities, level2.authority)) {
                    level1Data.push(level2);
                }
                continue;
            }
            let level2Data = [];
            for (let level3 of level2.data) {
                if (contains(authorities, level3.authority)) {
                    level2Data.push(level3);
                }
            }
            if (level2Data.length > 0) {
                level1Data.push({name: level2.name, icon: level2.icon, url: level2.url, data: level2Data});
            }
        }
        if (level1Data.length > 0) {
            result.push({name: level1.name, icon: level1.icon, url: level1.url, data: level1Data});
        }
    }
    return result;
}

function contains(hasAuthorities, needAuthorities) {
    if (!needAuthorities) {
        return true;
    }
    for (let needAuthority of needAuthorities) {
        for (let hasAuthority of hasAuthorities) {
            if (needAuthority === hasAuthority) {
                return true;
            }
        }
    }
    return false;
}
