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
      <template v-slot:item.enabled="{ item }">
        {{ item.enabled ? '是' : '否' }}
      </template>

      <template v-slot:item.accountNonLocked="{ item }">
        <v-switch v-model="item.accountNonLocked" :label="item.accountNonLocked?'可用':'禁用'"
                  @change="changeLockStatus(item)"></v-switch>
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
  </v-container>
</template>

<script>
export default {
  name: "User",
  data: () => ({
    pageInfo: {
      pageTitle: '用户管理',
      searchTitle: '搜索（输入用户id、用户名、邮箱均可）',
      optionUrl: 'user'
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
        {text: '用户名', value: 'username'},
        {text: '邮箱', value: 'email'},
        {text: '邮件验证', value: 'enabled'},
        {text: '是否可用', value: 'accountNonLocked'},
      ],
    },
    options: {},
    dialogInfo: {
      editOrAddDialog: false,
      editOrAddDialogTitle: '',
      deleteDialog: false,
    },
    editRules: {
      //修改校验信息
      roleRule: [
        v => !!v || '请输入',
      ]
    },
    editOrAddData: {
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
    changeLockStatus(item) {
      this.$http.put(this.pageInfo.optionUrl + "/lock", item).then(value => {
        if (value.status === 200) {
          this.$toast.success("操作成功");
          return;
        }
        item.accountNonLocked = !item.accountNonLocked;
        this.$toast.error(value['message']);
      })
    },
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
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>