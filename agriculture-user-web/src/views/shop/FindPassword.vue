<template>
  <v-container>
    <v-dialog v-model="dialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">用户{{ this.username }}修改密码</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form">
              <v-text-field
                  v-model="password.password"
                  :rules="rules.passwordRule"
                  autocomplete="off"
                  label="新密码"
                  required
                  type="password"
              ></v-text-field>
              <v-text-field
                  v-model="password.rePassword"
                  :rules="rules.rePasswordRule.concat(passwordConfirmationRule)"
                  autocomplete="off"
                  label="重复密码"
                  type="password"
                  required/>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="change">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
import jwt_decode from "jwt-decode";

export default {
  name: "FindPassword",
  data: () => ({
    jwt: "",
    username: "",
    dialog: false,
    password: {
      password: '',
      rePassword: ''
    },
    rules: {
      passwordRule: [
        v => !!v || '请输入密码',
        v => /^[\w]{6,16}$/.test(v) || '新密码必须由数字、字母或下划线组成，长度在6~16位之间',
      ],
      rePasswordRule: [
        v => !!v || '请重复密码',
      ],
    },
  }),
  methods: {
    startChange() {
      this.jwt = this.$route.query.jwt;
      let decode;
      try {
        decode = jwt_decode(this.jwt);
      } catch (e) {
        this.$router.replace("/");
        return;
      }
      if (decode.sub !== 'find-password') {
        this.$router.replace("/");
        return;
      }
      this.username = decode.username;
      this.dialog = true;
    },
    change() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      let o = this.password;
      Object.assign(o, {token: this.jwt});
      this.$http.put("user-info/password/find", o).then(value => {
        if (value.status === 200) {
          this.$toast.success("密码修改成功");
          this.$router.replace("/");
          return;
        }
        this.$toast.error(value['message']);
      })
    }
  },
  computed: {
    passwordConfirmationRule() {
      if (this.password.password === this.password.rePassword) {
        return true;
      }
      return "两次密码必须相同";
    },
  },
  activated() {
    this.startChange();
  }
}
</script>


<style scoped>

</style>