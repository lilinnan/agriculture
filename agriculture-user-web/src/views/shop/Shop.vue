<template>
  <v-container>
    <v-carousel
        cycle
        interval="5000"
        hide-delimiter-background
        show-arrows-on-hover
    >
      <v-carousel-item
          v-for="(item,i) in items"
          :key="i"
          :src="item.src"
      ></v-carousel-item>
    </v-carousel>
    <v-container>
      <v-subheader style="font-size: 1.5rem">最新上架</v-subheader>
      <v-row>
        <v-col class="col-6 col-md-3" v-for="spu of latestSpu">
          <spu :id="spu.id"
               :name="spu.name"
               :image-url="spu.imageUrl"
               :price="spu.price"/>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script>
import Spu from "../../components/Spu";

export default {
  name: "Main",
  components: {Spu},
  data: () => ({
    latestSpu: [],
    items: [
      {
        src: 'https://img.lilinnan.com/e163535c-364b-4dea-a7e4-32ca52ca10b1.jpg',
      },
      {
        src: 'https://img.lilinnan.com/23a3d048-4116-4546-ab3e-3b34914acaba.jpg',
      },
      {
        src: 'https://img.lilinnan.com/cdf337b5-8ac7-4df9-9189-9c3fe8c41109.jpg',
      },
      {
        src: 'https://img.lilinnan.com/f9686b44-00c4-4038-a8f3-54f149d38e24.jpg',
      },
    ],
  }),
  mounted() {
    this.$http.get("item/spu/latest/12").then(value => {
      if (value.status === 200) {
        this.latestSpu = value.data;
        return;
      }
      this.$toast.error(value['message']);
    })
  }
}
</script>

<style scoped>

</style>