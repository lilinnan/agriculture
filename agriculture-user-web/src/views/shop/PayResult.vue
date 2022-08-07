<template>
  <v-container>
    <v-col>
      <v-row>
        支付结果查询中...
      </v-row>
    </v-col>
    <v-dialog v-model="resultDialog" persistent max-width="360">
      <v-card>
        <v-card-title>
          提示
        </v-card-title>
        <v-card-text>
          {{ result }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="toOrderDetail">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
export default {
  name: "PayResult",
  data: () => ({
    resultDialog: false,
    result: '',
    orderNum: ''
  }),
  methods: {
    check() {
      this.orderNum = this.$route.query.out_trade_no;
      this.$http.get("order/check-pay-result", {
        params: this.$route.query
      }).then(value => {
        if (value.status === 200) {
          this.resultDialog = true;
          this.result = value['message'];
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    toOrderDetail() {
      this.resultDialog = false;
      this.$router.replace("order-detail?order-num=" + this.orderNum)
    }
  },
  activated() {
    this.check();
  }
}
</script>

<style scoped>

</style>