<template>
  <order-list title="待收货" :status=3></order-list>
</template>

<script>
import OrderList from "@/components/OrderList";

export default {
  name: "ToBeReceiveOrder",
  components: {OrderList},
}
</script>

<style scoped>

</style>