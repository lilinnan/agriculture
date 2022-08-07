<template>
  <v-container>
    <v-row>
      <v-col class="col-md-6 offset-md-3 col-12">

        <v-list two-line subheader>
          <v-subheader>用户信息</v-subheader>
          <v-list-item @click="">
            <v-list-item-content>
              <v-list-item-title>用户名</v-list-item-title>
              <v-list-item-subtitle>{{ username }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="changePasswordDialog=true">
            <v-list-item-content>
              <v-list-item-title>修改密码</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>

        <v-list two-line subheader>
          <v-subheader>收货地址</v-subheader>
          <v-list-item @click="openAddressDialog">
            <v-list-item-content>
              <v-list-item-title>管理收货信息</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>

    <v-dialog v-model="changePasswordDialog" persistent max-width="400">
      <change-password v-model="changePasswordDialog"></change-password>
    </v-dialog>

    <v-dialog v-model="addressDialog" persistent max-width="480">
      <v-card>
        <v-card-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="addressDialog=false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <address-manage ref="addressManage" :select="false"></address-manage>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>

import ChangePassword from "../../components/ChangePassword";
import AddressSelect from "../../components/AddressSelect";
import AddressManage from "../../components/AddressManage";

export default {
  name: "UserInfo",
  components: {AddressManage, AddressSelect, ChangePassword},
  data: () => ({
    username: '',
    changePasswordDialog: false,
    addressDialog: false,
  }),
  methods: {
    openAddressDialog() {
      this.addressDialog = true;
      setTimeout(() => {
        this.$refs.addressManage.updateAddressList();
      }, 0)
    },
  },
  activated() {
    this.$http.get("user-info/user-info").then(value => {
      if (value.status === 200) {
        this.username = value.data;
        return;
      }
      this.$toast.error(value['message']);
    })
  },
}
</script>

<style scoped>

</style>