<template>
  <v-container>
    <v-data-table
        :items-per-page=5
        :headers="headers"
        :items="data"
        :options.sync="options"
        :server-items-length="total"
        :loading="loading"
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

      <template v-slot:item.authorities="{ item }">
        <template v-if="item['authorities'].length > 0">
          <v-chip x-small :color="randomColor(authority.id)" v-for="authority in item['authorities']">
            {{ authority['description'] }}
          </v-chip>
        </template>
        <template v-else>
          无权限
        </template>
      </template>

      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editRoleData(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteRole(item)"
        >
          mdi-delete
        </v-icon>
      </template>

      <template v-slot:top>
        <v-toolbar flat rounded>
          <v-toolbar-title>角色管理</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-text-field
              v-model="searchKey"
              @blur="getRoleData"
              ref="searchInput"
              @keyup.enter="$refs.searchInput.blur()"
              style="margin: 0;padding: 0"
              append-icon="mdi-magnify"
              label="搜索（输入角色id、角色、描述均可）"
              single-line
              hide-details
          ></v-text-field>
          <v-dialog v-model="editOrAddDialog" max-width="600px" persistent>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  icon
                  v-bind="attrs"
                  v-on="on"
                  @click="addRole"
              >
                <v-icon>mdi-plus</v-icon>
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
                        label="角色"
                        v-model="editData.role"
                        :rules="editRules.roleRule"
                        required
                    ></v-text-field>
                    <v-text-field
                        type="text"
                        autocomplete="off"
                        label="角色描述"
                        v-model="editData.description"
                        :rules="editRules.roleDescriptionRule"
                        required
                    ></v-text-field>
                    <v-autocomplete
                        v-model="editData.authoritySelect.values"
                        :items="editData.authoritySelect.items"
                        item-text="description"
                        item-value="id"
                        chips
                        small-chips
                        label="选择权限"
                        auto-select-first
                        clearable
                        deletable-chips
                        hide-no-data
                        multiple>
                    </v-autocomplete>
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
    <v-dialog v-model="deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除此角色吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realDelete">确定</v-btn>
          <v-btn color="primary" text @click="deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    searchKey: '',
    total: 0,
    data: [],
    loading: true,
    options: {},
    headers: [
      {text: 'ID', value: 'id'},
      {text: '角色', value: 'role'},
      {text: '权限', value: 'authorities', sortable: false, width: "35%"},
      {text: '描述', value: 'description'},
      {text: '操作', value: 'actions', sortable: false},
    ],
    editOrAddDialog: false,
    editOrAddDialogTitle: '',
    editRules: {
      roleRule: [
        v => !!v || '请输入角色',
      ], roleDescriptionRule: [
        v => !!v || '请输入角色描述',
      ]
    },
    /**
     * 弹窗数据部分
     */
    editData: {
      id: -1,
      role: '',
      description: '',
      authoritySelect: {
        items: undefined,
        values: [],
      },
    },
    deleteDialog: false,
    deleteData: {
      deleteId: -1
    },
    color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',]
  }),
  watch: {
    options: {
      handler() {
        this.getRoleData();
      },
      deep: true,
    },
  },
  methods: {
    editRoleData(item) {
      let temp = [];
      let authorities = item['authorities'];
      authorities.forEach(authority => {
        temp.push(authority.id)
      })
      this.editData.id = item.id;
      this.editData.authoritySelect.values = temp;
      this.editData.role = item.role;
      this.editData.description = item.description;
      this.editOrAddDialog = true;
      this.editOrAddDialogTitle = "编辑";

    },
    addRole() {
      this.editOrAddDialogTitle = "添加";
      this.editData.id = -1;
    },
    getRoleData() {
      this.loading = true;
      this.$search("authority/role", this.options, this.searchKey).then(result => {
        if (result.status === 200) {
          this.data = result.data.list
          this.total = result.data.total
          this.loading = false
          return;
        }
        this.$toast.error(result['message']);
      });
    },
    resetEditData() {
      this.editOrAddDialog = false
      this.editData.role = "";
      this.editData.description = "";
      this.editData.authoritySelect.values = [];
      this.$refs.form.reset()
    },
    save() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      let data = {
        "id": this.editData.id,
        "role": this.editData.role,
        "description": this.editData.description,
        "authorities": this.editData.authoritySelect.values
      }
      if (this.editData.id === -1) {
        this.$http.post("authority/role", data).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.getRoleData();
            this.resetEditData();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      this.$http.put("authority/role", data).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.getRoleData();
          this.resetEditData();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    realDelete() {
      this.$http.delete("authority/role/" + this.deleteData.deleteId).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.getRoleData();
          this.deleteDialog = false;
          return;
        }
        this.deleteDialog = false;
        this.$toast.error(value['message']);
      })
    },
    deleteRole(item) {
      this.deleteDialog = true;
      this.deleteData.deleteId = item.id;
    },
    randomColor(id) {
      return this.color[id % this.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
  },
  activated() {
    this.$http.get("authority/authority/simple").then(result => {
      if (result.status === 200) {
        this.editData.authoritySelect.items = result.data.list
        return;
      }
      this.$toast.error(result['message']);
    })
  }
}
</script>

<style scoped>

</style>