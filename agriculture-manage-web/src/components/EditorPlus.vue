<template>
  <v-container id="container">
    <editor
        api-key="key"
        :init="{
           height: 400,
           menubar: true,
           plugins: [
             'advlist autolink lists link image charmap print preview anchor',
             'searchreplace visualblocks code fullscreen',
             'insertdatetime media table paste code help wordcount'
           ],
           toolbar:
             'undo redo | formatselect | image | fullscreen | bold italic backcolor | \
             alignleft aligncenter alignright alignjustify | \
             bullist numlist outdent indent',
           language:'zh_CN',
           images_upload_handler:uploadImg
         }"
        id="aaaa"
        v-model="html"
    />
  </v-container>
</template>

<script>
import Editor from '@tinymce/tinymce-vue'

export default {
  name: "EditorPlus",
  props: {
    value: String
  },
  components: {
    Editor
  },
  data: () => ({
    active: false,
    html: ''
  }),
  methods: {
    uploadImg(blobInfo, success, failure) {
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
    },
  },
  watch: {
    value(newValue) {
      this.html = newValue;
    },
    html(newValue) {
      this.$emit("input", newValue)
    }
  },
  mounted() {
    this.html = this.value;
  }
}
</script>

<style scoped>

</style>