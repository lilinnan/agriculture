<template>
  <v-card hover :to="'spu-detail?id='+id">
    <v-card-text>
      <v-img :src="imageUrl" aspect-ratio="1"></v-img>
    </v-card-text>
    <v-card-actions>
      <v-row>
        <v-col class="col-12 spu-title">
          {{ name }}
        </v-col>
        <v-col class="col-12 red--text"><span>￥</span>
          <span style="font-size: 1.2rem;font-weight: bold">{{ yuan }}</span>
          <span style="font-size: 1.2rem;font-weight: bold">.</span>
          <span style="font-size: 0.8rem">{{ fen }}</span>
        </v-col>
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "Spu",
  props: {
    id: Number,
    name: String,
    imageUrl: String,
    price: Number,
  },
  data: () => ({
    strPrice: "0.00"
  }),
  methods: {
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
  },
  computed: {
    fen() {
      return this.strPrice.substring(this.strPrice.indexOf(".") + 1);
    },
    yuan() {
      return this.strPrice.substring(0, this.strPrice.indexOf("."))
    }
  },
  mounted() {
    this.strPrice = this.toDecimal2(this.price / 100);
  }
}
</script>

<style scoped>
.spu-title {
  line-height: 1.2rem;
  font-size: 0.9rem;
  height: 2.4rem;
  overflow: hidden;
  padding-top: 0;
  padding-bottom: 0;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>