<template>
  <iframe
    width="100%"
    height="100%"
    :src="videoUrl"
    title="VideoPlayer"
    frameborder="0"
    allow="
      accelerometer;
      autoplay;
      clipboard-write;
      encrypted-media;
      gyroscope;
      picture-in-picture;
      web-share;
    "
    referrerpolicy="strict-origin-when-cross-origin"
    allowfullscreen
  ></iframe>
</template>

<script setup>
defineProps(['videoUrl'])
</script>
