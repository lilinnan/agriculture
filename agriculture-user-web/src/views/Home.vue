<template>
  <v-app>
    <v-app-bar app clipped-left color="primary">
      <v-app-bar-nav-icon @click.stop="$store.commit('setDrawer')" class="d-lg-none"></v-app-bar-nav-icon>
      <v-btn icon color="white" @click="back">
        <v-icon>mdi-keyboard-backspace</v-icon>
      </v-btn>
      <v-toolbar-title style="color: white;cursor: pointer" @click="toHome">农业大数据中心</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text @click="$router.replace('shop')" color="white">农药商城</v-btn>
      <v-btn icon color="white" @click="searchDialog=true">
        <v-icon>mdi-magnify</v-icon>
      </v-btn>
      <v-btn icon color="white" @click="switchTheme">
        <v-icon>{{ themeIcon }}</v-icon>
      </v-btn>
      <login></login>
      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn icon color="white" :class="{'d-none':!($store.state.isLogin)}" v-bind="attrs" v-on="on">
            <v-icon>mdi-menu</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item @click="toUserInfo">
            个人中心
          </v-list-item>
          <v-list-item @click="toUserOrder">
            我的订单
          </v-list-item>
          <v-list-item @click.stop="logoutDialog=true">
            退出登录
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <v-main>
      <v-container>
        <keep-alive>
          <router-view/>
        </keep-alive>
      </v-container>
    </v-main>

    <v-dialog v-model="logoutDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">提示</v-card-title>
        <v-card-text>您确定要退出登录吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="logout">确定</v-btn>
          <v-btn color="primary" text @click="logoutDialog = false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


    <v-dialog v-model="searchDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">搜索</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form">
              <v-text-field
                  type="text"
                  autocomplete="off"
                  label="请输入您要搜索的内容"
                  v-model="key"
                  required
              ></v-text-field>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="searchDialog = false">取消</v-btn>
          <v-btn color="primary" text @click="search">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-app>
</template>

<script>
import Login from "../components/Login";

export default {
  components: {Login},
  data: () => ({
    searchDialog: false,
    key: '',
    userinfo: {
      username: '',
      avatar: ''
    },
    urlHistory: new Set(),
    urlToName: new Map(),
    drawer: null,
    mini: false,
    themeIcon: "mdi-moon-waxing-crescent",
    logoutDialog: false,
  }),
  methods: {
    switchTheme: function () {
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
      if (this.$vuetify.theme.dark) {
        this.themeIcon = "mdi-weather-sunny";
        window.localStorage.setItem("theme", "dark");
        return;
      }
      this.themeIcon = "mdi-moon-waxing-crescent";
      window.localStorage.setItem("theme", "sunny");
    },
    logout: function () {
      this.logoutDialog = false;
      this.$store.commit("setLoginStatus", false);
      this.$toast.success("注销成功");
      this.$router.replace("/")
      window.localStorage.removeItem("Authorization")
    },
    toUserInfo() {
      this.$router.push("user-info")
    },
    toUserOrder() {
      this.$router.push("user-order")
    },
    toHome() {
      if (this.$route.path !== '/') {
        this.$router.push("/")
      }
    },
    back() {
      this.$router.back();
    },
    refresh() {
      this.$router.replace("blank");
    },
    search() {
      let refresh = this.$route.path === '/search';
      this.$router.replace("/search?key=" + this.key);
      if (refresh) {
        this.refresh();
      }
      this.key = '';
      this.searchDialog = false;
    }
  },
  mounted() {
    const theme = window.localStorage.getItem("theme");
    if (theme === 'dark') {
      this.$vuetify.theme.dark = false;
      this.switchTheme();
    }
    if (window.localStorage.getItem("Authorization")) {
      this.$store.commit("setLoginStatus", true);
    }
  },
}
</script>
<style lang="less">
::-webkit-scrollbar {
  /*隐藏滚轮*/
  display: none;
}

</style>