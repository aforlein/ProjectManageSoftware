<template>
  <div class="map-container">
    <div id="map" style="height: 100%; width: 100%"></div>

    <div class="basemap-switcher">
      <el-button-group>
        <el-button :type="currentBasemap === 'vector' ? 'primary' : 'default'" size="small" @click="switchBasemap('vector')">矢量</el-button>
        <el-button :type="currentBasemap === 'imagery' ? 'primary' : 'default'" size="small" @click="switchBasemap('imagery')">影像</el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMapProjects } from '@/api/map'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import 'leaflet.markercluster'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

// 修复 Leaflet 默认图标在 Vite 打包后路径丢失的问题
delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow
})

let map = null
let vectorLayer = null   // ArcGIS 矢量
let imageryLayer = null  // ArcGIS 影像
let markerCluster = null
const currentBasemap = ref('vector')

onMounted(async () => {
  map = L.map('map').setView([35.86, 104.19], 5)

  // ArcGIS 矢量地图（World Street Map）
  vectorLayer = L.tileLayer('https://server.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}', {
    attribution: '&copy; Esri',
    maxZoom: 18
  })

  // ArcGIS 影像地图（World Imagery）
  imageryLayer = L.tileLayer('https://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
    attribution: '&copy; Esri',
    maxZoom: 18
  })

  // 默认显示矢量
  vectorLayer.addTo(map)

  markerCluster = L.markerClusterGroup({ maxClusterRadius: 50 })

  try {
    const projects = await getMapProjects()
    const statusMap = { planning: '规划中', in_progress: '进行中', completed: '已完成', closed: '已关闭', on_hold: '已暂停' }

    projects.forEach(p => {
      if (!p.latitude || !p.longitude) return
      const marker = L.marker([Number(p.latitude), Number(p.longitude)])
      marker.bindPopup(`
        <b>${p.projectName}</b><br/>
        客户: ${p.customerName || '-'}<br/>
        状态: ${statusMap[p.status] || p.status}<br/>
        <a href="#/project/detail/${p.id}">查看详情</a>
      `)
      markerCluster.addLayer(marker)
    })

    map.addLayer(markerCluster)

    const validMarkers = projects.filter(p => p.latitude && p.longitude)
    if (validMarkers.length > 0) {
      map.fitBounds(validMarkers.map(p => [Number(p.latitude), Number(p.longitude)]), { padding: [50, 50] })
    }
    L.control.scale({ metric: true, imperial: false, position: 'bottomleft' }).addTo(map)
  } catch {}
})

function switchBasemap(type) {
  if (currentBasemap.value === type) return
  currentBasemap.value = type

  if (type === 'vector') {
    map.removeLayer(imageryLayer)
    vectorLayer.addTo(map)
  } else {
    map.removeLayer(vectorLayer)
    imageryLayer.addTo(map)
  }
}
</script>
