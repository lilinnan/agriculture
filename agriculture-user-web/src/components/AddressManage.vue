<template>
  <v-card-text>
    <v-list three-line subheader>
      <v-list-item @click="newAddress">
        <v-list-item-content>
          <v-list-item-title>新建地址</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-for="address of addressList" @click="selectAddress(address)">
        <v-list-item-content>
          <v-list-item-title>{{ address.name }}</v-list-item-title>
          <v-list-item-subtitle>{{ address.phone }}</v-list-item-subtitle>
          <v-list-item-subtitle>{{ address.address + address.detail }}</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn @click.stop="editAddress(address)" icon>
            <v-icon color="gray">mdi-pencil</v-icon>
          </v-btn>
          <v-btn @click.stop="deleteAddress(address)" icon>
            <v-icon color="gray">mdi-delete</v-icon>
          </v-btn>
          <v-btn @click.stop="setDefaultAddress(address)" icon>
            <v-icon :color="address.default?'primary':'gray'" style="transform:rotate(180deg)">mdi-label</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
    <v-dialog v-model="newAddressDialog" persistent max-width="480">
      <v-card>
        <v-card-title>{{ title }}</v-card-title>
        <v-divider></v-divider>
        <v-card-text>
          <v-form ref="newAddressForm">
            <v-text-field v-model="detail.name" placeholder="请输入收货人姓名" :rules="rules.nameRule"></v-text-field>
            <v-text-field v-model="detail.phone" placeholder="请输入收货手机号" type="number"
                          :rules="rules.phoneRule.concat(check)" @click="clearPhone"
                          :type="phoneCheck?'number':'text'"></v-text-field>
            <v-text-field v-model="address" placeholder="请选择收货地址" readonly :rules="rules.address"
                          @click="openAddressSelectDialog"/>
            <v-text-field v-model="detail.detail" placeholder="请输入详细地址" :rules="rules.detail"></v-text-field>
          </v-form>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="cancelNewAddressDialog">取消</v-btn>
          <v-btn color="primary" text @click="saveNewAddressDialog">保存</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="addressSelectDialog" persistent scrollable max-width="480">
      <v-card>
        <v-card-title>选择您的收货地址</v-card-title>
        <v-divider></v-divider>
        <v-card-text style="height: 400px">
          <address-select v-model="address" ref="addressSelect"></address-select>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn color="primary" text @click="cancelAndClearAddressSelectDialog">清空选择</v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="cancelAddressSelectDialog">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteDialog" persistent scrollable max-width="360">
      <v-card>
        <v-card-title>警告</v-card-title>
        <v-card-text>
          您确定要删除此条收货信息吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDeleteAddress">确定</v-btn>
          <v-btn color="primary" text @click="deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


  </v-card-text>
</template>

<script>
import AddressSelect from "./AddressSelect";

export default {
  name: "AddressManage",
  props: {
    select: Boolean
  },
  components: {AddressSelect},
  data: () => ({
    title: '',
    addressList: [],
    newAddressDialog: false,
    addressSelectDialog: false,
    deleteDialog: false,
    deleteId: -1,
    address: '',
    backupAddress: '',
    detail: {
      id: -1,
      name: '',
      phone: '',
      detail: ''
    },
    phoneCheck: false,
    rules: {
      nameRule: [
        v => !!v || '请输入收货人姓名',
      ],
      phoneRule: [
        v => !!v || '请输入手机号',
      ],
      detail: [
        v => !!v || '请输入详细地址'
      ],
      address: [
        v => !!v || '请选择您的地址'
      ]
    },
  }),
  methods: {
    clearPhone() {
      if (!this.phoneCheck) {
        this.phoneCheck = true;
        this.detail.phone = '';
      }
    },
    editAddress(item) {
      this.phoneCheck = false;
      this.detail.id = item.id;
      this.detail.name = item.name;
      this.detail.phone = item.phone;
      this.detail.detail = item.detail;
      this.address = item.address;
      this.newAddressDialog = true;
      this.openNewAddressDialog("编辑收货信息");
    },
    newAddress() {
      this.detail.id = -1;
      this.phoneCheck = true;
      this.openNewAddressDialog("新建收货信息");
    },
    deleteAddress(item) {
      this.deleteId = item.id;
      this.deleteDialog = true;
    },
    realDeleteAddress() {
      this.$http.delete("user-info/address/" + this.deleteId).then(value => {
        this.deleteDialog = false;
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.updateAddressList();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    setDefaultAddress(item) {
      this.$http.put("user-info/address/default/" + item.id).then(value => {
        if (value.status === 200) {
          this.$toast.success("已设为默认");
          this.updateDefaultList();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    updateAddressList() {
      this.$http.get("user-info/address").then(value => {
        if (value.status === 200) {
          this.addressList = value.data;
          this.updateDefaultList();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    updateDefaultList() {
      this.$http.get("user-info/address/default").then(value => {
        if (value.status === 200) {
          if (value.data === null) {
            return;
          }
          let newAddressList = [];
          for (let address of this.addressList) {
            address.default = address.id === value.data.id;
            newAddressList.push(address);
          }
          this.addressList = newAddressList;
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    openAddressSelectDialog() {
      //开始编辑就清空。便于监听地址是否发生变化，这里需要备份之前的地址，来实现取消恢复功能
      this.addressSelectDialog = true;
      this.backupAddress = this.address;
      this.address = '';
    },
    cancelAndClearAddressSelectDialog() {
      //仅清空数据
      // this.addressSelectDialog = false;
      this.$refs.addressSelect.clear();
    },
    cancelAddressSelectDialog() {
      this.address = this.backupAddress;
      this.addressSelectDialog = false;
    },
    saveAddressSelectDialog() {
      this.addressSelectDialog = false;
    },
    cancelNewAddressDialog() {
      //取消地址编辑后，清空地址选择器！
      this.$refs.addressSelect && this.$refs.addressSelect.clear();
      this.phoneCheck = false;
      this.$refs.newAddressForm.reset();
      this.newAddressDialog = false;
    },
    openNewAddressDialog(title) {
      this.title = title;
      this.newAddressDialog = true;
    },
    selectAddress(item) {
      if (this.select) {
        this.$emit("input", item);
      }
    },
    saveNewAddressDialog() {
      if (!this.$refs.newAddressForm.validate()) {
        this.$toast.warning("请检查您的数据");
        return;
      }
      let obj = {};
      Object.assign(obj, this.detail);
      Object.assign(obj, {address: this.address});
      if (obj.id === -1) {
        this.$http.post("user-info/address", obj).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.updateAddressList();
            this.cancelNewAddressDialog();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      this.$http.put("user-info/address", obj).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.updateAddressList();
          this.cancelNewAddressDialog();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
  },
  watch: {
    address(val) {
      if (val) {
        //如果传来了地址，就保存并关闭修改框
        this.saveAddressSelectDialog();
      }
    },
  },
  computed: {
    check() {
      if (!this.phoneCheck) {
        return true;
      }
      return /^1[3-9]\d{9}$/.test(this.detail.phone) || '请输入正确的手机号';
    }
  },
}
</script>

<style scoped>

</style>