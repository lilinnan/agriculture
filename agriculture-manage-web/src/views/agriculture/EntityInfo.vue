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
          <v-list-item @click="startAddDetail(classify)">
            <v-list-item-title>添加详情</v-list-item-title>
          </v-list-item>
          <v-list-item @click="startEditClassify(classify)">
            <v-list-item-title>修改分类名称</v-list-item-title>
          </v-list-item>
          <v-list-item @click="startDeleteClassify(classify)">
            <v-list-item-title>删除该分类</v-list-item-title>
          </v-list-item>
        </v-list-group>

        <v-list-item @click="startAddClassify">
          <v-list-item-title>添加分类</v-list-item-title>
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
        <div class="text-right">
          <v-btn
              icon
              @click="startEditDetail"
          >
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-btn
              icon
              @click="dialogInfo.deleteDetailDialog=true"
          >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </div>
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
                {{ kv.attrName }}
              </td>
              <td class="text-center" style="width: 75%">
                {{ kv.attrValue }}
              </td>
            </tr>
            </tbody>
          </template>
        </v-simple-table>
        <div>
          图库
          <v-row>
            <v-col
                v-for="url in showData.detailImgs"
                class="col-md-2 col-6">
              <v-img :src="url.img" aspect-ratio="1"/>
            </v-col>
            <v-col v-if="showData.detailImgs.length===0">
              暂无图片
            </v-col>
          </v-row>
        </div>
      </v-container>
    </v-main>


    <v-dialog v-model="dialogInfo.editOrAddClassifyDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">{{ dialogInfo.editOrAddClassifyDialogTitle }}</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="classifyForm">
              <v-text-field
                  type="text"
                  autocomplete="off"
                  label="请输入分类名称"
                  v-model="editOrAddClassifyData.name"
                  required
              ></v-text-field>
            </v-form>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="resetEditClassifyData">取消</v-btn>
          <v-btn color="primary" text @click="saveClassifyData">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


    <v-dialog v-model="dialogInfo.editOrAddDetailDialog" persistent max-width="720">
      <v-card>
        <v-card-title class="headline">{{ dialogInfo.editOrAddDetailDialogTitle }}</v-card-title>
        <v-card-text>
          <v-container>
            <v-form ref="detailForm">
              <v-text-field
                  type="text"
                  autocomplete="off"
                  label="请输入详情名称"
                  v-model="editOrAddDetailData.name"
                  required
              ></v-text-field>
            </v-form>
            <attribute-list v-model="editOrAddDetailData.attrs" title="属性"/>
            <image-upload v-model="editOrAddDetailData.images"/>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="resetEditDetailData">取消</v-btn>
          <v-btn color="primary" text @click="saveDetailData">确定</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogInfo.deleteDetailDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除此详情信息吗<span class="red--text">（该操作无法恢复！！！）</span>？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="deleteDetail">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.deleteDetailDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogInfo.deleteClassifyDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除该分类及分类下的所有信息吗？<span class="red--text">（该操作无法恢复！！！）</span>？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="deleteClassify">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.deleteClassifyDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


  </v-card>
</template>

<script>
import AttributeList from "@/components/AttributeList";
import ImageUpload from "@/components/ImageUpload";

