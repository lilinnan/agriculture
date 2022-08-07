<template>
  <order-list title="已完成" :status=4></order-list>
</template>

<script>
import OrderList from "@/components/OrderList";

export default {
  name: "CompletedOrder",
  components: {OrderList},
}
</script>

<style scoped>

</style>