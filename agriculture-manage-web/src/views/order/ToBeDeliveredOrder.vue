<template>
  <order-list title="待发货" :status=2></order-list>
</template>

<script>
import OrderList from "@/components/OrderList";

export default {
  name: "ToBeDeliveredOrder",
  components: {OrderList},
}
</script>

<style scoped>

</style>