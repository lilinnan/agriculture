<template>
  <v-container fluid>
    <v-row class="mt-md-16" style="align-items: center">
      <v-col class="col-md-4 col-10 offset-1 offset-md-0">
        <v-card>
          <v-carousel
              hide-delimiters
              cycle
              interval="2000"
              hide-delimiter-background
              show-arrows-on-hover
              height="300"
          >
            <v-carousel-item
                v-for="(item,i) in spuInfo.spuImgs"
                :key="i"
                :src="item.img"
            ></v-carousel-item>
          </v-carousel>
        </v-card>
      </v-col>
      <v-col class="col-md-7 col-12 offset-md-1" style="font-size: 1.1rem">
        <h3>{{ spuInfo.name }}</h3>
        <div class="mt-3">
          品牌：
          <v-chip small :color="randomColor(spuInfo.brand.id)">{{
              spuInfo.brand.name
            }}
          </v-chip>
        </div>
        <div class="mt-3">
          商品分类：
          <v-chip v-for="category of spuInfo.categories" small :color="randomColor(category.id)" class="ml-2">{{
              category.name
            }}
          </v-chip>
        </div>
        <div class="mt-3">
          价格：<span class="red--text"><span>￥</span>{{ price }}</span>
        </div>
        <div class="mt-3">
          请选择产品规格
          <v-radio-group v-for="value of specification" v-model="value.checked" class="pa-0" row>
            <span class="mt-2">{{ value.label }}：</span>
            <v-radio class="ml-2 mt-2" v-for="child of value.values" :label="child.label"
                     :value="child.value" :disabled="disabled.has(child.value)"></v-radio>
          </v-radio-group>
        </div>
        <div>
          <v-btn icon @click="buyNum--" :disabled="buyNum<=1">
            <v-icon>mdi-minus</v-icon>
          </v-btn>
          <input v-model="buyNum" class="num-input text--primary" aria-label="num" @blur="checkBuy"/>件
          <v-btn icon :disabled="buyNum>=stock" @click="buyNum++">
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>
        <div class="mt-3">
          <v-btn class="ml-3" :disabled="!checked || stock <= 0" @click="buy">立即购买</v-btn>
          <span class="ml-2">库存{{ stock }}件</span>
        </div>
      </v-col>
    </v-row>
    <v-row class="mt-md-10 mt-4">
      <v-card width="100%">
        <v-card-title>产品参数</v-card-title>
        <v-card-text>
          <v-row>
            <v-col class="col-md-4 col-12" v-for="a of attr">
              {{ a['attrName'] }}：{{ a['attrValue'] }}
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-row>
    <v-row class="mt-md-10 mt-4">
      <v-card width="100%">
        <v-card-title>商品详情</v-card-title>
        <v-card-text v-html="spuInfo.spuInfo.description"></v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import Spu from "../../components/Spu";
import AddressSelect from "../../components/AddressSelect";

export default {
  name: "SpuDetail",
  components: {AddressSelect, Spu},
  data: () => ({
    add: [],
    buyNum: 1,
    buyPrice: undefined,
    buyStock: undefined,
    radios: undefined,
    spuId: 0,
    spuInfo: {
      "id": -1,
      "name": "",
      "saleable": true,
      "delete": false,
      "brand": {"id": -1, "name": ""},
      "spuImgs": [],
      "categories": [],
      "spuInfo": {
        "id": -1,
        "spuId": -1,
        "attr": "[]",
        "specification": "[]",
        "description": ""
      },
      "skus": []
    },
    attr: [],
    specification: [],
    color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',],
    //这个是最后选择的sku编号
    checked: undefined,
    disabled: new Set()
  }),
  methods: {
    selectSpuInfo() {
      this.$http.get("item/spu/info/" + this.spuId).then(value => {
        if (value.status === 200) {
          this.spuInfo = value.data;
          if (!value.data.saleable) {
            this.$toast.error("商品已下架");
            this.$router.back();
            return;
          }
          this.attr = JSON.parse(this.spuInfo.spuInfo.attr);
          this.parseSpecification(this.spuInfo.spuInfo.specification);
          // console.log(this.spuInfo);
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    randomColor(id) {
      return this.color[id % this.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
    parseSpecification(specification) {
      const specificationObj = JSON.parse(specification);
      let result = [];
      for (let s of specificationObj) {
        let obj = {};
        obj['label'] = s.name;
        obj['checked'] = undefined;
        const children = s.children;
        obj['values'] = [];
        for (let child of children) {
          let value = {};
          value['value'] = s.id + "-" + child.id;
          value['label'] = child.name;
          obj['values'].push(value);
        }
        result.push(obj);
      }
      this.specification = result;
    },
    checkBuy() {
      if (this.buyNum === '') {
        this.buyNum = 1;
      }
      if (!Number.isInteger(Number(this.buyNum))) {
        this.buyNum = 1;
      }
      if (this.buyNum <= 0) {
        this.buyNum = 1;
      }
      if (this.stock === 0) {
        return;
      }
      if (this.buyNum > this.stock) {
        this.buyNum = this.stock;
      }
    },
    buy() {
      this.$router.push("buy?skuId=" + this.checked + "&num=" + this.buyNum);
    }
  },
  activated() {
    this.spuId = this.$route.query.id;
    this.checked = undefined;
    this.buyStock = undefined;
    this.buyPrice = undefined;
    this.buyNum = 1;
    this.selectSpuInfo();
  },
  watch: {
    specification: {
      handler(newVal) {
        for (let value of newVal) {
          if (!value.checked) {
            this.checked = undefined;
            this.buyStock = undefined;
            this.buyPrice = undefined;
            return;
          }
        }
        let skus = this.spuInfo.skus;
        let temp = [];
        //先复制一遍
        for (let sku of skus) {
          temp.push(sku);
        }
        for (let i = 0; i < temp.length; i++) {
          let sku = temp[i];
          for (let value of newVal) {
            if (sku['specificationValue'].indexOf(value.checked) === -1) {
              temp.splice(i, 1);
              i--;
              break;
            }
          }
        }
        let finalChecked = temp[0];
        this.buyPrice = this.$toDecimal2(finalChecked.price);
        this.buyStock = finalChecked.stock;
        this.checked = finalChecked.id;
        this.checkBuy();
      },
      deep: true
    }
  },
  computed: {
    price() {
      let skus = this.spuInfo.skus;
      if (skus.length <= 0) {
        return 'loading...';
      }
      if (this.buyPrice !== undefined) {
        return this.buyPrice;
      }
      let min = skus[0].price;
      let max = skus[0].price;

      for (let sku of skus) {
        if (sku.price < min) {
          min = sku.price;
        }
        if (sku.price > max) {
          max = sku.price;
        }
      }
      if (min === max) {
        return this.$toDecimal2(min);
      }
      return this.$toDecimal2(min) + "-" + this.$toDecimal2(max);
    },
    stock() {
      if (this.buyStock !== undefined) {
        //寻找对应库存
        return this.buyStock;
      }
      //计算全部库存
      let sum = 0;
      for (let sku of this.spuInfo.skus) {
        sum += sku.stock;
      }
      return sum;
    }
  }
}
</script>

<style scoped>
.num-input {
  text-align: center;
  width: 5rem;
}

.num-input:focus {
  outline: none;
}
</style>