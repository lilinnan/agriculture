<template>
  <v-container>
    <v-data-table
        :items-per-page=5
        :headers="tableInfo.headers"
        :items="data"
        :multi-sort="true">

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

      <template v-slot:item.attrValue="{ item }">
        <span style="word-break: break-all">{{item.attrValue}}</span>
      </template>

      <template v-slot:top>
        <v-toolbar flat rounded>
          <v-toolbar-title>{{ title }}</v-toolbar-title>
          <v-spacer></v-spacer>
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
                        label="属性名称"
                        v-model="editOrAddData.attrName"
                        :rules="editRules.attrNameRule"
                        required
                    ></v-text-field>

                    <v-textarea
                        rows="3"
                        type="text"
                        autocomplete="off"
                        label="属性值"
                        v-model="editOrAddData.attrValue"
                        :rules="editRules.attrValueRule"
                        required
                    ></v-textarea>

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
export default {
  name: "AttributeList",
  props: {
    value: Array,
    title: String
  },
  data: () => ({
    data: [],
    tableInfo: {
      headers: [
        {text: '属性名称', value: 'attrName'},
        {text: '属性值', value: 'attrValue',width:'65%'},
        {text: '操作', value: 'actions', sortable: false},
      ],
    },
    dialogInfo: {
      editOrAddDialog: false,
      editOrAddDialogTitle: '',
      deleteDialog: false,
    },
    editRules: {
      attrNameRule: [
        v => !!v || '请输入属性名称',
      ], attrValueRule: [
        v => !!v || '请输入属性值',
      ]
    },
    editOrAddData: {
      id: -1,
      attrName: '',
      attrValue: '',
      obj: undefined
    },
    deleteData: {
      deleteId: -1
    }
  }),
  methods: {
    startEdit(item) {
      this.dialogInfo.editOrAddDialogTitle = "编辑";
      this.dialogInfo.editOrAddDialog = true;
      this.editOrAddData.id = item.id;
      this.editOrAddData.attrName = item.attrName;
      this.editOrAddData.attrValue = item.attrValue;
      this.editOrAddData.obj = item
    },
    startAdd() {
      this.dialogInfo.editOrAddDialogTitle = "添加";
      this.editOrAddData.id = -1;
    },
    resetEditData() {
      this.dialogInfo.editOrAddDialog = false
      this.$refs.form.reset();
    },
    save() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      if (this.isExist(this.editOrAddData.attrName, this.editOrAddData.id)) {
        this.$toast.error("该属性名称已存在");
        return;
      }
      if (this.editOrAddData.id === -1) {
        let data = {
          id: this.getNewId(),
          attrName: this.editOrAddData.attrName,
          attrValue: this.editOrAddData.attrValue
        }
        this.data.push(data);
        this.resetEditData();
        this.$toast.success("添加成功");
        return;
      }
      this.editOrAddData.obj.attrName = this.editOrAddData.attrName;
      this.editOrAddData.obj.attrValue = this.editOrAddData.attrValue;
      this.$toast.success("修改成功");
      this.resetEditData();
    },
    startDelete(item) {
      this.dialogInfo.deleteDialog = true;
      this.deleteData.deleteId = item.id;
    },
    realDelete() {
      for (let i = 0; i < this.data.length; i++) {
        let attr = this.data[i];
        if (attr.id === this.deleteData.deleteId) {
          this.data.splice(i, 1);
          this.dialogInfo.deleteDialog = false;
          this.$toast.success("删除成功");
          return;
        }
      }
    },
    getNewId() {
      let maxId = -1;
      for (let attr of this.data) {
        if (attr.id > maxId) {
          maxId = attr.id;
        }
      }
      return maxId + 1;
    },
    isExist(attrName, id) {
      for (let attr of this.data) {
        if (attr.attrName === attrName && attr.id !== id) {
          return true;
        }
      }
      return false;
    }
  },
  watch: {
    data: {
      handler(newValue) {
        this.$emit("input", newValue)
      },
      deep: true
    },
    value(newValue) {
      this.data = newValue;
    }
  },
  mounted() {
    if (!this.value) {
      return;
    }
    this.data = this.value
  }
}
</script>

<style scoped>

</style>