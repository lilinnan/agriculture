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

      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="startRestore(item)"
        >
          mdi-restore
        </v-icon>
        <v-icon
            small
            @click="startDelete(item)"
        >
          mdi-delete
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
    <v-dialog v-model="dialogInfo.deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要彻底删除此商品吗（该操作不可恢复）？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogInfo.restoreDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">提示</v-card-title>
        <v-card-text>您确定要恢复吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="realRestore">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.restoreDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  name: "SpuRecycleBin",
  data: () => ({
    pageInfo: {
      loadFlag: false,
      pageTitle: '商品回收站',
      searchTitle: '搜索',
      optionUrl: 'item/spu-recycle-bin'
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
        {text: '商品名称', value: 'name'},
        {text: '操作', value: 'actions', sortable: false},
      ],
    },
    options: {},
    dialogInfo: {
      deleteDialog: false,
      restoreDialog: false
    },
    restoreSpuData: {
      id: -1,
    },
    deleteData: {
      deleteId: -1
    },
    otherData: {
      searchKey: '',
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
      this.$search(this.pageInfo.optionUrl, this.options, this.otherData.searchKey).then(result => {
        if (result.status === 200) {
          this.showData.data = result.data.list
          this.showData.total = result.data.total
          this.tableInfo.loading = false
          return;
        }
        this.$toast.error(result['message']);
      });
    },
    startRestore(item) {
      this.restoreSpuData.id = item.id;
      this.dialogInfo.restoreDialog = true;
      //在这里进行其他赋值操作
    },
    realRestore() {
      this.$http.put(this.pageInfo.optionUrl + "/" + this.restoreSpuData.id).then(value => {
        if (value.status === 200) {
          this.$toast.success("恢复成功");
          this.getDataFromApi();
          this.dialogInfo.restoreDialog = false;
          return;
        }
        this.dialogInfo.restoreDialog = false;
        this.$toast.error(value['message']);
      })
    },
    startDelete(item) {
      this.dialogInfo.deleteDialog = true;
      this.deleteData.deleteId = item.id;
    },
    realDelete() {
      this.$http.delete(this.pageInfo.optionUrl + "/" + this.deleteData.deleteId).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.getDataFromApi();
          this.dialogInfo.deleteDialog = false;
          return;
        }
        this.dialogInfo.deleteDialog = false;
        this.$toast.error(value['message']);
      })
    },
  },
  activated() {
    if (this.pageInfo.loadFlag) {
      this.getDataFromApi();
    }
    this.pageInfo.loadFlag = true;
  }
}
</script>

<style scoped>

</style>