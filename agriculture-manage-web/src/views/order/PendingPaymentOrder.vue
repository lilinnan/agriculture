<template>
  <order-list title="待支付" :status=1></order-list>
</template>

<script>
import OrderList from "@/components/OrderList";

export default {
  name: "PendingPaymentOrder",
  components: {OrderList},
}
</script>

<style scoped>

</style>