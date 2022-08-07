<template>
  <div>
    <v-navigation-drawer v-model="$store.state.drawer" app clipped>
      <v-list dense nav>
        <v-list-item to="/">
          <v-list-item-title>植物信息</v-list-item-title>
        </v-list-item>
        <v-list-item to="animal">
          <v-list-item-title>动物信息</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <keep-alive>
      <router-view/>
    </keep-alive>
  </div>
</template>

<script>
import Plant from "@/views/shop/agriculture/Plant";

export default {
  name: "Main",
  components: {Plant},
}
</script>

<style scoped>

</style>