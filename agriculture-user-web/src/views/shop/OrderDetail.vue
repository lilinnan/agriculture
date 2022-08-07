<template>
  <v-container v-if="order">
    <v-row>
      <v-col class="col-md-6 offset-md-3 col-12">
        <v-card>
          <v-list two-line subheader>
            <v-subheader>收货信息</v-subheader>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title><b>{{ order.name }}</b><span
                    class="ml-2">{{ order.phone }}</span></v-list-item-title>
                <v-list-item-subtitle class="mt-2">{{ order.address + order.detail }}
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-row>
            <v-col class="d-12 mr-1">
              <div style="display: flex">
                <div style="flex: 1;font-weight: bold" class="ml-4">
                  商城自营
                </div>
                <div class="red--text">
                  {{ status }}
                </div>
              </div>
            </v-col>
          </v-row>
          <v-divider></v-divider>
          <v-row style="cursor: pointer" @click="toSpuDetail">
            <v-col class="col-4 col-md-3">
              <v-card class="ml-2">
                <v-img :src="img"
                       aspect-ratio="1">
                </v-img>
              </v-card>
            </v-col>
            <v-col class="col-8 col-md-9">
              <div style="display: flex">
                <div class="spu-title" style="flex: 1">
                  {{ order.skus[0]['spuName'] }}
                </div>
                <div class="mr-1">
                  <div class="text-right">￥{{ skuPrice }}</div>
                  <div class="text-right" style="color: gray">×{{ order.skus[0]['num'] }}</div>
                </div>
              </div>
              <div style="font-size: 0.8rem" v-html="skuName"></div>
            </v-col>
          </v-row>
          <v-divider></v-divider>
          <v-row>
            <v-col class="text-right">
              实付：￥{{ price }}（免运费）
            </v-col>
          </v-row>
          <v-divider
              v-if="order.status.id===0||order.status.id===1||order.status.id===3||order.status.id===4"></v-divider>
          <v-list>
            <v-list-item v-if="order.status.id===1">
              <v-btn width="100%" color="primary" @click="pay">支付宝支付</v-btn>
            </v-list-item>
            <v-list-item v-if="order.status.id===1">
              <v-btn width="100%" color="primary" @click="cancelDialog=true">取消订单</v-btn>
            </v-list-item>
            <v-list-item v-if="order.status.id===3">
              <v-btn width="100%" color="primary" @click="confirmDialog=true">确认收货</v-btn>
            </v-list-item>
            <v-list-item v-if="order.status.id===0||order.status.id===4">
              <v-btn width="100%" color="primary" @click="deleteDialog=true">删除订单</v-btn>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list>
            <v-list-item>
              <v-row>
                <v-col>
                  <div style="display: flex">
                    <div style="flex: 1;margin: auto">
                      订单编号：{{ order.orderNum }}
                    </div>
                    <div>
                      <v-btn @click="copyOrderNum">复制</v-btn>
                    </div>
                  </div>
                </v-col>
              </v-row>
            </v-list-item>
            <v-list-item v-if="order['remark']">
              备注：{{ order['remark'] }}
            </v-list-item>
            <v-list-item v-if="order['logisticsCompany']">
              物流公司：{{ order['logisticsCompany']['name'] }}
            </v-list-item>


            <v-list-item v-if="order['trackNumber']">
              <v-row>
                <v-col>
                  <div style="display: flex">
                    <div style="flex: 1;margin: auto">
                      物流单号：{{ order['trackNumber'] }}
                    </div>
                    <div>
                      <v-btn @click="copyTrackNumber">复制</v-btn>
                    </div>
                  </div>
                </v-col>
              </v-row>
            </v-list-item>

            <v-list-item v-if="order['createTime']">
              下单时间：{{ order['createTime'] }}
            </v-list-item>
            <v-list-item v-if="order.status>=2">
              支付方式：支付宝
            </v-list-item>
            <v-list-item v-if="order['payTime']">
              支付时间：{{ order['payTime'] }}
            </v-list-item>
            <v-list-item v-if="order['payOrderNum']">
              支付单号：{{ order['payOrderNum'] }}
            </v-list-item>
            <v-list-item v-if="order['deliveryTime']">
              发货时间：{{ order['deliveryTime'] }}
            </v-list-item>
            <v-list-item v-if="order['confirmTime']">
              成交时间：{{ order['confirmTime'] }}
            </v-list-item>
          </v-list>
          <div class="d-none" v-html="result">

          </div>
        </v-card>
      </v-col>
    </v-row>


    <!--弹窗开始-->

    <v-dialog v-model="confirmDialog" persistent max-width="360">
      <v-card>
        <v-card-title>
          提示
        </v-card-title>
        <v-card-text>
          您确认已经收到货了吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="realConfirm">确定</v-btn>
          <v-btn color="gray" text @click="confirmDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="cancelDialog" persistent max-width="360">
      <v-card>
        <v-card-title>
          提示
        </v-card-title>
        <v-card-text>
          您确认要取消此订单吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="realCancel">确定</v-btn>
          <v-btn color="gray" text @click="cancelDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title>
          提示
        </v-card-title>
        <v-card-text>
          您确认要删除此订单吗（删除后的订单无法进行恢复或查看）？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="realDelete">确定</v-btn>
          <v-btn color="gray" text @click="deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


    <!--    弹窗结束-->

  </v-container>
