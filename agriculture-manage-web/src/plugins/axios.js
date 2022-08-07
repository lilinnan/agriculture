import Axios from "axios";
import router from "@/router";

Axios.interceptors.response.use(response => {
    return response.data;
}, error => {
    if (error.response.status === 401 && error.response.data.data === "tokenInvalid") {
        localStorage.removeItem("Authorization")
        router.push('/login').then();
    }
    return error.response.data;
});

Axios.interceptors.request.use(function (config) {
    let item = window.localStorage.getItem("Authorization");
    if (item) {
        config.headers.Authorization = item;
    }
    return config;
});


