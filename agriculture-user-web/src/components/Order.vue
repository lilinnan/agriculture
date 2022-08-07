<template>
  <v-card class="mb-3" hover @click="toDetail">
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
    <v-row>
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
    <v-divider></v-divider>
    <v-row>
      <v-col class="mr-3" style="text-align: right" v-if="this.order.status.id===0 || this.order.status.id === 4">
        <v-btn @click.stop="deleteDialog=true">删除订单</v-btn>
      </v-col>
      <v-col class="mr-3" style="text-align: right" v-if="this.order.status.id===1">
        <v-btn @click.stop="cancelDialog=true" class="mr-4">取消订单</v-btn>
        <v-btn @click.stop="pay">支付</v-btn>
      </v-col>
      <v-col class="mr-3" style="text-align: right" v-if="this.order.status.id===3">
        <v-btn @click.stop="confirmDialog = true">确认收货</v-btn>
      </v-col>
    </v-row>
    <div class="d-none" v-html="result"></div>

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


  </v-card>
</template>

<script>
export default {
  name: "Order",
  props: {
    order: {
      type: Object,
      default() {
        return {}
      },
    },
    callback: {
      type: Function,
      default() {
        return () => {
        }
      },
    },
  },
  data: () => ({
    result: '',
    confirmDialog: false,
    deleteDialog: false,
    cancelDialog: false
  }),
  methods: {
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
    realConfirm() {
      this.$http.put("order/confirm/" + this.order.orderNum).then(value => {
        if (value.status === 200) {
          this.$toast.success("收货成功");
          this.confirmDialog = false;
          this.callback();
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
          this.callback();
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
          this.callback();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    toDetail() {
      this.$router.push("order-detail?order-num=" + this.order.orderNum);
    }
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