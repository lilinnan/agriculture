<template>
  <v-container>
    <v-row>Hi~, {{ username }} ，正在验证您的邮件，请稍候...</v-row>

    <v-dialog v-model="dialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">提示</v-card-title>
        <v-card-text>Hi~, {{ username }}，{{ result }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="ok">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import jwt_decode from "jwt-decode";

export default {
  name: "MailCheck",
  data: () => ({
    jwt: "",
    username: "",
    result: "",
    dialog: false
  }),
  methods: {
    startVerification() {
      this.jwt = this.$route.query.jwt;
      let decode;
      try {
        decode = jwt_decode(this.jwt);
      } catch (e) {
        this.$router.replace("/");
        return;
      }
      if (decode.sub !== 'mail-verify') {
        this.$router.replace("/");
      }
      this.username = decode.username;
      this.$http.put("user-info/register/" + this.jwt).then(value => {
        this.dialog = true;
        this.result = value['message'];
      })
    },
    ok() {
      this.$router.replace("/")
    }
  },
  activated() {
    this.startVerification();
  }
}
</script>


<style scoped>

</style>