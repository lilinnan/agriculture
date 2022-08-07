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

      <template v-slot:item.roles="{ item }">
        <template v-if="item['roles'].length > 0">
          <v-chip x-small :color="randomColor(role.id)" v-for="role in item['roles']">
            {{ role['description'] }}
          </v-chip>
        </template>
        <template v-else>
          无角色
        </template>
      </template>

      <template v-slot:item.accountNonLocked="{ item }">
        {{ item['accountNonLocked'] ? '否' : '是' }}
      </template>

      <template v-slot:item.avatar="{ item }">
        <v-avatar>
          <v-img :src="item['avatar']"/>
        </v-avatar>
      </template>


      <template v-slot:item.enabled="{ item }">
        {{ item['enabled'] ? '是' : '否' }}
      </template>


      <template v-slot:item.actions="{ item }">
        <v-icon
            small
            class="mr-2"
            @click="editAdminUserData(item)"
        >
          mdi-pencil
        </v-icon>
        <v-icon
            small
            @click="deleteAdminUserRole(item)"
        >
          mdi-delete
        </v-icon>
      </template>

      <template v-slot:top>
        <v-toolbar flat rounded>
          <v-toolbar-title>管理员信息</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-text-field
              v-model="searchKey"
              @blur="getAdminUserData"
              ref="searchInput"
              @keyup.enter="$refs.searchInput.blur()"
              style="margin: 0;padding: 0"
              append-icon="mdi-magnify"
              label="搜索（输入用户id、用户名、描述均可）"
              single-line
              hide-details
          ></v-text-field>
          <v-dialog v-model="editOrAddDialog" max-width="500px" persistent>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  icon
                  v-bind="attrs"
                  v-on="on"
                  @click="addAdminUser"
              >
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ dialogTitle }}</span>
              </v-card-title>
              <v-card-text>
                <v-container>
                  <v-form ref="form">
                    <v-text-field
                        type="text"
                        autocomplete="off"
                        :label="usernameReadonly?'用户名无法修改':'用户名'"
                        v-model="editData.username"
                        :rules="editRules.usernameRule"
                        :readonly="usernameReadonly"
                        required
                    ></v-text-field>
                    <v-text-field
                        type="text"
                        autocomplete="off"
                        label="用户描述"
                        v-model="editData.description"
                        :rules="editRules.userDescriptionRule"
                        required
                    ></v-text-field>
                    <v-text-field
                        type="password"
                        autocomplete="off"
                        label="用户密码"
                        v-model="editData.password"
                        :rules="editRules.passwordRule.concat(passwordNextRule)"
                        required
                    ></v-text-field>

                    <v-row justify="space-around">
                      <v-switch v-model="editData.locked"
                                :label="editData.locked?'已锁定':'未锁定'"></v-switch>
                      <v-switch v-model="editData.enabled" :label="editData.enabled?'已启用':'未启用'"></v-switch>
                    </v-row>
                    <v-autocomplete
                        v-model="editData.roleSelect.values"
                        :items="editData.roleSelect.items"
                        item-text="description"
                        item-value="id"
                        chips
                        small-chips
                        label="选择角色"
                        auto-select-first
                        clearable
                        deletable-chips
                        hide-no-data
                        multiple>
                    </v-autocomplete>
                    <avatar-select text="请选择头像" :old-avatar="editData.oldAvatar" ref="avatarSelect"/>
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
        <v-card-text>您确定要删除此管理员吗？
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
import AvatarSelect from "@/components/AvatarSelect";

