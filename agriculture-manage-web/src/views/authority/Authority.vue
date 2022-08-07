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
  name: "Authority",
  data: () => ({
    pageInfo: {
      pageTitle: '权限查看',
      searchTitle: '搜索（输入权限id、权限或描述均可）',
      optionUrl: 'authority/authority'
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
        {text: '权限', value: 'authority'},
        {text: '描述', value: 'description'},
      ],
    },
    options: {},
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
  },
}
</script>

<style scoped>

</style>