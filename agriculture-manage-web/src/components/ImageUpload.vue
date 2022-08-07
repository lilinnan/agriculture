<template>
  <div>
    请添加图片
    <input type="file" multiple ref="fileInput" @change="fileChangeEvent" class="d-none" accept="image/*">
    <v-card flat>
      <v-container fluid>
        <v-row>
          <v-col
              v-for="url in fileUrls"
              cols="4">
            <v-hover v-slot:default="{ hover }">
              <v-img
                  :src="url"
                  aspect-ratio="1">
                <template v-slot:placeholder>
                  <v-row
                      class="fill-height ma-0"
                      align="center"
                      justify="center"
                  >
                    <v-progress-circular indeterminate color="grey"></v-progress-circular>
                  </v-row>
                </template>
                <v-expand-transition>
                  <div
                      v-if="hover"
                      class="d-flex transition-fast-in-fast-out v-card--reveal"
                      style="height: 100%;"
                  >
                    <v-btn @click="remove(url)" icon color="error">
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </div>
                </v-expand-transition>
              </v-img>
            </v-hover>
          </v-col>
          <v-col v-if="fileUrls.length<max"
                 cols="4">
            <v-img
                @click="select()"
                src="@/assets/img/add.svg"
                aspect-ratio="1" style="cursor: pointer"></v-img>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
    <v-overlay :value="overlay">
      <v-progress-linear :value="progress"></v-progress-linear>
      <div>{{ tip }}</div>
    </v-overlay>
    <v-dialog v-model="deleteDialog" persistent max-width="360">
      <v-card>
        <v-card-title class="headline">警告</v-card-title>
        <v-card-text>您确定要删除此图片吗？
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="realRemove">确定</v-btn>
          <v-btn color="primary" text @click="deleteDialog=false">取消</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div>
</template>

<script>
export default {
  name: "ImageUpload",
  props: {
    value: {
      type: Array,
      default() {
        return [];
      }
    },
    max: {
      type: Number,
      default: 9999999
    }
  },
  data: () => ({
    overlay: false,
    fileUrls: [],
    tip: '',
    progress: 0,
    deleteDialog: false,
    deleteUrl: ''
  }),
  methods: {
    fileChangeEvent(e) {
      let files = e.target.files || e.dataTransfer.files;
      this.$emit('select-file', files);
      if (!files.length) {
        return;
      }
      this.uploadFile(files);
    },
    remove(url) {
      this.deleteDialog = true;
      this.deleteUrl = url;
    },
    realRemove() {
      let temp = [];
      for (let i = 0; i < this.fileUrls.length; i++) {
        let url = this.fileUrls[i];
        if (url !== this.deleteUrl) {
          temp.push(url);
        }
      }
      this.fileUrls = temp;
      this.deleteDialog = false;
    },
    select() {
      this.$refs.fileInput.click()
    },
    async uploadFile(files) {
      this.overlay = true;
      for (let i = 0; i < files.length; i++) {
        this.tip = "图片上传中" + (i + 1) + "/" + files.length;
        let file = files[i];
        let formData = new FormData();
        formData.append("file", file);
        let config = {
          onUploadProgress: progressEvent => {
            this.progress = progressEvent.loaded / progressEvent.total * 100 | 0;
          }
        }
        const result = await this.$http.post("file-upload", formData, config);
        if (result.status === 200) {
          this.fileUrls.push(result.data);
        }
      }
      this.overlay = false;
      this.$refs.fileInput.value = '';
    },
  }, watch: {
    fileUrls(val) {
      this.$emit("input", val);
    },
    value(val) {
      this.fileUrls = val;
    }
  },
  mounted() {
    this.fileUrls = this.value;
  }
}
</script>

<style scoped>
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  position: absolute;
  width: 100%;
}
</style>