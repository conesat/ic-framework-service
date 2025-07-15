<template>
  <div style="width: 100%;height: 100%">
    <baidu-map
      :center="center"
      :zoom="zoom"
      style="width: 100%; min-height: 200px;height: 100%;"
      @click="onMapClick"
      @ready="onMapReady"
      :scroll-wheel-zoom="true"
    >
      <bm-marker
        v-if="markerPoint"
        :position="markerPoint"
      />
    </baidu-map>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, computed } from "vue";

const emit = defineEmits(['change', 'cancel']);
const props = defineProps({
  lng: { type: Number, default: null },
  lat: { type: Number, default: null },
  city: { type: String, default: null }
});

const zoom = ref(20);
const center = ref(props.city || {lng: props.lng, lat: props.lat} || '北京');
const markerPoint = ref(props.lng && props.lat ? {lng: props.lng, lat: props.lat} : null);

watch(() => [props.lng, props.lat], ([lng, lat]) => {
  if (lng && lat) {
    markerPoint.value = {lng, lat};
    center.value = {lng, lat};
  }
});

watch(() => props.city, (city) => {
  if (city) {
    center.value = city;
  }
});

const onMapReady = (mapInstance: any) => {
  // 可在此处获取 map 实例
};

const onMapClick = ({ point }: any) => {
  if (!point) return;
  markerPoint.value = { lng: point.lng, lat: point.lat };
  // 使用百度地图API获取详细地址
  const geocoder = new (window as any).BMap.Geocoder();
  geocoder.getLocation(point, (rs: any) => {
    let addComp = rs.addressComponents;
    emit("change", {
      lng: point.lng,
      lat: point.lat,
      address: `${addComp.province}${addComp.city}${addComp.district}${addComp.street}${addComp.streetNumber}`
    });
  });
};
</script>

<style scoped>
#mapContainer {
  width: 100%;
  height: 500px;
}
</style>
