<template>

</template>

<script>
export default {
  name: "Blank",
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.$router.replace({path: from.path, query: from.query})
    })
  }
}
</script>

<style scoped>

</style>