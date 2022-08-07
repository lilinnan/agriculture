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
        }">

      <template v-slot:item.saleable="{ item }">
        <v-switch v-model="item.saleable" @change="changeSaleableStatus(item)"></v-switch>
      </template>


      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="startEdit(item)">
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="startDelete(item)">
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
              hide-details></v-text-field>
          <v-dialog v-model="dialogInfo.editOrAddDialog" max-width="720" persistent>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  icon
                  v-bind="attrs"
                  v-on="on"
                  @click="startAdd">
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ dialogInfo.editOrAddDialogTitle }}</span>
                <v-spacer></v-spacer>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-stepper v-model="editOrAddConfig.step">
                    <v-stepper-header>
                      <v-stepper-step step="1" editable>基本信息</v-stepper-step>
                      <v-divider></v-divider>
                      <v-stepper-step step="2" editable>分类选择</v-stepper-step>
                      <v-divider></v-divider>
                      <v-stepper-step step="3" editable>介绍详情</v-stepper-step>
                      <v-divider></v-divider>
                      <v-stepper-step step="4" editable>基本属性</v-stepper-step>
                      <v-divider></v-divider>
                      <v-stepper-step step="5" editable>规格与库存</v-stepper-step>
                    </v-stepper-header>
                    <v-stepper-items>
                      <v-stepper-content step="1" style="padding: 0">
                        <v-card flat>
                          <v-card-text>
                            <v-form ref="form">
                              <v-autocomplete
                                  v-model="editOrAddData.brandId"
                                  :items="editOrAddConfig.brandData"
                                  item-text="name"
                                  item-value="id"
                                  clearable
                                  label="请选择品牌"></v-autocomplete>
                              <v-text-field
                                  type="text"
                                  autocomplete="off"
                                  label="请输入商品名称"
                                  v-model="editOrAddData.spuName"
                                  required></v-text-field>
                              <image-upload v-model="editOrAddData.spuImgs"></image-upload>
                              <v-row>
                                <v-switch class="ml-5" v-model="editOrAddData.saleable"
                                          label="是否上架">
                                </v-switch>
                              </v-row>
                            </v-form>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn color="primary" @click="resetEditData" text>退出</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editOrAddConfig.step=2" text>下一步</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-stepper-content>

                      <v-stepper-content step="2" style="padding: 0">
                        <v-card elevation="0">
                          <v-card-text>
                            <v-card elevation="0">
                              <v-card-text>
                                商品分类：
                                <template v-if="editOrAddData.categoryData.length>0">
                                  <v-chip small v-for="category in editOrAddData.categoryData"
                                          :color="randomColor(category.id)">
                                    {{ category.name }}
                                  </v-chip>
                                </template>
                                <template v-else>
                                  请选择分类
                                </template>
                              </v-card-text>
                            </v-card>
                            <v-treeview
                                v-model="editOrAddData.categoryData"
                                transition
                                selected-color="primary"
                                :items="editOrAddConfig.categoryData"
                                selectable
                                return-object></v-treeview>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn color="primary" @click="resetEditData" text>退出</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editOrAddConfig.step=1" text>上一步</v-btn>
                            <v-btn color="primary" @click="editOrAddConfig.step=3" text>下一步</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-stepper-content>

                      <v-stepper-content step="3" style="padding: 0">
                        <v-card elevation="0">
                          <v-card-text>
                            <editor v-model="editOrAddData.description"></editor>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn color="primary" @click="resetEditData" text>退出</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editOrAddConfig.step=2" text>上一步</v-btn>
                            <v-btn color="primary" @click="editOrAddConfig.step=4" text>下一步</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-stepper-content>

                      <v-stepper-content step="4" style="padding: 0">
                        <v-card elevation="0">
                          <v-card-text>
                            <attribute-list v-model="editOrAddData.attr" title="商品属性管理"></attribute-list>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn color="primary" @click="resetEditData" text>退出</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editOrAddConfig.step=3" text>上一步</v-btn>
                            <v-btn color="primary" @click="editOrAddConfig.step=5" text>下一步</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-stepper-content>
                      <v-stepper-content step="5" style="padding: 0">
                        <v-card elevation="0">
                          <v-card-text>
                            <specification-tree v-model="editOrAddData.stockData"
                                                ref="specificationTree"></specification-tree>
                          </v-card-text>
                          <v-card-actions>
                            <v-btn color="primary" @click="resetEditData" text>退出</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editOrAddConfig.step=4" text>上一步</v-btn>
                            <v-btn color="primary" @click="save" text>保存</v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-stepper-content>
                    </v-stepper-items>
                  </v-stepper>
                </v-container>
              </v-card-text>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
    </v-data-table>
    <v-dialog v-model="dialogInfo.deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import ImageUpload from "@/components/ImageUpload";
