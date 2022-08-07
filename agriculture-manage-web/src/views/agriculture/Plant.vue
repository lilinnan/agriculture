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

      <template v-slot:item.image="{ item }">
        <v-img class="mt-2 mb-2" :src="item.image" height="100px" width="100px"/>
      </template>

      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="startEdit(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            class="mr-2"
            @click="showInfo(item)"
        >
          mdi-information-outline
        </v-icon>
        <v-icon
            small
            @click="startDelete(item)"
        >
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
              hide-details
          ></v-text-field>
          <v-dialog v-model="dialogInfo.editOrAddDialog" max-width="480px" persistent>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  icon
                  v-bind="attrs"
                  v-on="on"
                  @click="startAdd"
              >
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ dialogInfo.editOrAddDialogTitle }}</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-form ref="form">
                    <v-text-field
                        type="text"
                        autocomplete="off"
                        label="植物名称"
                        v-model="editOrAddData.name"
                        :rules="editRules.nameRule"
                        required
                    ></v-text-field>
                    <v-textarea
                        rows="3"
                        type="text"
                        autocomplete="off"
                        label="植物描述"
                        v-model="editOrAddData.description"
                        :rules="editRules.descriptionRule"
                        required
                    ></v-textarea>
                    <image-upload :max=1 v-model="editOrAddData.imageList"/>
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
        </v-toolbar>
      </template>
    </v-data-table>
    <v-dialog v-model="dialogInfo.deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除吗？</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="dialogInfo.deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>


    <v-dialog v-model="dialogInfo.infoDialog" fullscreen hide-overlay scrollable>
      <entity-info v-model="dialogInfo.infoDialog" :entity-id="pageInfo.showEntityId"
                   :entity-name="pageInfo.showEntityName"/>
    </v-dialog>
  </v-container>
</template>

<script>
import ImageUpload from "../../components/ImageUpload";
import EntityInfo from "./EntityInfo";


export default {
  name: "Plant",
  components: {EntityInfo, ImageUpload},
  data: () => ({
    pageInfo: {
      pageTitle: '植物信息',
      searchTitle: '搜索（输入植物id、植物名称均可）',
      optionUrl: 'agriculture/entity',
      showEntityId: 0,
      showEntityName: ''
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
        {text: '名称', value: 'name'},
        {text: '图片', value: 'image'},
        {text: '介绍', value: 'description'},
        {text: '操作', value: 'actions', sortable: false},
      ],
    },
    options: {},
    dialogInfo: {
      editOrAddDialog: false,
      editOrAddDialogTitle: '',
      deleteDialog: false,
      infoDialog: false
    },
    editRules: {
      nameRule: [
        v => !!v || '请输入名称',
      ], descriptionRule: [
        v => !!v || '请输入描述',
      ],
    },
    editOrAddData: {
      id: -1,
      name: '',
      description: '',
      imageList: []
    },
    deleteData: {
      deleteId: -1
    },
    otherData: {
      searchKey: '',
      color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',]
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
      this.$search(this.pageInfo.optionUrl + "/plant", this.options, this.otherData.searchKey).then(result => {
        if (result.status === 200) {
          this.showData.data = result.data.list
          this.showData.total = result.data.total
          this.tableInfo.loading = false
          return;
        }
        this.$toast.error(result['message']);
      });
    },
    startEdit(item) {
      this.dialogInfo.editOrAddDialogTitle = "编辑";
      this.editOrAddData.id = item.id;
      this.dialogInfo.editOrAddDialog = true;
      this.editOrAddData.imageList.push(item['image']);
      this.editOrAddData.name = item.name;
      this.editOrAddData.description = item.description;
    },
    startAdd() {
      this.dialogInfo.editOrAddDialogTitle = "添加";
      this.editOrAddData.id = -1;
    },
    showInfo(item) {
      this.dialogInfo.infoDialog = true;
      this.pageInfo.showEntityId = item.id;
      this.pageInfo.showEntityName = item.name;
    },
    resetEditData() {
      this.dialogInfo.editOrAddDialog = false
      this.editOrAddData.imageList = [];
      this.$refs.form.reset()
    },
    save() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      if (!this.editOrAddData.imageList.length) {
        this.$toast.warning("请选择图片");
        return;
      }
      let data = {
        name: this.editOrAddData.name,
        image: this.editOrAddData.imageList[0],
        description: this.editOrAddData.description,
        plant: true
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
      Object.assign(data, {id: this.editOrAddData.id})
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
      return this.color[id % this.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>