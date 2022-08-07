import Axios from "axios";
import store from "../store";

Axios.interceptors.request.use(function (config) {
  let item = window.localStorage.getItem("Authorization");
  if (item) {
    config.headers.Authorization = item;
  }
  return config;
});

Axios.interceptors.response.use(response => {
  return response.data;
}, error => {
  //发现过期了，重新请求登录
  if (error.response.status === 401 && error.response.data.data === "tokenInvalid") {
    localStorage.removeItem("Authorization")
    store.commit("setLoginDialog", true);
    store.commit("setLoginStatus", false);
    store.commit("setLoginDialogClose", false);
  }
  return error.response.data;
})