</template>

<script>
import Order from "../../components/Order";

export default {
  name: "OrderDetail",
  components: {Order},
  data: () => ({
    orderNum: '',
    order: undefined,
    result: '',
    confirmDialog: false,
    deleteDialog: false,
    cancelDialog: false
  }),
  methods: {
    queryDetailInfo() {
      this.$http.get("order/" + this.orderNum).then(value => {
        if (value.status === 200) {
          this.order = value.data;
          return;
        }
        if (value.status === 400) {
          this.$toast.error(value['message']);
          this.$router.replace("user-order");
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    pay() {
      this.$http.post("order/pay", {
        orderNum: this.order.orderNum,
        pc: this.$isPc()
      }).then(value => {
        if (value.status === 200) {
          this.result = value.data;
          setTimeout(() => {
            document.getElementsByName("punchout_form")[0].submit();
          }, 0);
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    copyOrderNum() {
      this.$copyText(this.order.orderNum).then(() => {
        this.$toast.success("复制成功");
      }, function () {
        this.$toast.success("复制失败");
      })
    },
    copyTrackNumber() {
      this.$copyText(this.order['trackNumber']).then(() => {
        this.$toast.success("复制成功");
      }, function () {
        this.$toast.success("复制失败");
      })
    },
    toSpuDetail() {
      this.$router.push("spu-detail?id=" + this.order.skus[0]['spuId'])
    },
    realConfirm() {
      this.$http.put("order/confirm/" + this.order.orderNum).then(value => {
        if (value.status === 200) {
          this.$toast.success("收货成功");
          this.confirmDialog = false;
          this.refresh();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    realCancel() {
      this.$http.put("order/cancel/" + this.order.orderNum).then(value => {
        if (value.status === 200) {
          this.$toast.success("取消成功");
          this.confirmDialog = false;
          this.refresh();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    realDelete() {
      this.$http.put("order/delete/" + this.order.orderNum).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.confirmDialog = false;
          this.refresh();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    refresh() {
      this.$router.replace("blank");
    }
  },
  activated() {
    this.order = undefined;
    this.orderNum = this.$route.query['order-num'];
    if (!this.orderNum) {
      this.$router.replace("user-order");
      return;
    }
    this.queryDetailInfo();
  },
  computed: {
    status() {
      switch (this.order.status.id) {
        case 0:
          return '已取消';
        case 1:
          return '未支付';
        case 2:
          return '已支付';
        case 3:
          return '已发货';
        case 4:
          return '交易成功';
      }
    },
    price() {
      return this.$toDecimal2(this.order.price)
    },
    skuPrice() {
      return this.$toDecimal2(this.order.skus[0]['skuPrice']);
    },
    skuName() {
      return this.order.skus[0]['skuName'] && this.order.skus[0]['skuName'].replaceAll("\n", "<br>")
    },
    img() {
      return this.order.skus[0]['spuImg'];
    }
  }
}
</script>

<style scoped>

</style>