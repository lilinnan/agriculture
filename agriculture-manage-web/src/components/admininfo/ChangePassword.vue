<template>
  <v-row justify="center">
    <v-dialog v-model="value" persistent max-width="400">
      <v-card>
        <v-card-title class="headline">修改密码</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="form">
              <v-text-field
                  type="password"
                  v-model="password.oldPassword"
                  :rules="oldPasswordRule"
                  autocomplete="off"
                  label="原密码"
                  required
              ></v-text-field>
              <v-text-field
                  type="password"
                  v-model="password.newPassword"
                  :rules="newPasswordRule"
                  autocomplete="off"
                  label="新密码"
                  required
              ></v-text-field>
              <v-text-field
                  type="password"
                  v-model="password.reNewPassword"
                  :rules="reNewPasswordRule.concat(passwordConfirmationRule)"
                  autocomplete="off"
                  label="重复密码"
                  required/>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="reset">取消</v-btn>
          <v-btn color="primary" text @click="change">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import router from "@/router";

export default {
  props: {
    value: Boolean
  },
  name: "ChangePassword",
  data: () => ({
    dialog: false,
    password: {
      oldPassword: '',
      newPassword: '',
      reNewPassword: ''
    },
    validPassword: "",
    oldPasswordRule: [
      v => !!v || '请输入原密码',
    ], newPasswordRule: [
      v => !!v || '请输入新密码',
      v => /^[\w]{6,16}$/.test(v) || '新密码必须由数字、字母或下划线组成，长度在6~16位之间',
    ], reNewPasswordRule: [
      v => !!v || '请重复您的密码',
    ],
  }),
  methods: {
    change() {
      if (!this.$refs.form.validate()) {
        this.$toast.error("请检查您输入的数据");
        return;
      }
      this.$http.put("admin-info/password/", this.password).then(value => {
        if (value.status === 200) {
          this.$toast.success(value['message']);
          this.reset();
          localStorage.removeItem("Authorization")
          router.push('/login').then();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    reset() {
      this.$emit("input", false);
      this.password.oldPassword = "";
      this.password.newPassword = "";
      this.password.reNewPassword = "";
      this.$refs.form.reset()
    },
  },
  computed: {
    passwordConfirmationRule() {
      if (this.password.newPassword === this.password.reNewPassword) {
        return true;
      }
      return "两次密码必须相同";
    },
  }
}
</script>

<style scoped>

</style>