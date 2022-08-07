<template>
  <v-container>
    <v-row>
      <v-col class="col-md-6 offset-md-3 col-12">

        <v-list two-line subheader>
          <v-subheader>用户信息</v-subheader>
          <v-list-item @click="">
            <v-list-item-content>
              <v-list-item-title>用户名</v-list-item-title>
              <v-list-item-subtitle>{{ $root['userInfo'].username }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="changePasswordDialog=true">
            <v-list-item-content>
              <v-list-item-title>修改密码</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>
    <change-password v-model="changePasswordDialog"/>
  </v-container>
</template>

<script>

import ChangePassword from "../../components/admininfo/ChangePassword";

export default {
  name: "UserInfo",
  components: {ChangePassword},
  data: () => ({
    changePasswordDialog: false
  })
}
</script>

<style scoped>

</style>