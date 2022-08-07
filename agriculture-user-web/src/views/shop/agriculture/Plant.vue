<template>
  <v-container>
    <v-data-table
        :items-per-page=5
        :headers="tableInfo.headers"
        :items="showData.data"
        :options.sync="options"
        :server-items-length="showData.total"
        :loading="tableInfo.loading"
        class="elevation-1"
        :multi-sort="true"
        :footer-props="{
          showFirstLastPage: true,
          firstIcon: 'mdi-arrow-collapse-left',
          lastIcon: 'mdi-arrow-collapse-right',
          prevIcon: 'mdi-minus',
          nextIcon: 'mdi-plus'
        }"
    >

      <template v-slot:item.image="{ item }">
        <v-img class="mt-2 mb-2" :src="item.image" height="100px" width="100px"/>
      </template>

      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="showInfo(item)"
        >
          mdi-information-outline
        </v-icon>
      </template>

      <template v-slot:top>
        <v-toolbar flat rounded>
          <v-toolbar-title>{{ pageInfo.pageTitle }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-text-field
              v-model="otherData.searchKey"
              @blur="getDataFromApi"
              ref="searchInput"
              @keyup.enter="$refs.searchInput.blur()"
              style="margin: 0;padding: 0"
              append-icon="mdi-magnify"
              :label="pageInfo.searchTitle"
              single-line
              hide-details
          ></v-text-field>
        </v-toolbar>
      </template>
    </v-data-table>

    <v-dialog v-model="dialogInfo.infoDialog" fullscreen hide-overlay scrollable>
      <entity-info v-model="dialogInfo.infoDialog" :entity-id="pageInfo.showEntityId"
                   :entity-name="pageInfo.showEntityName"/>
    </v-dialog>
  </v-container>
</template>

<script>
import EntityInfo from "./EntityInfo";


export default {
  name: "Plant",
  components: {EntityInfo},
  data: () => ({
    pageInfo: {
      pageTitle: '植物信息',
      searchTitle: '搜索（输入植物id、植物名称均可）',
      optionUrl: 'agriculture/entity',
      showEntityId: 0,
      showEntityName: ''
    },
    showData: {
      //向页面展示的数据
      total: 0,
      data: [],
    },
    tableInfo: {
      //表格的数据信息
      loading: true,
      headers: [
        {text: 'ID', value: 'id'},
        {text: '名称', value: 'name'},
        {text: '图片', value: 'image'},
        {text: '介绍', value: 'description'},
        {text: '操作', value: 'actions', sortable: false},
      ],
    },
    options: {},
    dialogInfo: {
      editOrAddDialog: false,
      editOrAddDialogTitle: '',
      deleteDialog: false,
      infoDialog: false
    },
    otherData: {
      searchKey: '',
      color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',]
    },
  }),
  watch: {
    options: {
      handler() {
        this.getDataFromApi();
      },
      deep: true,
    },
  },
  methods: {
    getDataFromApi() {
      this.loading = true;
      this.$search(this.pageInfo.optionUrl + "/plant", this.options, this.otherData.searchKey).then(result => {
        if (result.status === 200) {
          this.showData.data = result.data.list
          this.showData.total = result.data.total
          this.tableInfo.loading = false
          return;
        }
        this.$toast.error(result['message']);
      });
    },
    showInfo(item) {
      this.dialogInfo.infoDialog = true;
      this.pageInfo.showEntityId = item.id;
      this.pageInfo.showEntityName = item.name;
    },

    randomColor(id) {
      return this.color[id % this.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>