import SpecificationTree from "@/components/SpecificationTree";
import AttributeList from "@/components/AttributeList";
import Editor from "@/components/EditorPlus";

export default {
  name: "Spu",
  components: {
    Editor,
    AttributeList,
    SpecificationTree,
    ImageUpload,
  },
  data: () => ({
    editorData: "",
    editOrAddConfig: {
      step: 1,
      categoryData: null,
      brandData: null
    },
    pageInfo: {
      loadFlag: false,
      pageTitle: '商品管理',
      searchTitle: '搜索',
      optionUrl: 'item/spu'
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
        {text: '是否上架', value: 'saleable'},
        {text: '操作', value: 'actions', sortable: false},
      ],
    },
    options: {},
    dialogInfo: {
      editOrAddDialog: false,
      editOrAddDialogTitle: '',
      deleteDialog: false,
    },
    editRules: {
      specificationTreeNameRule: [
        v => !!v || '规格名称不能为空',
      ],
    },
    editOrAddData: {
      id: -1,
      //品牌id
      brandId: null,
      //spu名称
      spuName: '',
      //是否上架
      saleable: true,
      //分类数据
      categoryData: [],
      //商品图片
      spuImgs: [],
      //库存与介绍
      stockData: undefined,
      //描述
      description: '',
      //属性
      attr: []
    },
    deleteData: {
      deleteId: -1
    },
    otherData: {
      searchKey: '',
      color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',],
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
    changeSaleableStatus(item) {
      this.$http.put(this.pageInfo.optionUrl + "/saleable", item).then(value => {
        if (value.status === 200) {
          this.$toast.success("操作成功");
          return;
        }
        this.$toast.error(value['message']);
        item.saleable = !item.saleable;
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
    async startEdit(item) {
      this.dialogInfo.editOrAddDialogTitle = "编辑";
      this.editOrAddData.id = item.id;
      this.dialogInfo.editOrAddDialog = true;
      //在这里进行其他赋值操作
      await this.$http.get(this.pageInfo.optionUrl + "/" + this.editOrAddData.id).then(value => {
        if (value.status === 200) {
          let data = value.data;
          this.editOrAddData.spuName = data.name;
          this.editOrAddData.saleable = data.saleable;
          this.editOrAddData.brandId = data.brand.id;
          this.restoreFromServerImage(data.spuImgs);
          this.restoreFromServerCategories(data.categories);
          this.editOrAddData.attr = JSON.parse(data.spuInfo.attr);
          this.$refs.specificationTree.setSkuValue(data.skus);
          this.$refs.specificationTree.setEdit(true);
          this.restoreFromServerSpecification(data.spuInfo.specification);
          this.editOrAddData.description = data.spuInfo.description;
          return;
        }
        this.$toast.error(value['message']);
      })

    },
    startAdd() {
      this.dialogInfo.editOrAddDialogTitle = "添加";
      this.editOrAddData.id = -1;
      //在这里进行其他编辑操作
      setTimeout(() => {
        this.$refs.specificationTree.setEdit(false);
      }, 0)
    },
    resetEditData() {
      this.dialogInfo.editOrAddDialog = false
      //在这里进行数据清空操作
      this.editOrAddData.spuName = '';
      this.editOrAddData.saleable = true;
      this.editOrAddData.brandId = undefined;
      this.editOrAddData.spuImgs = [];
      this.editOrAddData.categoryData = [];
      this.editOrAddData.attr = [];
      if (this.editOrAddData.stockData && this.editOrAddData.stockData.specification) {
        this.editOrAddData.stockData.specification.splice(0, this.editOrAddData.stockData.specification.length);
      }
      this.editOrAddData.description = '';
      this.editOrAddConfig.step = 1;
    },
    save() {
      if (!this.editOrAddData.brandId) {
        this.$toast.warning("请选择商品品牌");
        return;
      }
      if (!this.editOrAddData.spuName) {
        this.$toast.warning("请检查输入商品名称");
        return;
      }
      if (!this.editOrAddData.spuImgs || this.editOrAddData.spuImgs.length === 0) {
        this.$toast.warning("请添加商品图片");
        return;
      }
      if (!this.editOrAddData.categoryData || this.editOrAddData.categoryData.length === 0) {
        this.$toast.warning("请选择商品分类");
        return;
      }
      if (!this.editOrAddData.stockData ||
          this.editOrAddData.stockData.specification.length === 0 ||
          this.editOrAddData.stockData.sku.length === 0) {
        this.$toast.warning("请输入商品规格");
        return;
      }

      /**
       * id: -1,
       //品牌id
       brandId: null,
       //spu名称
       spuName: '',
       //是否上架
       saleable: true,
       //分类数据
       categoryData: [],
       //商品图片
       spuImgs: [],
       //库存与介绍
       stockData: undefined,
       //描述
       description: '',
       //属性
       attr: []
       */
      let data = {
        name: this.editOrAddData.spuName,
        saleable: this.editOrAddData.saleable,
        brand: {
          id: this.editOrAddData.brandId
        },
        spuImgs: this.getImageObject(),
        categories: this.getCategoryObject(),
        spuInfo: {
          attr: JSON.stringify(this.editOrAddData.attr),
          specification: JSON.stringify(this.editOrAddData.stockData.specification),
          description: this.editOrAddData.description
        },
        skus: this.toServerSku()
      }
      if (this.editOrAddData.id === -1) {
        this.$http.post(this.pageInfo.optionUrl, data).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.getDataFromApi();
            this.resetEditData();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      data = Object.assign(data, {id: this.editOrAddData.id})
      this.$http.put(this.pageInfo.optionUrl, data).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.getDataFromApi();
          this.resetEditData();
          return;
        }
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
    randomColor(id) {
      return this.otherData.color[id % this.otherData.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
    getImageObject() {
      let data = [];
      for (let i of this.editOrAddData.spuImgs) {
        data.push({img: i})
      }
      return data;
    },
    getCategoryObject() {
      let data = [];
      for (let i of this.editOrAddData.categoryData) {
        data.push({id: i.id})
      }
      return data;
    },
    toServerSku() {
      let serverSku = [];
      for (let data of this.editOrAddData.stockData.sku) {
        serverSku.push({
          specificationValue: data['specificationValue'],
          price: parseInt(data.price.replaceAll('.', '')),
          stock: data['stock'],
          name: data['name']
        })
      }
      return serverSku;
    },
    restoreFromServerImage(imageData) {
      for (let image of imageData) {
        this.editOrAddData.spuImgs.push(image.img);
      }
    },
    restoreFromServerCategories(categories) {
      for (let category of categories) {
        this.editOrAddData.categoryData.push({id: category.id})
      }
    },
    restoreFromServerSpecification(specification) {
      let data = JSON.parse(specification);
      for (let s of data) {
        this.editOrAddData.stockData.specification.push(s);
      }
    },
  },
  activated() {
    this.$http.get("item/category/simple").then(value => {
      if (value.status === 200) {
        this.editOrAddConfig.categoryData = value.data;
        return;
      }
      this.$toast.error(value['message']);
    });
    this.$http.get("item/brand/simple").then(value => {
      if (value.status === 200) {
        this.editOrAddConfig.brandData = value.data.list;
        return;
      }
      this.$toast.error(value['message']);
    })
    if (this.pageInfo.loadFlag) {
      this.getDataFromApi();
    }
    this.pageInfo.loadFlag = true;
  },
}
</script>

<style scoped>

</style>