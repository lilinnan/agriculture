<template>
  <v-list dense nav expand v-model="data">
    <template v-for="level1 in data">
      <v-list-item selectable v-if="level1.data.length===0" :to="level1.url" link :prepend-icon="level1.icon"
                   color="primary">
        <v-list-item-icon>
          <v-icon v-text="level1.icon"/>
        </v-list-item-icon>
        <v-list-item-title v-text="level1.name"/>
      </v-list-item>

      <v-list-group :prepend-icon="level1.icon" v-else no-action>
        <template v-slot:activator>
          <v-list-item-title v-text="level1.name"/>
        </template>
        <template v-for="level2 in level1.data">
          <v-list-item v-if="level2.data.length===0" :to="level2.url" link>
            <v-list-item-title v-text="level2.name"/>
          </v-list-item>
          <v-list-group no-action sub-group v-else>
            <template v-slot:activator>
              <v-list-item-content>
                <v-list-item-title v-text="level2.name"/>
              </v-list-item-content>
            </template>
            <v-list-item v-for="level3 in level2.data" :to="level3.url" link>
              <v-list-item-title v-text="level3.name"/>
            </v-list-item>
          </v-list-group>
        </template>
      </v-list-group>
    </template>
  </v-list>
</template>

<script>
export default {
  name: "Navigation",
  props: {
    data: Array
  }
}
</script>

<style scoped>

</style>