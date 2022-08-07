import Vue from 'vue';
import Vuetify, {VIcon, VSnackbar} from 'vuetify/lib';
import VuetifyToast from 'vuetify-toast-snackbar-ng'
import zhHans from 'vuetify/es5/locale/zh-Hans'

Vue.use(Vuetify, {
    components: {
        VIcon,
        VSnackbar
    }
});

Vue.use(VuetifyToast, {
    x: 'null',
    y: 'bottom',
    classes: [
        'body-2'
    ],
    timeout: 1500,
    property: '$toast',
    shorts: {
        success: {
            icon: 'mdi-check',
            color: 'success'
        },
        info: {
            icon: 'mdi-information-outline',
            color: 'info'
        },
        error: {
            icon: 'mdi-cancel',
            color: 'error'
        },
        warning: {
            icon: 'mdi-alert-outline',
            color: 'warning'
        }
    },
})

export default new Vuetify({
    theme: {
        themes: {
            light: {
                primary: "#ff0000",
                // primary: "#FF6888",
                // primary: "#00a9ff",
            },
            dark:{
                // primary: "#ff9a00",
            }
        },
    },
    lang: {
        locales: {zhHans},
        current: 'zhHans',
    },
});