<template>


  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="800">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary"
            dark
            v-bind="attrs"
            v-on="on"
        >
          打开编辑器
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="headline">测试编辑器</v-card-title>
        <v-card-text>

        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="dialog = false">Disagree</v-btn>
          <v-btn color="green darken-1" text @click="dialog = false">Agree</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {

  name: "Test3",
  data: () => ({
    dialog: false
  }),
  methods: {
    upload(blobInfo, success, failure) {
      let formData = new FormData();
      formData.append("file", blobInfo.blob());
      this.$http.post("file-upload", formData).then(value => {
        if (value.status === 200) {
          success(value['data']);
          return;
        }
        failure(value['message']);
        this.$toast.error(value['message']);
      })
    }
  }
}
</script>
<style scoped>

</style>