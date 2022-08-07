<template>
  <v-container fluid>
    <v-autocomplete
        v-model="values"
        :items="items"
        item-text="description"
        item-value="id"
        outlined
        chips
        label="选择权限"
        auto-select-first
        clearable
        deletable-chips
        hide-no-data
        multiple>
    </v-autocomplete>
    <v-btn @click="test">测试</v-btn>
  </v-container>
</template>


<script>
export default {
  name: "Test2",
  data: () => ({
    items: undefined,
    values: [],
  }),
  methods: {
    test: function () {
      this.$toast.info(this.values);
    }
  },
  mounted() {
    this.$http.get("authority/authority").then(result => {
      if (result.status === 200) {
        this.items = result.data.list
        return;
      }
      this.$toast.error(result['message']);
    })
  }
}
</script>

<style scoped>

</style>