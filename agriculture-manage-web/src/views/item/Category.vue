<template>
  <v-container>
    <span class="headline">商品分类数据</span>
    <v-row>
      <v-col>
        <v-treeview
            open-all
            :items="items"
            selection-type="independent"
            return-object
        >
          <template v-slot:append="{ item }">
            <v-btn icon small>
              <v-icon small @click="editCategory(item.name,item.id)">mdi-pencil</v-icon>
            </v-btn>
            <v-btn icon small>
              <v-icon small @click="addCategory(item.name,item.id)">mdi-plus</v-icon>
            </v-btn>
            <v-btn icon small>
              <v-icon small @click="deleteCategory(item.id)">mdi-delete</v-icon>
            </v-btn>
          </template>
        </v-treeview>
        <v-dialog v-model="editOrAddDialog" max-width="500px" persistent>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                v-bind="attrs"
                v-on="on"
                @click="addCategory('根节点',1)"
            >
              创建新的分类
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="headline">{{ editOrAddDialogTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-form ref="form">
                  <v-text-field
                      type="text"
                      autocomplete="off"
                      label="请输入分类名称"
                      v-model="editOrAddData.name"
                      :rules="editOrAddRules.nameRule"
                      required
                  ></v-text-field>
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
      </v-col>
    </v-row>

    <v-dialog v-model="deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除此分类及以下的所有数据吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="deleteDialog = false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  name: "Category",
  data: () => ({
    items: [],
    deleteDialog: false,
    deleteData: {
      deleteId: -1
    },
    editOrAddDialog: false,
    editOrAddDialogTitle: '',
    editOrAddData: {
      type: 'add',
      id: -1,
      name: ''
    }, editOrAddRules: {
      nameRule: [
        v => !!v || '请输入分类名称',
      ],
    }
  }),
  methods: {
    resetEditData() {
      this.editOrAddDialog = false;
      this.$refs.form.reset();
    },
    save() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      if (this.editOrAddData.type === 'add') {
        this.$http.post('item/category', {
          'parentId': this.editOrAddData.id,
          'name': this.editOrAddData.name
        }).then(value => {
          if (value.status === 200) {
            this.$toast.success('添加成功');
            this.refresh();
            this.resetEditData();
            return;
          }
          this.$toast.error(value['message']);
        })
      } else {
        this.$http.put('item/category', {
          'id': this.editOrAddData.id,
          'name': this.editOrAddData.name
        }).then(value => {
          if (value.status === 200) {
            this.$toast.success('编辑成功');
            this.refresh();
            this.resetEditData();
            return;
          }
          this.$toast.error(value['message']);
        })
      }
    },
    addCategory(itemName, id) {
      this.editOrAddDialog = true;
      this.editOrAddData.type = 'add';
      this.editOrAddDialogTitle = '在' + itemName + '下添加新的分类';
      this.editOrAddData.id = id;
    },
    editCategory(itemName, id) {
      this.editOrAddDialog = true;
      this.editOrAddData.type = 'edit';
      this.editOrAddDialogTitle = '编辑分类名称';
      this.editOrAddData.id = id;
      this.editOrAddData.name = itemName;
    },
    deleteCategory(itemId) {
      this.deleteData.deleteId = itemId;
      this.deleteDialog = true;
    },
    realDelete() {
      this.$http.delete('item/category/' + this.deleteData.deleteId).then(value => {
        this.deleteDialog = false;
        if (value.status === 200) {
          this.$toast.success('删除成功');
          this.refresh();
          this.resetEditData();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    refresh() {
      this.$http.get('item/category').then(value => {
        if (value.status === 200) {
          this.items = value.data;
          return;
        }
        this.$toast.error(value['message']);
      })
    }
  },
  activated() {
    this.refresh();
  }
}
</script>

<style scoped>

</style>