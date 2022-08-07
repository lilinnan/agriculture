<template>
  <v-card style="position: fixed;overflow-y:scroll;overflow-x:hidden;">
    <v-navigation-drawer v-model="drawer" fixed clipped>
      <v-list nav dense :class="{'mt-64':$vuetify.breakpoint.lgAndUp}">
        <v-list-group no-action v-for="classify in classifyList">
          <template v-slot:activator>
            <v-list-item-title v-text="classify.name"/>
          </template>
          <template v-for="detail in classify.details">
            <v-list-item @click="loadInfo(detail)">
              <v-list-item-title v-text="detail.name"/>
            </v-list-item>
          </template>
        </v-list-group>
      </v-list>
      <v-list v-if="classifyList && classifyList.length===0">
        <v-list-item>
          <v-list-item-title>暂无信息</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar color="primary" dark fixed clipped-left>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" v-show="!$vuetify.breakpoint.lgAndUp"></v-app-bar-nav-icon>
      <v-toolbar-title>{{ entityName }}</v-toolbar-title>
      <v-spacer/>
      <v-btn icon @click="close">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container v-if="showData">
        <v-simple-table>
          <template v-slot:default>
            <thead>
            <tr>
              <th class="text-center text--primary" style="font-size: 1.3rem;" colspan="2">{{ showData.name }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="kv of JSON.parse(showData.attr)">
              <td class="text-center" style="width: 25%">
                {{ kv['attrName'] }}
              </td>
              <td class="text-center" style="width: 75%">
                {{ kv['attrValue'] }}
              </td>
            </tr>
            </tbody>
          </template>
        </v-simple-table>
        <div>
          图库
          <v-row>
            <v-col
                v-for="url in showData['detailImgs']"
                class="col-md-2 col-6">
              <v-img :src="url.img" aspect-ratio="1"/>
            </v-col>
            <v-col v-if="showData['detailImgs'].length===0">
              暂无图片
            </v-col>
          </v-row>
        </div>
      </v-container>
    </v-main>

  </v-card>
</template>

<script>
export default {
  name: "EntityInfo",
  props: {
    value: {
      type: Boolean,
      default: false
    },
    entityId: Number,
    entityName: String
  },
  data: () => ({
    drawer: null,
    classifyList: [],
    pageInfo: {
      classifyOperateUrl: 'agriculture/classify',
      detailOperateUrl: 'agriculture/detail',
    },
    showData: undefined,
  }),
  methods: {
    close() {
      this.$emit("input", false);
    },
    onResize() {
      if (this.$vuetify.breakpoint.lgAndUp && this.value) {
        this.drawer = true;
      }
    },
    getDataFromApi() {
      this.$http.get(this.pageInfo.classifyOperateUrl + "/" + this.entityId).then(value => {
        if (value.status === 200) {
          this.classifyList = value.data;
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    loadInfo(detail) {
      this.$http.get(this.pageInfo.detailOperateUrl + "/" + detail.id).then(value => {
        if (value.status === 200) {
          this.showData = value.data;
          return;
        }
        this.$toast.error(value['message']);
      })
    },
  },
  watch: {
    value(v) {
      if (v && this.$vuetify.breakpoint.lgAndUp) {
        this.drawer = true;
      }
    },
    entityId: {
      immediate: true,
      handler: function () {
        this.getDataFromApi();
        this.showData = undefined;
      }
    }
  },
  beforeDestroy() {
    if (typeof window === 'undefined') {
      return;
    }
    window.removeEventListener('resize', this.onResize)
  },
  mounted() {
    this.onResize();
    window.addEventListener('resize', this.onResize, {passive: true})
  }

}
</script>

<style scoped>
.mt-64 {
  margin-top: 64px
}
</style>