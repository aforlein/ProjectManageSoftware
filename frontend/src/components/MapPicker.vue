<template>
  <el-dialog v-model="visible" title="在地图上选点" width="700px" @opened="initMap" @close="handleClose">
    <div style="position:relative">
      <div id="pickerMap" style="height:400px;width:100%"></div>
      <div style="position:absolute;top:50%;left:50%;transform:translate(-50%,-100%);pointer-events:none;z-index:1000">
        <el-icon :size="32" color="#F56C6C"><MapLocation /></el-icon>
      </div>
    </div>
    <div style="margin-top:12px;display:flex;gap:12px">
      <el-input v-model="pickedLng" placeholder="经度" readonly size="small" style="width:160px">
        <template #prepend>经度</template>
      </el-input>
      <el-input v-model="pickedLat" placeholder="纬度" readonly size="small" style="width:160px">
        <template #prepend>纬度</template>
      </el-input>
      <el-input v-model="pickedAddress" placeholder="地址（自动识别）" readonly size="small" style="flex:1">
        <template #prepend>地址</template>
      </el-input>
    </div>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="confirmPick">确认选择</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow
})

const emit = defineEmits(['confirm'])
const visible = ref(true)
const pickedLng = ref('')
const pickedLat = ref('')
const pickedAddress = ref('')
let pickerMap = null
let pickerMarker = null

async function initMap() {
  await nextTick()
  if (pickerMap) { pickerMap.invalidateSize(); return }

  pickerMap = L.map('pickerMap').setView([35.86, 104.19], 5)

  L.tileLayer('https://server.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
    attribution: '&copy; Esri',
    maxZoom: 18
  }).addTo(pickerMap)

  // 点击地图放置标记
  pickerMap.on('click', async (e) => {
    const { lat, lng } = e.latlng
    pickedLng.value = lng.toFixed(6)
    pickedLat.value = lat.toFixed(6)

    if (pickerMarker) pickerMap.removeLayer(pickerMarker)
    pickerMarker = L.marker([lat, lng]).addTo(pickerMap)

    // 逆地理编码
    try {
      const resp = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=18&accept-language=zh`)
      const data = await resp.json()
      pickedAddress.value = data.display_name || ''
    } catch {
      pickedAddress.value = ''
    }
  })
}

function confirmPick() {
  if (!pickedLng.value || !pickedLat.value) {
    ElMessage.warning('请先在地图上点击选择位置')
    return
  }
  emit('confirm', {
    longitude: Number(pickedLng.value),
    latitude: Number(pickedLat.value),
    location: pickedAddress.value
  })
  visible.value = false
}

function handleClose() {
  if (pickerMap) {
    pickerMap.remove()
    pickerMap = null
    pickerMarker = null
  }
}
</script>
