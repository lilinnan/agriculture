<template>
  <v-container>
    <v-row>
      <v-col class="col-md-6 offset-md-3 col-12">
        <v-tabs v-model="tab" grow show-arrows>
          <v-tab href="#all">
            全部订单
          </v-tab>
          <v-tab href="#pending-payment">
            待支付
          </v-tab>
          <v-tab href="#to-be-delivered">
            待发货
          </v-tab>
          <v-tab href="#to-be-receive">
            待收货
          </v-tab>
        </v-tabs>

        <v-tabs-items v-model="tab" class="mt-3">
          <v-tab-item value="all">
            <order v-for="order of allOrder" :order="order" :callback="getDataFromApi"></order>
          </v-tab-item>
          <v-tab-item value="pending-payment">
            <order v-for="order of pendingPaymentOrder" :order="order" :callback="getDataFromApi"></order>
          </v-tab-item>
          <v-tab-item value="to-be-delivered">
            <order v-for="order of toBeDelivered" :order="order" :callback="getDataFromApi"></order>
          </v-tab-item>
          <v-tab-item value="to-be-receive">
            <order v-for="order of toBeReceive" :order="order" :callback="getDataFromApi"></order>
          </v-tab-item>
        </v-tabs-items>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Order from "../../components/Order";

export default {
  name: "UserOrder",
  components: {Order},
  data: () => ({
    tab: undefined,
    allOrder: [],
    pendingPaymentOrder: [],
    toBeDelivered: [],
    toBeReceive: []
  }),
  methods: {
    getDataFromApi() {
      this.allOrder = [];
      this.pendingPaymentOrder = [];
      this.toBeDelivered = [];
      this.toBeReceive = [];
      this.$http.get("order/history-order/-1").then(value => {
        if (value.status === 200) {
          this.allOrder = value.data;
          this.computeOrder(this.allOrder);
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    computeOrder(all) {
      for (let order of all) {
        switch (order.status.id) {
          case 1:
            this.pendingPaymentOrder.push(order);
            break;
          case 2:
            this.toBeDelivered.push(order);
            break;
          case 3:
            this.toBeReceive.push(order);
            break;
          default:
            break;
        }
      }
    }
  }, activated() {
    this.getDataFromApi();
  }
}
</script>

<style scoped>

</style>