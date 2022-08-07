<template>
  <v-container>
    <v-card :class="{'d-none':isEdit}">
      <v-card-title>规格</v-card-title>
      <v-card-text>
        <v-treeview
            open-all
            :items="specificationTree"
            selection-type="independent"
            return-object>
          <template v-slot:append="{ item }">
            <v-btn icon small>
              <v-icon small @click="editSpecification(item)">mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon small v-if="item.children">
              <v-icon small @click="addSpecificationValue(item.id)">mdi-plus</v-icon>
            </v-btn>
            <v-btn icon small>
              <v-icon small @click="deleteSpecification(item)">mdi-delete</v-icon>
            </v-btn>
          </template>
        </v-treeview>
        <v-row>
          <v-spacer></v-spacer>
          <v-dialog v-model="specificationTreeDialog" max-width="500px" persistent>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  v-bind="attrs"
                  v-on="on"
                  @click="addSpecificationName()">
                创建新的规格
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{
                    specificationNameOperate === 'addName' ?
                        '请添加规格' : specificationNameOperate === 'addValue' ? '请添加规格属性' : '请输入新的名称'
                  }}
                </span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-form ref="form">
                    <v-text-field
                        type="text"
                        autocomplete="off"
                        label="请输入名称"
                        v-model="name"
                        :rules="editRules.specificationTreeNameRule"
                        required></v-text-field>
                  </v-form>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" text @click="resetEditData">取消</v-btn>
                <v-btn color="primary" text @click="save">保存</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-spacer></v-spacer>
        </v-row>
        <div class="mt-3">
          <small class="red--text">警告：请务必在添加完成规格后再编辑库存，否则会使编辑的规格库存表中信息被清空</small>
        </div>
        <div class="mt-1">
          <small class="red--text">警告：任何对规格数据修改的操作均会导致规格库存表重新生成</small>
        </div>
        <div class="mt-1">
          <small class="red--text">警告：规格表在创建后无法进行修改，请谨慎操作</small>
        </div>
      </v-card-text>
    </v-card>

    <v-dialog v-model="deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-card class="mt-3">
      <v-card-title>规格库存表(自动生成)</v-card-title>
      <v-card-text>
        <v-data-table
            :headers="headers"
            :multi-sort="true"
            :items="tableData">
          <template v-slot:item.price="props">
            <v-edit-dialog
                @open="openPriceInlineEdit(props.item)"
                @save="savePriceInlineEdit"
                large
                persistent
                save-text="保存"
                cancel-text="取消">
              {{ props.item.price }}
              <template v-slot:input>
                <v-text-field
                    :rules="rules.priceRule"
                    type="number"
                    v-model="inlineEditData.price"
                    label="编辑"
                    single-line
                    autofocus></v-text-field>
              </template>
            </v-edit-dialog>
          </template>
          <template v-slot:item.stock="props">
            <v-edit-dialog
                @open="openStockInlineEdit(props.item)"
                @save="saveStockInlineEdit"
                large
                persistent
                save-text="保存"
                cancel-text="取消">
              {{ props.item.stock }}
              <template v-slot:input>
                <v-text-field
                    :rules="rules.stockRule"
                    type="number"
                    v-model="inlineEditData.stock"
                    label="编辑"
                    single-line
                    autofocus></v-text-field>
              </template>
            </v-edit-dialog>
          </template>
        </v-data-table>
        <div class="mt-3">
          <small class="red--text">提示：点击每行的库存与价格的数据可以对单行的数据进行编辑。</small>
        </div>
        <div class="mt-2">
          <small class="red--text">提示：如果有大部分相同的数据，或者各个规格的价格与库存都相同，您可以使用下方的规格数据填充器来对规格与库存进行填充，以减少工作量。</small>
        </div>
      </v-card-text>
    </v-card>
    <v-card class="mt-3">
      <v-card-title>规格数据填充器(通用数据填充)</v-card-title>
      <v-card-text>
        <v-form ref="fillForm">
          <v-autocomplete
              v-for="fill of fillerData"
              v-model="fill.checked"
              :items="fill.value"
              item-text="name"
              item-value="id"
              clearable
              :label="'请选择'+fill.name"/>
          <v-text-field
              v-model="fillStock"
              label="请输入库存"
              type="number"
              :rules="rules.stockRule"/>
          <v-text-field
              v-model="fillPrice"
              label="请输入价格"
              type="number"
              :rules="rules.priceRule"/>
          <v-row>
            <v-spacer></v-spacer>
            <v-btn @click="fill(0)">填充库存</v-btn>
            <v-btn @click="fill(1)" class="ml-1">填充价格</v-btn>
            <v-btn @click="fill(2)" class="ml-1">全部填充</v-btn>
            <v-spacer></v-spacer>
          </v-row>
        </v-form>
        <div class="mt-3">
          <small class="red--text">提示：此功能用于批量填充某些规格的库存与价格</small>
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "SpecificationTree",
  props: {
    value: Object
  },
  data: () => ({
    rules: {
      priceRule: [
        v => v !== undefined || '请输入价格',
        v => v !== '' || '请输入价格',
        v => v >= 0 || '价格不能是负数',
        v => (v + '').indexOf('.') === -1 || (v + '').length - (v + '').indexOf('.') <= 3 || '小数不能超过两位'
      ],
      stockRule: [
        v => v !== undefined || '请输入库存',
        v => v !== '' || '请输入库存',
        v => v >= 0 || '库存不能是负数',
        v => v % 1 === 0 || '库存必须是整数'
      ]
    },
    specificationNameOperate: 'addName',
    specificationTreeDialog: false,
    specificationTree: [],
    name: '',
    editRules: {
      specificationTreeNameRule: [v => !!v || '请输入名称',]
    },
    parentId: -1,
    editOrDeleteObj: undefined,
    deleteDialog: false,
    tableData: [],
    headers: [],
    fillerData: [],
    fillStock: 0,
    fillPrice: 0,
    inlineEditData: {
      item: undefined,
      price: undefined,
      stock: undefined
    },
    serverSkuData: undefined,
    needToRestore: false,
    isEdit: false
  }),
  methods: {
    realDelete() {
      for (let i = 0; i < this.specificationTree.length; i++) {
        let a = this.specificationTree[i];
        if (a === this.editOrDeleteObj) {
          this.specificationTree.splice(i, 1);
          this.deleteDialog = false;
          return;
        }
        for (let j = 0; j < a.children.length; j++) {
          let b = a.children[j];
          if (b === this.editOrDeleteObj) {
            a.children.splice(j, 1);
            this.deleteDialog = false;
            return;
          }
        }
      }
    },
    addSpecificationName() {
      this.specificationNameOperate = 'addName';
      this.specificationTreeDialog = true;
    },
    addSpecificationValue(id) {
      this.specificationNameOperate = 'addValue';
      this.specificationTreeDialog = true;
      this.parentId = id;
    },
    editSpecification(item) {
      this.specificationNameOperate = 'edit';
      this.editOrDeleteObj = item;
      this.name = item.name;
      this.specificationTreeDialog = true;
    },
    deleteSpecification(item) {
      this.editOrDeleteObj = item;
      this.deleteDialog = true;
    },
    resetEditData() {
      this.specificationTreeDialog = false;
      this.$refs.form.reset();
    },
    save() {
      if (this.specificationNameOperate === 'addName') {
        //添加规格名称
        let max = -1;
        for (let specification of this.specificationTree) {
          if (specification.id > max) {
            max = specification.id;
          }
          if (specification.name === this.name) {
            this.$toast.error("此规格已存在");
            return;
          }
        }
        this.specificationTree.push({id: max + 1, name: this.name, children: []})
        this.resetEditData();
        return;
      }
      if (this.specificationNameOperate === 'addValue') {
        let parent = undefined;
        for (let specification of this.specificationTree) {
          if (specification.id === this.parentId) {
            parent = specification;
          }
        }
        let children = parent.children;
        let max = -1;
        for (let specificationValue of children) {
          if (specificationValue.id > max) {
            max = specificationValue.id;
          }
          if (specificationValue.name === this.name) {
            this.$toast.error("此规格值已存在");
          }
        }
        children.push({id: max + 1, name: this.name});
        this.resetEditData();
        return;
      }
      this.editOrDeleteObj.name = this.name;
      this.resetEditData();
    },

    updateTableData() {
      let data = [];
      data = this.combination(this.specificationTree, 0, data);
      const result = [];
      for (let value of data) {
        result.push(this.specificationValueToObject(value));
      }
      const newHeader = [];
      // newHeader.push({text: '编号', value: 'id'})
      for (let i = 0; i < this.specificationTree.length; i++) {
        const value = this.specificationTree[i];
        newHeader.push({text: value.name, value: 'param_' + i});
      }
      newHeader.push({text: '库存', value: 'stock'})
      newHeader.push({text: '价格', value: 'price'})
      this.tableData = result;
      this.headers = newHeader;
      this.fillerDataGenerate();
      if (this.needToRestore && this.serverSkuData) {
        this.restoreFromServerSku();
      }
    },
    fillerDataGenerate() {
      let newFillerData = [];
      for (let value of this.specificationTree) {
        let data = {};
        data.name = value.name;
        //data中的内容
        let values = [];
        values.push({id: '', name: '全部'});
        for (let child of value.children) {
          values.push({id: value.id + "-" + child.id, name: child.name});
        }
        data.value = values;
        data.checked = '';
        newFillerData.push(data)
      }
      this.fillerData = newFillerData;
    },
    /**
     * 填充库存与价格
     * @param type 0-库存 1-价格 2-全部
     */
    fill(type) {
      if (!this.$refs.fillForm.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      for (let value of this.tableData) {
        let flag = true;
        for (let value1 of this.fillerData) {
          if (value['specificationValue'].indexOf(value1.checked) < 0) {
            flag = false;
            break;
          }
        }
        if (flag) {
          if (type === 0 || type === 2) {
            value.stock = Number(this.fillStock);
          }
          if (type === 1 || type === 2) {
            value.price = this.toDecimal2(Number(this.fillPrice));
          }
        }
      }
      this.$toast.success("数据已填充");
    },
    openPriceInlineEdit(item) {
      this.inlineEditData.item = item;
      this.inlineEditData.price = item.price;
    },
    savePriceInlineEdit() {
      let p = this.inlineEditData.price;
      if (p === undefined || p === '' || p < 0 || ((p + '').indexOf('.') !== -1 && (p + '').length - (p + '').indexOf('.') > 3)) {
        this.$toast.error("编辑失败");
        return;
      }
      this.inlineEditData.item.price = this.toDecimal2(Number(p));
    },
    openStockInlineEdit(item) {
      this.inlineEditData.item = item;
      this.inlineEditData.stock = item.stock;
    },
    saveStockInlineEdit() {
      let s = this.inlineEditData.stock;
      if (s === undefined || s === '' || s < 0 || s % 1 !== 0) {
        this.$toast.error("编辑失败");
        return;
      }
      this.inlineEditData.item.stock = Number(s);
    },
    specificationValueToObject(specificationValue) {
      const values = specificationValue.split(",");
      const obj = {};
      obj['specificationValue'] = specificationValue;
      obj['stock'] = 0;
      obj['price'] = '0.00';
      let name = '';
      for (let i = 0; i < values.length; i++) {
        const value = values[i];
        const split = value.split("-");
        //正确的规格父亲
        let rightTree = undefined;
        for (let tree of this.specificationTree) {
          if (tree.id === parseInt(split[0])) {
            rightTree = tree;
            break;
          }
        }
        if (rightTree === undefined) {
          return obj;
        }
        name += rightTree.name;
        name += "："
        for (let specificationValue of rightTree.children) {
          if (specificationValue.id === parseInt(split[1])) {
            obj['param_' + i] = specificationValue.name;
            name += specificationValue.name;
            name += '\n';
            break;
          }
        }
      }
      obj['name'] = name;
      console.log(obj.name);
      return obj;
    },
    combination(tree, index, data) {
      if (tree.length === 0) {
        return data;
      }
      if (tree.length === 1) {
        return this.treeToKV(tree[0]);
      }
      if (index >= tree.length) {
        return data;
      }
      let a = data;
      let b = this.treeToKV(tree[index++]);
      if (index === 1) {
        a = this.treeToKV(tree[index++]);
        let c = a;
        a = b;
        b = c;
      }
      data = [];
      for (let a1 of a) {
        for (let b1 of b) {
          data.push(a1 + "," + b1);
        }
      }
      return this.combination(tree, index, data)
    },
    treeToKV(treeData) {
      let data = [];
      for (let child of treeData.children) {
        data.push(treeData.id + "-" + child.id);
      }
      return data;
    },
    toDecimal2(x) {
      x = x + '';
      if (x.indexOf('.') === -1) {
        return x + '.00';
      }
      let n = 3 - (x.length - x.indexOf('.'));
      for (let i = 0; i < n; i++) {
        x += '0';
      }
      return x;
    },
    getStockData() {
      return {
        sku: this.tableData,
        specification: this.specificationTree
      }
    },
    restoreFromServerSku() {
      for (let serverSku of this.serverSkuData) {
        for (let clientSku of this.tableData) {
          if (serverSku['specificationValue'] === clientSku['specificationValue']) {
            clientSku['stock'] = serverSku['stock'];
            clientSku['price'] = this.toDecimal2((serverSku['price'] / 100));
            break;
          }
        }
      }
      this.needToRestore = false;
    },
    setSkuValue(skus) {
      this.needToRestore = true;
      this.serverSkuData = skus;
    },
    setEdit(flag) {
      this.isEdit = flag;
    }
  },
  watch: {
    specificationTree: {
      handler() {
        this.updateTableData();
      },
      deep: true
    },
    tableData: {
      handler() {
        this.$emit("input", this.getStockData())
      },
      deep: true
    },
    value(newValue) {
      this.specificationTree = newValue.specification;
      this.tableData = newValue.sku;
    }
  },
  mounted() {
    this.specificationTree = [];
    this.tableData = [];
    this.$emit("input", this.getStockData());
  }
}
</script>
<style scoped>
</style>