export default {
  name: "EntityInfo",
  components: {ImageUpload, AttributeList},
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
    dialogInfo: {
      editOrAddClassifyDialog: false,
      editOrAddClassifyDialogTitle: '',
      editOrAddDetailDialog: false,
      editOrAddDetailDialogTitle: '',
      deleteClassifyDialog: false,
      deleteDetailDialog: false,
    },
    editOrAddClassifyData: {
      id: -1,
      name: '',
    },
    editOrAddDetailData: {
      id: -1,
      name: '',
      attrs: [],
      images: [],
      classifyId: -1
    },
    pageInfo: {
      classifyOperateUrl: 'agriculture/classify',
      detailOperateUrl: 'agriculture/detail',
    },
    showData: undefined,
    deleteClassifyId: -1,
    needResetShowData: false
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
    startAddClassify() {
      this.dialogInfo.editOrAddClassifyDialog = true;
      this.dialogInfo.editOrAddClassifyDialogTitle = '添加分类';
      this.editOrAddClassifyData.id = -1;
    },
    startEditClassify(classify) {
      this.dialogInfo.editOrAddClassifyDialog = true;
      this.dialogInfo.editOrAddClassifyDialogTitle = '编辑分类名称';
      this.editOrAddClassifyData.id = classify.id;
      this.editOrAddClassifyData.name = classify.name;
    },
    saveClassifyData() {
      let data = {
        name: this.editOrAddClassifyData.name,
        entityId: this.entityId
      }
      if (this.editOrAddClassifyData.id === -1) {
        this.$http.post(this.pageInfo.classifyOperateUrl, data).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.getDataFromApi();
            this.resetEditClassifyData();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      Object.assign(data, {id: this.editOrAddClassifyData.id})
      this.$http.put(this.pageInfo.classifyOperateUrl, data).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.getDataFromApi();
          this.resetEditClassifyData();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    resetEditClassifyData() {
      this.dialogInfo.editOrAddClassifyDialog = false;
      this.$refs.classifyForm.reset();
    },
    startAddDetail(classify) {
      this.dialogInfo.editOrAddDetailDialog = true;
      this.dialogInfo.editOrAddDetailDialogTitle = classify.name + ">" + '添加详情';
      this.editOrAddDetailData.id = -1;
      this.editOrAddDetailData.classifyId = classify.id;
    },
    startEditDetail() {
      this.dialogInfo.editOrAddDetailDialog = true;
      this.dialogInfo.editOrAddDetailDialogTitle = this.showData.name;
      this.editOrAddDetailData.id = this.showData.id;
      //修改不需要修改classifyId，这玩意不允许改
      this.editOrAddDetailData.classifyId = -1;
      this.editOrAddDetailData.attrs = JSON.parse(this.showData.attr);
      this.editOrAddDetailData.name = this.showData.name;
      this.restoreImageList();
    },
    resetEditDetailData() {
      this.dialogInfo.editOrAddDetailDialog = false;
      this.$refs.detailForm.reset();
      this.editOrAddDetailData.attrs = [];
      this.editOrAddDetailData.images = [];
    },
    saveDetailData() {
      let data = {
        name: this.editOrAddDetailData.name,
        attr: JSON.stringify(this.editOrAddDetailData.attrs),
        classifyId: this.editOrAddDetailData.classifyId,
        detailImgs: this.getImageObject(),
      }
      if (this.editOrAddDetailData.id === -1) {
        this.$http.post(this.pageInfo.detailOperateUrl, data).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.getDataFromApi();
            this.resetEditDetailData();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      Object.assign(data, {id: this.editOrAddDetailData.id})
      this.$http.put(this.pageInfo.detailOperateUrl, data).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.getDataFromApi();
          this.loadInfo(this.showData);
          this.loadInfo(this.showData);
          this.resetEditDetailData();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    getImageObject() {
      let data = [];
      for (let i of this.editOrAddDetailData.images) {
        data.push({img: i})
      }
      return data;
    },
    restoreImageList() {
      let images = this.showData.detailImgs;
      let data = [];
      for (let i of images) {
        data.push(i.img);
      }
      this.editOrAddDetailData.images = data;
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
    deleteDetail() {
      this.$http.delete(this.pageInfo.detailOperateUrl + "/" + this.showData.id).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.getDataFromApi();
          this.showData = undefined;
          this.dialogInfo.deleteDetailDialog = false;
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    startDeleteClassify(classify) {
      this.deleteClassifyId = classify.id;
      this.dialogInfo.deleteClassifyDialog = true;
      let details = classify.details;
      for (let detail of details) {
        if (detail.id === this.showData.id) {
          this.needResetShowData = true;
          return;
        }
      }
      this.needResetShowData = false;
    },
    deleteClassify() {
      this.$http.delete(this.pageInfo.classifyOperateUrl + "/" + this.deleteClassifyId).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.dialogInfo.deleteClassifyDialog = false;
          this.getDataFromApi();
          if (this.needResetShowData) {
            this.showData = undefined;
          }
          return;
        }
        this.$toast.error(value['message']);
      })
    }
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