<template xmlns="">
  <v-app id="inspire">
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col cols="12" sm="8" md="4">
            <v-card class="elevation-12">
              <v-toolbar color="primary" dark flat>
                <v-toolbar-title>登录</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form ref="loginForm">
                  <v-text-field label="用户名" name="username" prepend-icon="mdi-account" type="text"
                                v-model="loginInfo.username" :rules="rules.usernameRule" autocomplete="off" required/>
                  <v-text-field id="password" label="密码"
                                name="password" prepend-icon="mdi-lock" type="password" v-model="loginInfo.password"
                                :rules="rules.passwordRule" autocomplete="off" required/>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" @click="validate">登录</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>


<script>
export default {
  data: () => ({
    loginInfo: {
      username: "",
      password: "",
      token: ""
    },
    vaptcha: {
      v: undefined
    },
    rules: {
      usernameRule: [
        v => !!v || '请输入用户名',
        v => /^[\w]{5,16}$/.test(v) || '用户名不合法',
      ],
      passwordRule: [
        v => !!v || '请输入密码'
      ]
    }
  }),
  methods: {
    validate: function () {
      if (this.vaptcha.v === undefined) {
        this.$toast.info('验证码加载中');
        return;
      }
      if (!this.$refs.loginForm.validate()) {
        return;
      }
      this.vaptcha.v.validate();
    },
    login: function () {
      this.$http.post("login", this.loginInfo).then(
          value => {
            if (value.status === 200) {
              window.localStorage.setItem("Authorization", value.data);
              this.$toast.success("登录成功");
              this.$router.push('/')
              return;
            }
            this.$toast.error(value['message']);
            this.vaptcha.v.reset();
          }
      )
    },
    initVaptcha: function () {
      //初始化验证码
      let v = this.vaptcha;
      let loginInfo = this.loginInfo;
      let loginFunction = this.login;
      this.$vcaptcha.loadSdk(function () {
        vaptcha({
          vid: '5f4c9786a16bb06a276bbe20',
          type: 'invisible',
          scene: 1,
          offline_server: "",
          lang: 'zh-CN',
          https: false,
          style: 'dark',
          color: '#57ABFF'
        }).then(function (vaptchaObj) {
          v.v = vaptchaObj;
          vaptchaObj.listen('pass', function () {
            loginInfo.token = vaptchaObj.getToken();
            loginFunction();
          });
        })
      })
    }

  },
  name: "Login",
  props: {
    source: String,
  },
  mounted() {
    this.initVaptcha();
    const theme = window.localStorage.getItem("theme");
    if (theme === 'dark') {
      this.$vuetify.theme.dark = true;
    }
    this.$root['editorKey'] = this.$root['editorKey'] + 1;
  }
}
</script>
