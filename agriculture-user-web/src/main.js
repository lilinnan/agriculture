import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import VueClipboard from 'vue-clipboard2'
import Axios from "axios";
import Vcaptcha from 'vaptcha-sdk-v3'
import './plugins/util'
import './plugins/axios.js'

Vue.use(VueClipboard)

Vue.config.productionTip = false
Vue.prototype.$http = Axios
Vue.prototype.$vcaptcha = Vcaptcha
Axios.defaults.baseURL = 'https://lilinnan.com/agriculture-user-api/v1/'
// Axios.defaults.baseURL = 'http:///192.168.77.119:8092/v1/'


const vue = new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
  methods: {
    test() {
      console.log(this.$http);
    }
  }
});
vue.$mount('#app')

