<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" :mini-variant.sync="mini" app clipped>
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-img :src="$root['userInfo'].avatar"></v-img>
        </v-list-item-avatar>
        <v-list-item-title>{{ $root['userInfo'].username }}</v-list-item-title>
        <v-btn icon @click.stop="mini = !mini" class="d-none d-lg-flex">
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
      </v-list-item>
      <v-divider/>
      <navigation :data="left"/>
    </v-navigation-drawer>

    <v-app-bar app clipped-left color="primary">
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" class="d-lg-none"></v-app-bar-nav-icon>
      <v-toolbar-title style="color: white">后台管理</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon color="white" @click="switchTheme">
        <v-icon>{{ themeIcon }}</v-icon>
      </v-btn>
      <v-btn icon color="white" @click.stop="logoutDialog=true">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>
    <v-main>
      <div class="d-none d-lg-flex">
        <v-tabs height="56" ref="historyTabs">
          <v-chip class="align-self-center ml-2 mr-2"
                  active-class="primary"
                  close
                  v-for="data in urlHistory"
                  link :to="data"
                  @click:close="close(data)">
            {{ urlToName.get(data) }}
          </v-chip>
        </v-tabs>
      </div>
      <v-divider/>
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
  </v-app>
</template>

<script>
import Navigation from "../components/Navigation";

export default {
  components: {Navigation},
  data: () => ({
    userinfo: {
      username: '',
      avatar: ''
    },
    urlHistory: new Set(),
    urlToName: new Map(),
    drawer: null,
    mini: false,
    left: [],
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
    close: function (test) {
      if (this.$route.path === test) {
        return;
      }
      this.urlHistory.delete(test);
      let temp = new Set();
      for (let i of this.urlHistory) {
        temp.add(i);
      }
      this.urlHistory = temp;
    },
    /**
     * 将从后台获取到的路由列表转换为键值对
     */
    initUrlToName: function () {
      for (let i = 0; i < this.left.length; i++) {
        let level1 = this.left[i];
        if (level1.data.length === 0) {
          this.addUrlToName(level1.url, level1.name);
          continue;
        }
        for (let j = 0; j < level1.data.length; j++) {
          let level2 = level1.data[j];
          if (level2.data.length === 0) {
            this.addUrlToName(level2.url, level2.name);
            continue;
          }
          for (let k = 0; k < level2.data.length; k++) {
            let level3 = level2.data[k];
            this.addUrlToName(level3.url, level3.name);
          }
        }
      }
    },
    /**
     * 存储url到键值对，与initUrlToName共同使用
     * @param url url
     * @param name 界面名称
     */
    addUrlToName: function (url, name) {
      if (url.indexOf("/") !== 0) {
        url = "/" + url;
      }
      this.urlToName.set(url, name);
    },
    /**
     * 切换到新的页面时需要干的事情
     * @param newPath
     */
    toNewView: function (newPath) {
      if (newPath === "/") {
        return;
      }
      this.urlHistory.add(newPath);
    },
    logout: function () {
      window.localStorage.removeItem("Authorization");
      this.$router.push("/login");
      this.$toast.success("注销成功");
    }
  },
  mounted() {
    this.left=this.$getLeftMenu();
    this.initUrlToName();
    this.toNewView(this.$route.path)
    const theme = window.localStorage.getItem("theme");
    if (theme === 'dark') {
      this.$vuetify.theme.dark = false;
      this.switchTheme();
    }
    this.$http.get("admin-info/avatar").then(
        value => {
          if (value.status === 200) {
            this.$root['userInfo'].avatar = value.data.avatar;
            this.$root['userInfo'].username = value.data.username;
            return;
          }
          this.$toast.error(value['message']);
        }
    )
  },
  watch: {
    $route(to) {
      this.toNewView(to.path);
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