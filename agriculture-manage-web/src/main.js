import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import Axios from "axios";
import Vcaptcha from 'vaptcha-sdk-v3'
import './plugins/util'
import './plugins/axios.js'
import store from './store'


Vue.config.productionTip = false
Vue.prototype.$http = Axios
Vue.prototype.$vcaptcha = Vcaptcha
Axios.defaults.baseURL = 'https://lilinnan.com/agriculture-manage-api/v1/'
// Axios.defaults.baseURL = 'http://192.168.77.119:8091/v1/'


new Vue({
  router,
  vuetify,
  render: h => h(App),
  store,

  data: () => ({
    userInfo: {
      username: 'loading',
      avatar: ''
    },
    editorKey: 0
  })
}).$mount('#app')