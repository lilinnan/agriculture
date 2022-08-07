<template>
  <v-container>
    <v-data-table
        @click:row="open"
        :items-per-page=5
        :headers="tableInfo.headers"
        :items="showData.data"
        :options.sync="options"
        :server-items-length="showData.total"
        :loading="tableInfo.loading"
        item-key="orderNum"
        class="elevation-1"
        show-expand
        :expanded.sync="expanded"
        :multi-sort="true"
        :footer-props="{
          showFirstLastPage: true,
          firstIcon: 'mdi-arrow-collapse-left',
          lastIcon: 'mdi-arrow-collapse-right',
          prevIcon: 'mdi-minus',
          nextIcon: 'mdi-plus'
        }"
    >
      <template v-slot:item.price="{ item }">
        {{ $toDecimal2(item.price) + ' 元' }}
      </template>

      <template v-slot:expanded-item="{ headers, item }">
        <td :colspan="headers.length" class="elevation-0">
          <v-container>
            <v-row>
              <template v-for="sku of item.skus">
                <v-col class="col-md-4 col-12">
                  品名：{{ sku.spuName }}
                </v-col>
                <v-col class="col-md-4 col-12">
                  规格：{{ sku['skuName'] }}
                </v-col>
                <v-col class="col-md-4 col-12">
                  数量：{{ sku['num'] }}
                </v-col>
              </template>
            </v-row>
            <v-row>
              <v-col class="col-md-4 col-12">
                收货人：{{ item.name }}
              </v-col>
              <v-col class="col-md-4 col-12">
                手机号：{{ item.phone }}
              </v-col>
              <v-col class="col-md-4 col-12">
                收货地址：{{ item.address + item.detail }}
              </v-col>
            </v-row>

            <v-row>
              <v-col class="col-12 col-md-8" style="word-break: break-all">
                订单备注：{{ item['remark'] ? item['remark'] : '无备注' }}
              </v-col>
              <v-col class="col-12 col-md-4" v-if="item.status.id>=2">
                支付时间：{{ item['payTime'] }}
              </v-col>
            </v-row>

            <v-row>
              <v-col class="col-12 col-md-4" v-if="item.status.id>=2">
                支付单号：{{ item['payOrderNum'] }}
              </v-col>
            </v-row>


            <v-row v-if="item.status.id>=3">
              <v-col class="col-md-4 col-12">
                物流公司：{{ item['logisticsCompany']['name'] }}
              </v-col>
              <v-col class="col-md-4 col-12">
                物流单号：{{ item['trackNumber'] }}
              </v-col>
              <v-col class="col-md-4 col-12">
                发货时间：{{ item['deliveryTime'] }}
              </v-col>
            </v-row>
            <v-row>
              <v-col class="col-12 col-md-8">
                用户邮箱：{{ item.user.email }}
              </v-col>
              <v-col class="col-12 col-md-4" v-if="item.status.id>=4">
                收货时间：{{ item['confirmTime'] }}
              </v-col>
            </v-row>
            <v-row v-if="item.status.id===2 || item.status.id===3">
              <v-spacer></v-spacer>
              <v-dialog v-model="dialogInfo.deliveryDialog" max-width="360px" persistent>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                      v-bind="attrs"
                      v-on="on"
                      @click="startDelivery(item)">
                    发货/修改物流
                  </v-btn>
                </template>
                <v-card>
                  <v-card-title>
                    <span class="headline">发货</span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-form ref="deliveryForm">
                        <v-autocomplete
                            v-model="deliveryData.logisticsCompanyId"
                            :items="otherData.logisticsCompanyData"
                            item-text="name"
                            item-value="id"
                            clearable
                            label="请选择物流公司"></v-autocomplete>
                        <v-text-field
                            type="text"
                            autocomplete="off"
                            label="请输入物流单号"
                            v-model="deliveryData.trackNumber"
                            :rules="rules.trackNumberRule"
                            required></v-text-field>
                      </v-form>
                    </v-container>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" text @click="resetDeliveryDialog">取消</v-btn>
                    <v-btn color="primary" text @click="delivery">保存</v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-row>
          </v-container>
        </td>
      </template>
      <template v-slot:item.status="{ item }">
        <v-chip :color="randomColor(item.status.id)">{{ item.status['statusName'] }}</v-chip>
      </template>
      <template v-slot:top>
        <v-toolbar flat rounded>
          <v-toolbar-title>{{ title }}</v-toolbar-title>
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
  name: "OrderList",
  props: {
    status: Number,
    title: String
  },
  data: () => ({
    expanded: [],
    pageInfo: {
      loadFlag: false,
      searchTitle: '搜索（输入品牌id、品牌名称均可）',
      optionUrl: 'order'
    },
    dialogInfo: {
      deliveryDialog: false
    },
    deliveryData: {
      deliveryOrder: undefined,
      logisticsCompanyId: -1,
      trackNumber: ''
    },
    showData: {
      //向页面展示的数据
      total: 0,
      data: [],
    },
    rules: {
      trackNumberRule: [
        v => !!v || '请输入物流单号',
      ],
    },
    tableInfo: {
      //表格的数据信息
      loading: true,
      headers: [
        {text: '订单号', value: 'orderNum'},
        {text: '用户', value: 'user.username'},
        {text: "金额", value: "price"},
        {text: "订单状态", value: "status"},
        {text: "下单时间", value: "createTime"},
        {text: '展开', value: 'data-table-expand'},
      ],
    },
    options: {},
    otherData: {
      logisticsCompanyData: [],
      searchKey: '',
      color: ['green', 'red', 'green', 'cyan', 'teal', 'orange', 'red', 'yellow',]
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
    open(item) {
      if (document.body.clientWidth <= 600) {
        return;
      }
      let flag = true;
      for (let i = 0; i < this.expanded.length; i++) {
        let obj = this.expanded[i];
        if (obj.orderNum === item.orderNum) {
          flag = false;
          this.expanded.splice(i, 1);
        }
      }
      if (flag) {
        this.expanded.push(item);
      }
    },
    getDataFromApi() {
      this.loading = true;
      this.$search(this.pageInfo.optionUrl + "/" + this.status, this.options, this.otherData.searchKey).then(result => {
        if (result.status === 200) {
          this.showData.data = result.data.list
          this.showData.total = result.data.total
          this.tableInfo.loading = false
          return;
        }
        this.$toast.error(result['message']);
      });
    },
    getLogisticsCompany() {
      this.$http.get("logistics/simple").then(value => {
        if (value.status === 200) {
          this.otherData.logisticsCompanyData = value.data.list;
          return;
        }
        this.$toast.error(value['message']);
      });
    },
    randomColor(id) {
      return this.otherData.color[id % this.otherData.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
    startDelivery(order) {
      this.deliveryData.deliveryOrder = order;
      if (order['logisticsCompany']) {
        this.deliveryData.logisticsCompanyId = order['logisticsCompany'].id;
      }
      if (order['trackNumber']) {
        this.deliveryData.logisticsCompanyId = order['trackNumber'];
      }
    },
    delivery() {
      if (this.deliveryData.logisticsCompanyId < 1) {
        this.$toast.warning("请选择物流公司");
        return;
      }
      if (!this.$refs.deliveryForm.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      //开始发货
      this.$http.put(this.pageInfo.optionUrl + "/delivery", {
        orderNum: this.deliveryData.deliveryOrder.orderNum,
        logisticsCompanyId: this.deliveryData.logisticsCompanyId,
        trackNumber: this.deliveryData.trackNumber
      }).then(value => {
        if (value.status === 200) {
          this.$toast.success("发货成功");
          this.resetDeliveryDialog();
          this.getDataFromApi();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    resetDeliveryDialog() {
      this.$refs.deliveryForm.reset();
      this.deliveryData.logisticsCompanyId = -1;
      this.dialogInfo.deliveryDialog = false;
    }
  },
  activated() {
    this.getLogisticsCompany();
    if (this.pageInfo.loadFlag) {
      this.getDataFromApi();
    }
    this.pageInfo.loadFlag = true;
  },
  computed: {}
}
</script>

<style scoped>

</style>