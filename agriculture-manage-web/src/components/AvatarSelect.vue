<template>
  <v-dialog v-model="dialog" persistent scrollable max-width="400">
    <template v-slot:activator="{ on, attrs }">
      <v-container>
        <v-row>
          <v-col cols="12" md="4">
            设置头像：
          </v-col>
          <v-col style="padding: 0" cols="12" md="8">
            <v-avatar
                v-bind="attrs"
                v-on="on"
                label="选择头像"
                v-model="text"
                size="100"
            >
              <template v-if="img!==''">
                <v-img :src="img"/>
              </template>
              <template v-else-if="oldAvatar!==''">
                <v-img :src="oldAvatar"/>
              </template>
              <template v-else-if="img===''">
                {{ text }}
              </template>
            </v-avatar>
          </v-col>
        </v-row>
      </v-container>
    </template>
    <v-card>
      <v-card-title class="headline">选择头像</v-card-title>
      <v-card-text style="padding-bottom: 0">
        <v-container>
          <v-row>
            <v-col :align="align" style="padding-bottom: 0">
              <avatar
                  :placeholder-svg="svg"
                  :width=100
                  :height=100
                  :borderRadius=50
                  :rotation="rotation"
                  :scale="scale"
                  ref="vueAvatarSelect"
              >
              </avatar>
              <v-btn @click="select()">选择图片</v-btn>
              <v-slider label="大小" min="1" max="5" step="0.02" v-model="scale" class="mt-8"/>
              <!--              <v-slider label="旋转" min="0" max="360" step="1" v-model="rotation"/>-->
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="dialog=false">取消</v-btn>
        <v-btn color="primary" text @click="ok">确定</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import Avatar from "@/components/Avatar";

export default {
  components: {
    Avatar,
  },
  props: {
    text: "",
    oldAvatar: ""
  },
  name: "AvatarSelect",
  data: () => ({
    img: "",
    url: "",
    dialog: false,
    scale: 1,
    rotation: 0,
    svg: "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"><svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"200px\" height=\"200px\" viewBox=\"0 0 200 200\" enable-background=\"new 0 0 200 200\" xml:space=\"preserve\">  <image id=\"image0\" width=\"200\" height=\"200\" x=\"0\" y=\"0\" href=\"data:image/png;base64,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\" /></svg>",
    align: "center"
  }), methods: {
    ok() {
      let data = this.$refs.vueAvatarSelect.getImageScaled().toDataURL();
      if (data === undefined || data === 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAABZUlEQVR4Xu3TQREAAAiEQK9/aWvsAxMw4O06ysAommCuINgTFKQgmAEMp4UUBDOA4bSQgmAGMJwWUhDMAIbTQgqCGcBwWkhBMAMYTgspCGYAw2khBcEMYDgtpCCYAQynhRQEM4DhtJCCYAYwnBZSEMwAhtNCCoIZwHBaSEEwAxhOCykIZgDDaSEFwQxgOC2kIJgBDKeFFAQzgOG0kIJgBjCcFlIQzACG00IKghnAcFpIQTADGE4LKQhmAMNpIQXBDGA4LaQgmAEMp4UUBDOA4bSQgmAGMJwWUhDMAIbTQgqCGcBwWkhBMAMYTgspCGYAw2khBcEMYDgtpCCYAQynhRQEM4DhtJCCYAYwnBZSEMwAhtNCCoIZwHBaSEEwAxhOCykIZgDDaSEFwQxgOC2kIJgBDKeFFAQzgOG0kIJgBjCcFlIQzACG00IKghnAcFpIQTADGE4LKQhmAMNpIQXBDGA4LQQL8oTPAGUY76lBAAAAAElFTkSuQmCC') {
        data = '';
      }
      this.img = data;

      if (this.img !== '') {
        let params = new URLSearchParams();
        params.append("avatar", this.img);
        this.$http.post("admin-info/avatar", params, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }).then(value => {
          if (value.status === 200) {
            this.$toast.success("上传成功");
            this.url = value.data;
            this.dialog = false;
            return;
          }
          this.$toast.error(value['message']);
        })
        return;
      }
      this.$toast.warning("请选择头像");
    },
    resetImage() {
      this.scale = 1;
      this.rotation = 0;
      if (this.$refs.vueAvatarSelect !== undefined) {
        this.$refs.vueAvatarSelect.resetImage();
      }
      this.img = "";
      this.url = "";
      this.dialog = false;
    },
    getUrl() {
      return this.url ? this.url : (this.oldAvatar ? this.oldAvatar : "https://img.lilinnan.com/133a73b8-efd8-459a-996f-55d8c2b4789e.png");
    }, select() {
      this.$refs.vueAvatarSelect.selectFile();
    }
  }
}
</script>

<style scoped>

</style>