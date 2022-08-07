<template>
  <v-dialog v-model="$store.state.loginDialog" persistent max-width="360">
    <template v-slot:activator="{ on, attrs }">
      <v-btn
          @click="$store.commit('setLoginDialogClose', true)"
          color="white"
          icon
          v-bind="attrs"
          v-on="on"
          :class="{'d-none':$store.state.isLogin}"
      >
        <v-icon>mdi-account</v-icon>
      </v-btn>
    </template>
    <v-card>
      <v-card-title class="headline">
        {{ title }}
        <v-spacer/>
        <v-btn v-if="$store.state.loginDialogClose" icon @click="$store.commit('setLoginDialog', false);;toLogin()">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-window v-model="step">
          <v-window-item :value="1">
            <v-form ref="loginForm">
              <v-text-field
                  v-model="loginFormData.username"
                  label="账号"
                  type="text"
                  :rules="rules.usernameRule"
              ></v-text-field>
              <v-text-field
                  v-model="loginFormData.password"
                  label="密码"
                  type="password"
                  :rules="rules.passwordRule"
              ></v-text-field>
              <v-btn width="100%" @click="validate('loginForm',login)">登录</v-btn>
            </v-form>
            <div class="login-footer">
              <v-btn text color="primary" @click="toRegister">注册</v-btn>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="toFindPassword">找回密码</v-btn>
            </div>
          </v-window-item>
          <v-window-item :value="2">
            <v-form ref="registerForm">
              <v-text-field
                  v-model="registerFormData.username"
                  label="账号"
                  type="text"
                  :rules="rules.registerUsernameRule"
              ></v-text-field>
              <v-text-field
                  v-model="registerFormData.email"
                  label="邮箱"
                  type="email"
                  :rules="rules.emailRule"
              ></v-text-field>
              <v-text-field
                  v-model="registerFormData.password"
                  label="密码"
                  type="password"
                  :rules="rules.registerPasswordRule"
              ></v-text-field>
              <v-text-field
                  v-model="registerFormData.rePassword"
                  label="确认密码"
                  type="password"
                  :rules="rules.registerRePasswordRule.concat(passwordConfirmationRule)"
              ></v-text-field>
              <v-btn width="100%" @click="validate('registerForm',register)">注册</v-btn>
            </v-form>
            <div class="login-footer">
              <v-btn text color="primary" @click="toLogin">登录</v-btn>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="toResendMail">重发注册邮件</v-btn>
            </div>
          </v-window-item>
          <v-window-item :value="3">
            <v-form ref="findPasswordForm">
              <v-text-field
                  v-model="findPasswordFormData.email"
                  label="请输入您的邮箱以找回密码"
                  type="email"
                  :rules="this.rules.emailRule"
              ></v-text-field>
              <v-btn width="100%" @click="validate('findPasswordForm',findPassword)">找回密码</v-btn>
            </v-form>
            <div class="login-footer">
              <v-btn text color="primary" @click="toLogin">登录</v-btn>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="toRegister">注册</v-btn>
            </div>
          </v-window-item>

          <v-window-item :value="4">
            <v-form ref="resendMailForm">
              <v-text-field
                  v-model="resendMailFormData.email"
                  label="请输入您注册时的邮箱"
                  type="email"
                  :rules="this.rules.emailRule"
              ></v-text-field>
              <v-btn width="100%" @click="validate('resendMailForm',resendMail)">重发邮件</v-btn>
            </v-form>
            <div class="login-footer">
              <v-btn text color="primary" @click="toLogin">登录</v-btn>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="toRegister">注册</v-btn>
            </div>
          </v-window-item>
        </v-window>
      </v-card-text>
    </v-card>


    <v-dialog v-model="resultDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">提示</v-card-title>
        <v-card-text>{{ result }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="resultDialog=false">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-dialog>
</template>

<script>
import Spu from "./Spu";

export default {
  name: "Login",
  components: {Spu},
  data: () => ({
    vaptcha: {
      v: undefined
    },
    resultDialog: false,
    result: '',
    step: 1,
    title: '登录',
    rules: {
      usernameRule: [
        v => !!v || '请输入用户名',
      ],
      passwordRule: [
        v => !!v || '请输入密码',
      ],
      registerUsernameRule: [
        v => !!v || '请输入用户名',
        v => /^[\w]{5,16}$/.test(v) || '用户名必须由数字、字母或下划线组成，长度在5~16位之间',
      ],
      registerPasswordRule: [
        v => !!v || '请输入密码',
        v => /^[\w]{6,16}$/.test(v) || '密码必须由数字、字母或下划线组成，长度在6~16位之间',
      ],
      registerRePasswordRule: [
        v => !!v || '请重复密码',
      ],
      emailRule: [
        v => !!v || '请输入邮箱',
        v => /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(v) || '请输入正确的邮箱',
      ]
    },
    loginFormData: {
      username: '',
      password: ''
    },
    registerFormData: {
      username: '',
      password: '',
      rePassword: '',
      email: ''
    },
    findPasswordFormData: {
      email: '',
    },
    resendMailFormData: {
      email: ''
    },
    pageToken: {
      token: ''
    },
    pageCallback: {
      callback: undefined
    }
  }),
  methods: {
    toLogin() {
      this.step = 1;
      this.title = "登录";
      this.resetAll();
    },
    toRegister() {
      this.step = 2;
      this.title = "注册";
      this.resetAll();
    },
    toFindPassword() {
      this.step = 3
      this.title = "找回密码";
      this.resetAll();
    },
    toResendMail() {
      this.step = 4;
      this.title = "邮件重发";
      this.resetAll();
    },
    validate(refName, callback) {
      if (!this.$refs[refName].validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      if (this.vaptcha.v === undefined) {
        this.$toast.info('验证码加载中');
        return;
      }
      // callback();
      this.pageCallback.callback = callback;
      this.vaptcha.v.validate();
    },
    login() {
      let o = this.loginFormData;
      Object.assign(o, {token: this.pageToken.token})
      this.$http.post("login", o).then(value => {
        if (value.status === 200) {
          this.$toast.success("登录成功");
          window.localStorage.setItem("Authorization", value.data);
          this.$store.commit("setLoginStatus", true);
          this.$store.commit("setLoginDialog", false);
          this.resetAll();
          if (!this.$store.state.loginDialogClose) {
            this.refresh();
          }
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    register() {
      let o = this.registerFormData;
      Object.assign(o, {token: this.pageToken.token})
      this.$http.post("user-info/register", o).then(value => {
        if (value.status === 200) {
          this.resultDialog = true;
          this.result = value['message'];
          this.$store.commit("setLoginDialog", false);
          this.resetAll();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    findPassword() {
      this.$http.get("user-info/password/find/" +
          this.findPasswordFormData.email +
          "/" + this.pageToken.token).then(value => {
        if (value.status === 200) {
          this.resultDialog = true;
          this.result = value['message'];
          this.$store.commit("setLoginDialog", false);
          this.resetAll();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    resendMail() {
      this.$http.get("user-info/register/" +
          this.resendMailFormData.email + "/" +
          this.pageToken.token).then(value => {
        if (value.status === 200) {
          this.resultDialog = true;
          this.result = value['message'];
          this.$store.commit("setLoginDialog", false);
          this.resetAll();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    resetAll() {
      this.$refs.loginForm && this.$refs.loginForm.reset();
      this.$refs.registerForm && this.$refs.registerForm.reset();
      this.$refs.findPasswordForm && this.$refs.findPasswordForm.reset();
      this.$refs.resendMailForm && this.$refs.resendMailForm.reset();
    },
    initVaptcha: function () {
      //初始化验证码
      let v = this.vaptcha;
      let tokenData = this.pageToken;
      let callback = this.pageCallback;
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
            tokenData.token = vaptchaObj.getToken();
            callback.callback();
            v.v.reset();
          });
        })
      })
    },
    refresh() {
      this.$router.replace("blank");
    }
  },
  computed: {
    passwordConfirmationRule() {
      if (this.registerFormData.password === this.registerFormData.rePassword) {
        return true;
      }
      return "两次密码必须相同";
    },
  },
  mounted() {
    this.initVaptcha();
  }
}
</script>

<style scoped>
.login-footer {
  margin-top: 10px;
  width: 100%;
  display: flex;
}
</style>