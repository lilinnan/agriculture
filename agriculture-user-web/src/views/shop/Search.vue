<template>
  <v-container>
    <v-subheader style="font-size: 1.5rem">'{{ key }}'的搜索结果</v-subheader>
    <template v-if="spus.length<=0">
      <v-container>
        暂时未找到此商品
      </v-container>
    </template>
    <template v-if="spus.length>0">
      <v-row>
        <v-col class="col-6 col-md-3" v-for="spu of spus">
          <spu :id="spu.id"
               :name="spu.name"
               :image-url="spu.imageUrl"
               :price="spu.price"/>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="8">
          <v-container class="max-width">
            <v-pagination
                v-model="options.page"
                @input="getDataFromApi"
                class="my-4"
                :length="maxPage"
            ></v-pagination>
          </v-container>
        </v-col>
      </v-row>
    </template>
  </v-container>
</template>

<script>
import Spu from "../../components/Spu";

export default {
  name: "Search",
  components: {Spu},
  data: () => ({
    spus: [],
    key: '',
    maxPage: 20,
    options: {
      page: 1,
      itemsPerPage: 12,
      sortBy: [],
      sortDesc: []
    }
  }),
  methods: {
    getDataFromApi() {
      this.$search("item/spu/search", this.options, this.key).then(value => {
        if (value.status === 200) {
          this.spus = value.data.list;
          this.maxPage = value.data.pages;
          return;
        }
        this.$toast.error(value['message']);
      });
    }
  },
  activated() {
    this.key = this.$route.query['key'];
    this.getDataFromApi();
  }
}
</script>

<style scoped>

</style>