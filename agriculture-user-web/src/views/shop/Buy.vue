<template>
  <v-container>

    <v-row>
      <v-col class="col-md-6 offset-md-3 col-12">
        <v-card>
          <v-list two-line subheader>
            <v-subheader>收货信息</v-subheader>
            <v-list-item @click="openAddressDialog" v-if="address">
              <v-list-item-content>
                <v-list-item-title><b>{{ address && address.name }}</b><span
                    class="ml-2">{{ address && address.phone }}</span></v-list-item-title>
                <v-list-item-subtitle class="mt-2">{{ address && (address.address + address.detail) }}
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-list-item v-if="!address" @click="openAddressDialog">
              <v-list-item-content>
                <v-list-item-title>选择收货地址</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list two-line subheader>
            <v-subheader>商品信息</v-subheader>
            <v-list-item>
              <v-row style="align-items: center;">
                <v-col class="col-4 col-md-3">
                  <v-card>
                    <v-img :src="spu.imageUrl"
                           aspect-ratio="1">
                    </v-img>
                  </v-card>
                </v-col>
                <v-col class="col-8 col-md-9">
                  <div class="spu-title">
                    {{ spu.name }}
                  </div>
                  <div class="mt-3" style="font-size: 0.8rem" v-html="sku.name">
                  </div>
                  <div class="mt-3">
                    <span class="red--text">￥{{ eachPrice }}/件</span>
                  </div>
                </v-col>
              </v-row>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list two-line>
            <v-list-item>
              <v-row style="align-items: center">
                <div style="flex: 1" class="ml-1">
                  购买数量：
                </div>
                <div>
                  <div>
                    <v-btn icon @click="num--" :disabled="num<=1">
                      <v-icon>mdi-minus</v-icon>
                    </v-btn>
                    <input v-model="num" class="num-input text--primary" aria-label="num" @blur="checkBuy"/>件
                    <v-btn icon :disabled="num>=this.sku.stock" @click="num++">
                      <v-icon>mdi-plus</v-icon>
                    </v-btn>
                  </div>
                </div>
              </v-row>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list two-line>
            <v-list-item>
              <v-row style="align-items: center">
                <div class="ml-1">
                  订单备注：
                </div>
                <div style="flex: 1">
                  <input v-model="remark" class="remark text--primary mr-2" aria-label="备注" placeholder="输入订单备注">
                </div>
              </v-row>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list two-line>
            <v-list-item>
              <v-row style="align-items: center">
                <div style="flex: 1" class="ml-1">
                  实付款：
                </div>
                <div>
                  <span class="red--text" style="font-size: 1.3rem">￥{{ realPrice }}</span><span
                    class="red--text ml-2">免运费</span>
                </div>
              </v-row>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list two-line>
            <v-list-item>
              <v-btn width="100%" @click="pay">支付宝支付</v-btn>
            </v-list-item>
          </v-list>
          <div class="d-none" v-html="result">

          </div>
        </v-card>
      </v-col>
    </v-row>
    <v-dialog v-model="addressDialog" persistent max-width="480">
      <v-card>
        <v-card-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="addressDialog=false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <address-manage ref="addressManage" v-model="address" :select="true"></address-manage>
      </v-card>
    </v-dialog>

    <v-dialog v-model="noStockDialog" persistent max-width="480">
      <v-card>
        <v-card-title>
          提示
        </v-card-title>
        <v-card-text>
          抱歉，您购买的此单商品全部无货！
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="toSpu">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


  </v-container>
</template>

<script>
import AddressManage from "../../components/AddressManage";

export default {
  name: "Buy",
  components: {AddressManage},
  data: () => ({
    skuId: -1,
    spuId: -1,
    num: 0,
    address: undefined,
    addressDialog: false,
    spu: {
      name: 'loading...',
      imageUrl: '',
    },
    sku: {
      id: '',
      price: 0,
      stock: 0,
      name: 'loading'
    },
    noStockDialog: false,
    remark: '',
    result: ''
  }),
  methods: {
    openAddressDialog() {
      this.addressDialog = true;
      setTimeout(() => {
        this.$refs.addressManage.updateAddressList();
      }, 0)
    },
    initParam() {
      this.skuId = this.$route.query.skuId;
      this.num = this.$route.query.num;
      if ((!Number.isInteger(Number(this.skuId))) && (!Number.isInteger(Number(this.num)))) {
        this.$router.replace("/");
        return;
      }
      if (this.num <= 0) {
        this.num = 1;
      }
    },
    loadDefaultAddress() {
      this.$http.get("user-info/address/default").then(value => {
        if (value.status === 200) {
          this.address = value.data;
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    checkBuy() {
      if (this.num === '') {
        this.num = 1;
      }
      if (!Number.isInteger(Number(this.num))) {
        this.num = 1;
      }
      if (this.num <= 0) {
        this.num = 1;
      }
      if (this.sku.stock === 0) {
        return;
      }
      if (this.num > this.sku.stock) {
        this.num = this.sku.stock;
      }
    },
    loadBuyInfo() {
      this.$http.get("item/spu/buy-info/" + this.skuId).then(value => {
        if (value.status === 200) {
          this.sku = value.data.sku;
          if (!this.sku) {
            this.$router.replace("/");
            return;
          }
          this.spu = value.data.spu;
          this.spuId = value.data.spu.id;
          if (this.sku.stock <= 0) {
            this.noStockDialog = true;
            return;
          }
          if (this.sku.name) {
            this.sku.name = this.sku.name.replaceAll("\n", "<br>")
          }
          return;
        }
        this.$toast.error(value['message']);
        this.$router.replace("/");
      })
    },
    toSpu() {
      this.$router.replace("/spu-detail?id=" + this.spuId);
    },
    pay() {
      this.$http.post("order", {
        pc: this.$isPc(),
        skuId: this.skuId,
        buyNum: this.num,
        addressId: this.address.id,
        remark: this.remark
      }).then(value => {
        if (value.status === 200) {
          this.result = value.data;
          setTimeout(() => {
            document.getElementsByName("punchout_form")[0].submit();
          }, 0);
          return;
        }
        this.$toast.error((value['message']));
      })
    }
  },
  activated() {
    this.initParam();
    this.loadDefaultAddress();
    this.loadBuyInfo();
    this.remark = '';
  },
  watch: {
    address(newVal) {
      if (newVal) {
        this.addressDialog = false;
      }
    },
  },
  computed: {
    eachPrice() {
      return this.$toDecimal2(this.sku.price);
    },
    realPrice() {
      return this.$toDecimal2(this.sku.price * this.num)
    }
  }
}
</script>

<style scoped>
.num-input {
  text-align: center;
  width: 5rem;
}

.num-input:focus {
  outline: none;
}

.remark {
  width: 100%;
  text-align: right;
}

.remark:focus {
  outline: none;
}

.spu-title {
  line-height: 1.2rem;
  height: 2.4rem;
  font-weight: bold;
  overflow: hidden;
  padding-top: 0;
  padding-bottom: 0;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

</style>