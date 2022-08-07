import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isLogin: false,
        loginDialog: false,
        loginDialogClose: false,
        drawer: null
    },
    mutations: {
        setLoginStatus(state, flag) {
            state.isLogin = flag;
        },
        setLoginDialog(state, flag) {
            state.loginDialog = flag;
        },
        setLoginDialogClose(state, flag) {
            state.loginDialogClose = flag;
        },
        setDrawer(state) {
            state.drawer = !state.drawer;
        }
    },
    actions: {},
    modules: {}
})