export default {
  components: {AvatarSelect},
  data: () => ({
    usernameReadonly: true,
    searchKey: '',
    total: 0,
    data: [],
    loading: true,
    options: {},
    headers: [
      {text: 'ID', value: 'id'},
      {text: '用户名', value: 'username'},
      {text: '头像', value: 'avatar', sortable: false},
      {text: '角色', value: 'roles', sortable: false, width: "20%"},
      {text: '描述', value: 'description'},
      {text: '锁定', value: 'accountNonLocked'},
      {text: '启用', value: 'enabled'},
      {text: '操作', value: 'actions', sortable: false},
    ],
    editOrAddDialog: false,
    deleteDialog: false,
    dialogTitle: '',
    editRules: {
      usernameRule: [
        v => !!v || '请输入用户名',
        v => /^[\w]{5,16}$/.test(v) || '用户名不合法',
      ], userDescriptionRule: [
        v => !!v || '请输入用户描述',
      ], passwordRule: [
        v => true
      ]
    },
    /**
     * 弹窗数据部分
     */
    editData: {
      id: -1,
      oldAvatar: '',
      username: '',
      password: '',
      enabled: true,
      locked: false,
      description: '',
      roleSelect: {
        items: undefined,
        values: [],
      },
    },
    deleteData: {
      deleteId: -1
    },
    color: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange', 'red', 'yellow',],
    rotation: 0,
    scale: 1,
    borderRadius: 0
  }),
  watch: {
    options: {
      handler() {
        this.getAdminUserData();
      },
      deep: true,
    },
  },
  methods: {
    editAdminUserData(item) {
      let temp = [];
      let roles = item['roles'];
      roles.forEach(role => {
        temp.push(role.id)
      })
      this.editData.id = item.id;
      this.editData.password = '';
      this.editData.roleSelect.values = temp;
      this.editData.username = item.username;
      this.editData.description = item.description;
      this.editOrAddDialog = true;
      this.dialogTitle = "编辑";
      this.editData.enabled = item.enabled;
      this.editData.locked = !item['accountNonLocked'];
      this.editData.oldAvatar = item.avatar;
      this.usernameReadonly = true;
    },
    addAdminUser() {
      this.dialogTitle = "添加";
      this.editData.id = -1;
      this.editData.locked = false;
      this.editData.enabled = true;
      this.usernameReadonly = false;
    },
    getAdminUserData() {
      this.loading = true;
      this.$search("authority/admin-user", this.options, this.searchKey).then(result => {
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
      this.editData.password = '';
      this.editData.username = '';
      this.editData.description = "";
      this.editData.roleSelect.values = [];
      this.editData.oldAvatar = '';
      this.$refs.avatarSelect.resetImage();
      this.$refs.form.reset();
    },
    save() {
      if (!this.$refs.form.validate()) {
        this.$toast.warning("请检查您输入的数据");
        return;
      }
      let data = {
        id: this.editData.id,
        username: this.editData.username,
        password: this.editData.password,
        accountNonLocked: !this.editData.locked,
        enabled: this.editData.enabled,
        description: this.editData.description,
        roles: this.editData.roleSelect.values,
        avatar: this.$refs.avatarSelect.getUrl()
      }
      if (this.editData.id === -1) {
        this.$http.post("authority/admin-user", data).then(value => {
          if (value.status === 200) {
            this.$toast.success("添加成功");
            this.getAdminUserData();
            this.resetEditData();
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      this.$http.put("authority/admin-user", data).then(value => {
        if (value.status === 200) {
          this.$toast.success("修改成功");
          this.getAdminUserData();
          if (this.editData.username === this.$root['userInfo'].username) {
            this.$root['userInfo'].avatar = data.avatar;
          }
          this.resetEditData();
          return;
        }
        this.$toast.error(value['message']);
      })
    },
    realDelete() {
      this.$http.delete("authority/admin-user/" + this.deleteData.deleteId).then(value => {
        if (value.status === 200) {
          this.$toast.success("删除成功");
          this.getAdminUserData();
          this.deleteDialog = false;
          return;
        }
        this.deleteDialog = false;
        this.$toast.error(value['message']);
      })
    },
    deleteAdminUserRole(item) {
      this.deleteDialog = true;
      this.deleteData.deleteId = item.id;
    },
    randomColor(id) {
      return this.color[id % this.color.length] + (this.$vuetify.theme.dark ? " darken-3" : " lighten-3");
    },
    onImageReady: function onImageReady() {
      this.scale = 1;
      this.rotation = 0;
    }
  },
  activated() {
    this.$http.get("authority/role/simple").then(result => {
      if (result.status === 200) {
        this.editData.roleSelect.items = result.data.list
        return;
      }
      this.$toast.error(result['message']);
    })
  },
  computed: {
    passwordNextRule() {
      if (this.editData.id !== -1) {
        if (this.editData.password === undefined || this.editData.password === '') {
          return true;
        }
        if (/^[\w]{6,16}$/.test(this.editData.password)) {
          return true;
        }
        return "新密码必须由数字、字母或下划线组成，长度在6~16位之间";
      }
      if (this.editData.password === undefined || this.editData.password === '') {
        return "请输入密码";
      }
      if (/^[\w]{6,16}$/.test(this.editData.password)) {
        return true;
      }
      return "新密码必须由数字、字母或下划线组成，长度在6~16位之间";
    },
  }
}
</script>

<style scoped>

</style>