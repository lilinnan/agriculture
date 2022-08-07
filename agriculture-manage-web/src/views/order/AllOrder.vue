<template>
  <order-list title="全部订单" :status=-1></order-list>
</template>

<script>
import OrderList from "@/components/OrderList";

export default {
  name: "AllOrder",
  components: {OrderList},
}
</script>

<style scoped>

</style>