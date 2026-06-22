

<template>

</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";

const data = reactive({
  id: router.currentRoute.value.query.id
})
export default {
  name: "test"
}
</script>