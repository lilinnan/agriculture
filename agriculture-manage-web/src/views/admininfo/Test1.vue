<template>
  <v-container>
    <v-btn @click="test">测试</v-btn>
    <v-row>
      <v-col>
        <v-treeview
            v-model="selection"
            :items="items"
            selection-type="independent"
            return-object
            open-all
        >
          <template v-slot:append="{ item }">
            <v-btn icon small>
              <v-icon small>mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon small>
              <v-icon small>mdi-plus</v-icon>
            </v-btn>
            <v-btn icon small>
              <v-icon small>mdi-delete</v-icon>
            </v-btn>
          </template>
        </v-treeview>
        <v-btn>创建新的根节点</v-btn>
      </v-col>
    </v-row>


  </v-container>
</template>

<script>
export default {
  name: "Test1",
  data: () => ({
    selectionType: 'leaf',
    selection: [],
    items: [
      {
        id: 1,
        name: '吃的',
        children: [
          {id: 2, name: '好吃的'},
          {id: 3, name: '很好吃的'},
          {
            id: 4,
            name: '不好吃的',
            children: [
              {id: 5, name: '难吃的'},
              {id: 6, name: '非常难吃的'},
              {id: 7, name: '非常难吃的'},
            ],
          },
        ],
      },
    ],
  }),
  methods: {
    test: function () {
      this.$http.get("authority/role").then(
          value => {
            this.$toast.success(value['message']);
          }
      )
    },
    select: function (item) {
      console.log(item.id)
    }
  }
}
</script>

<style scoped>

</style>