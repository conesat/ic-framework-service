<template>
  <view class="map-page">
    <map
      id="energy-station-map"
      class="map-canvas"
      :latitude="mapCenter.latitude"
      :longitude="mapCenter.longitude"
      :scale="mapScale"
      :markers="mapMarkers"
      :show-location="true"
      :enable-scroll="true"
      :enable-zoom="true"
      :enable-rotate="false"
      :enable-3d="false"
      @markertap="handleMarkerTap"
    ></map>

    <view class="map-header">
      <view class="map-title-row">
        <view class="map-title-copy">
          <text class="map-title">附近换电站</text>
          <text class="map-subtitle">已发现 {{ filteredStations.length }} 个可用站点</text>
        </view>
        <view class="map-help" @tap="showMapGuide">
          <uv-icon name="question-circle" color="#536174" size="21"></uv-icon>
        </view>
      </view>

      <view class="search-bar">
        <uv-icon name="search" color="#8592A3" size="18"></uv-icon>
        <input
          v-model="searchKeyword"
          class="station-search-input"
          placeholder="搜索站点名称或地址"
          placeholder-class="search-placeholder"
          confirm-type="search"
          @focus="openStationList"
          @confirm="openStationList"
        />
        <view class="location-city">
          <uv-icon name="map-fill" color="#358AF5" size="14"></uv-icon>
          <text>深圳</text>
        </view>
      </view>

      <scroll-view class="filter-scroll" scroll-x :show-scrollbar="false">
        <view class="filter-row">
          <view
            v-for="item in filters"
            :key="item"
            class="filter-chip"
            :class="{ active: activeFilter === item }"
            @tap="applyFilter(item)"
          >{{ item }}</view>
        </view>
      </scroll-view>
    </view>

    <view class="location-button" @tap="recenter">
      <uv-icon name="map-fill" color="#358AF5" size="22"></uv-icon>
    </view>

    <view v-if="selectedStation" class="station-sheet">
      <view class="sheet-handle"></view>
      <view class="sheet-heading">
        <view class="sheet-heading-copy">
          <text>距离最近</text>
          <text>骑行约 {{ selectedStation.minutes }} 分钟</text>
        </view>
        <view class="open-map-list" @tap="openStationList">
          <text>站点列表</text>
          <uv-icon name="list" color="#358AF5" size="16"></uv-icon>
        </view>
      </view>

      <view class="selected-card" @tap="openStation">
        <view class="station-logo">
          <view class="cabinet-mini"><view v-for="n in 6" :key="n"></view></view>
        </view>
        <view class="station-copy">
          <view class="station-name">
            <text>{{ selectedStation.name }}</text>
            <text>营业中</text>
          </view>
          <text class="station-address">{{ selectedStation.address }}</text>
          <view class="station-meta">
            <text>{{ selectedStation.distance }}</text>
            <text>24 小时营业</text>
          </view>
        </view>
        <uv-icon name="arrow-right" color="#A9B4C2" size="14"></uv-icon>
      </view>

      <view class="station-stock-row">
        <view class="stock-item">
          <text>{{ selectedStation.stock }}</text>
          <text>可用满电电池</text>
        </view>
        <view class="stock-item">
          <text>{{ selectedStation.slots }}</text>
          <text>可还空位</text>
        </view>
        <view class="go-station" @tap="openStation">
          <uv-icon name="map-fill" color="#FFFFFF" size="17"></uv-icon>
          <text>去这里</text>
        </view>
      </view>
    </view>

    <view v-if="listVisible" class="station-list-panel">
      <view class="list-panel-header">
        <view class="list-panel-title">
          <text>换电站列表</text>
          <text>{{ filteredStations.length }} 个结果</text>
        </view>
        <view class="close-list" @tap="closeStationList">
          <uv-icon name="close" color="#647488" size="19"></uv-icon>
        </view>
      </view>
      <view class="list-filter-summary">
        <text>{{ activeFilter }}</text>
        <text v-if="searchKeyword.trim()">“{{ searchKeyword.trim() }}”</text>
        <text>按距离排序</text>
      </view>
      <scroll-view class="station-list-scroll" scroll-y :show-scrollbar="false">
        <view v-for="station in filteredStations" :key="station.id" class="list-station-card" @tap="selectStationFromList(station)">
          <view class="station-logo list-station-logo">
            <view class="cabinet-mini"><view v-for="n in 6" :key="n"></view></view>
          </view>
          <view class="list-station-copy">
            <view class="list-station-name">
              <text>{{ station.name }}</text>
              <text>{{ station.open24 ? '24小时' : '营业中' }}</text>
            </view>
            <text class="list-station-address">{{ station.address }}</text>
            <view class="list-station-meta">
              <text>{{ station.distance }}</text>
              <text>骑行约 {{ station.minutes }} 分钟</text>
            </view>
            <view class="list-stock-row">
              <text><text>{{ station.stock }}</text> 块满电</text>
              <text><text>{{ station.slots }}</text> 个可还位</text>
            </view>
          </view>
          <view class="list-detail-button" @tap.stop="openStation(station)">详情</view>
        </view>
        <view v-if="!filteredStations.length" class="station-empty">
          <uv-icon name="search" color="#9BA8B7" size="25"></uv-icon>
          <text>没有找到匹配的换电站</text>
          <text>试试切换筛选条件或搜索其他关键词</text>
        </view>
        <view class="list-bottom-space"></view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import { getNearbyStations } from '../../api/energy'

export default {
  name: 'EnergyMap',
  data() {
    return {
      allStations: [
        { id: 1, name: '科技园换电站', address: '科技园南区科苑路15号', stock: 12, slots: 8, distance: '326m', minutes: 2, latitude: 22.5407, longitude: 113.9536, open24: true },
        { id: 2, name: '深大地铁站换电点', address: '深南大道深大地铁站A口', stock: 8, slots: 5, distance: '680m', minutes: 4, latitude: 22.5358, longitude: 113.9424, open24: true },
        { id: 3, name: '软件产业基地站', address: '滨海大道软件产业基地', stock: 3, slots: 9, distance: '1.2km', minutes: 7, latitude: 22.5324, longitude: 113.9621, open24: false },
        { id: 4, name: '高新园北区站', address: '高新北六道16号', stock: 6, slots: 4, distance: '1.6km', minutes: 9, latitude: 22.5488, longitude: 113.9461, open24: true },
        { id: 5, name: '后海总部基地换电站', address: '后海大道与创业路交汇处', stock: 10, slots: 6, distance: '2.1km', minutes: 12, latitude: 22.5264, longitude: 113.9434, open24: true },
        { id: 6, name: '南山科技金融城站', address: '科苑南路与白石路交汇处', stock: 5, slots: 11, distance: '2.8km', minutes: 15, latitude: 22.5242, longitude: 113.9582, open24: false }
      ],
      fallbackStations: [],
      selectedStation: null,
      mapCenter: { latitude: 22.5407, longitude: 113.9536 },
      mapScale: 14,
      filters: ['距离最近', '电池充足', '可还电池', '24小时'],
      activeFilter: '距离最近',
      searchKeyword: '',
      listVisible: false
    }
  },
  computed: {
    stations() {
      const list = [...this.allStations]
      if (this.activeFilter === '电池充足') return list.filter(item => item.stock >= 6)
      if (this.activeFilter === '可还电池') return list.filter(item => item.slots > 0)
      if (this.activeFilter === '24小时') return list.filter(item => item.open24)
      return list.sort((a, b) => a.minutes - b.minutes)
    },
    filteredStations() {
      const keyword = this.searchKeyword.trim().toLowerCase()
      if (!keyword) return this.stations
      return this.stations.filter(item => `${item.name}${item.address}${item.district || ''}`.toLowerCase().includes(keyword))
    },
    mapMarkers() {
      return this.filteredStations.map((station, index) => {
        const isSelected = this.selectedStation && Number(this.selectedStation.id) === Number(station.id)
        return {
          id: Number(station.id || index + 1),
          latitude: station.latitude,
          longitude: station.longitude,
          iconPath: '/static/imgs/station-marker.png',
          width: isSelected ? 38 : 32,
          height: isSelected ? 48 : 40,
          anchor: { x: 0.5, y: 1 },
          callout: {
            content: `${station.stock} 块满电`,
            color: '#FFFFFF',
            fontSize: 11,
            borderRadius: 16,
            bgColor: isSelected ? '#3389F7' : '#5D9DF2',
            padding: 7,
            display: isSelected ? 'ALWAYS' : 'BYCLICK',
            textAlign: 'center'
          }
        }
      })
    }
  },
  created() {
    this.fallbackStations = [...this.allStations]
    this.selectedStation = this.allStations[0]
    this.loadStations()
  },
  methods: {
    setMapCenter(station, scale = 15) {
      if (!station) return
      this.mapCenter = { latitude: station.latitude, longitude: station.longitude }
      this.mapScale = scale
    },
    async loadStations() {
      try {
        const stations = await getNearbyStations(30)
        if (Array.isArray(stations) && stations.length) {
          this.allStations = stations.map((item, index) => this.normalizeStation(item, index))
          this.selectedStation = this.allStations[0]
          this.setMapCenter(this.selectedStation, 14)
        }
      } catch (_) {
        // 服务暂不可用时仍展示本地兜底站点，确保找站和筛选流程可用。
        this.allStations = this.fallbackStations
      }
    },
    normalizeStation(station, index) {
      const fallback = this.fallbackStations[index % this.fallbackStations.length] || {}
      const stock = Number(station.availableBatteryCount ?? station.stock ?? fallback.stock ?? 0)
      const slots = Number(station.availableReturnSlots ?? station.slots ?? fallback.slots ?? 0)
      const distance = station.distance || fallback.distance || '--'
      const minutes = Number(station.minutes ?? fallback.minutes ?? index * 3 + 2)
      return {
        ...station,
        id: station.id ?? fallback.id ?? index + 1,
        name: station.name || fallback.name || '换电站',
        address: station.address || fallback.address || '地址待完善',
        stock,
        slots,
        distance,
        minutes,
        latitude: Number(station.latitude ?? fallback.latitude),
        longitude: Number(station.longitude ?? fallback.longitude),
        open24: station.open24 ?? (/24\s*小时|24h/i.test(station.businessHours || '') || fallback.open24 || false)
      }
    },
    selectStation(station) {
      this.selectedStation = station
      this.setMapCenter(station)
    },
    selectStationFromList(station) {
      this.selectStation(station)
      this.listVisible = false
    },
    handleMarkerTap(event) {
      const markerId = Number(event.detail && event.detail.markerId)
      const station = this.stations.find(item => Number(item.id) === markerId)
      if (station) this.selectStation(station)
    },
    applyFilter(filter) {
      this.activeFilter = filter
      const visible = this.filteredStations
      if (!visible.some(item => item.id === this.selectedStation?.id)) this.selectedStation = visible[0] || this.allStations[0]
      this.setMapCenter(this.selectedStation, 14)
    },
    openStation(station = this.selectedStation) {
      const id = station?.id
      uni.navigateTo({ url: id ? `/pages/station/detail?id=${id}` : '/pages/station/detail' })
    },
    recenter() {
      uni.getLocation({
        type: 'gcj02',
        success: ({ latitude, longitude }) => {
          this.mapCenter = { latitude, longitude }
          this.mapScale = 15
          uni.showToast({ title: '已定位到当前位置', icon: 'none' })
        },
        fail: () => {
          this.activeFilter = '距离最近'
          this.selectedStation = this.allStations[0]
          this.setMapCenter(this.selectedStation, 14)
          uni.showToast({ title: '暂未获取定位，已展示附近站点', icon: 'none' })
        }
      })
    },
    openStationList() {
      this.listVisible = true
    },
    closeStationList() {
      this.listVisible = false
    },
    showMapGuide() {
      uni.showModal({ title: '找站说明', content: '可按距离、电池库存、可还空位和营业时间筛选。点击“站点列表”可比较全部站点，点击任一站点可回到地图定位。', showCancel: false })
    }
  }
}
</script>

<style scoped>
.map-page { height: 100%; position: relative; overflow: hidden; color: #253043; background: #EAF2F9; }
.map-canvas { width: 100%; height: 100%; position: absolute; inset: 0; }
.map-header { position: absolute; z-index: 3; left: 0; right: 0; top: 0; padding: calc(var(--status-bar-height, 0px) + 38rpx) 30rpx 20rpx; background: linear-gradient(180deg, rgba(248,251,255,.98) 0%, rgba(248,251,255,.94) 78%, rgba(248,251,255,0) 100%); }
.map-title-row { display: flex; align-items: center; justify-content: space-between; }
.map-title-copy { display: flex; flex-direction: column; }
.map-title { color: #253043; font-size: 36rpx; font-weight: 700; }
.map-subtitle { margin-top: 7rpx; color: #8A97A7; font-size: 23rpx; }
.map-help { width: 62rpx; height: 62rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #FFFFFF; box-shadow: 0 10rpx 28rpx rgba(37,66,99,.095); }
.search-bar { height: 78rpx; margin-top: 24rpx; padding: 0 20rpx; display: flex; align-items: center; border: 2rpx solid #E8EDF3; border-radius: 24rpx; background: rgba(255,255,255,.98); box-shadow: 0 11rpx 30rpx rgba(37,66,99,.08); }
.search-placeholder { min-width: 0; margin-left: 11rpx; overflow: hidden; color: #8C98A7; font-size: 24rpx; text-overflow: ellipsis; white-space: nowrap; }
.location-city { flex-shrink: 0; margin-left: auto; padding-left: 14rpx; display: flex; align-items: center; gap: 5rpx; border-left: 2rpx solid #EEF1F5; color: #3C8AF2; font-size: 23rpx; }
.filter-scroll { width: 100%; margin-top: 17rpx; white-space: nowrap; }
.filter-row { display: inline-flex; padding-right: 20rpx; gap: 11rpx; }
.filter-chip { height: 55rpx; padding: 0 21rpx; display: flex; align-items: center; border: 2rpx solid rgba(218,226,235,.96); border-radius: 19rpx; color: #69788A; background: rgba(255,255,255,.94); font-size: 23rpx; }
.filter-chip.active { border-color: #C7DDFC; color: #3488F3; background: #E8F2FF; font-weight: 600; }
.location-button { width: 68rpx; height: 68rpx; position: absolute; z-index: 3; right: 28rpx; bottom: calc(538rpx + env(safe-area-inset-bottom)); display: flex; align-items: center; justify-content: center; border: 2rpx solid #E2EAF2; border-radius: 22rpx; background: rgba(255,255,255,.97); box-shadow: 0 9rpx 24rpx rgba(42,72,105,.12); }
.station-sheet { position: absolute; z-index: 4; left: 18rpx; right: 18rpx; bottom: calc(144rpx + env(safe-area-inset-bottom)); padding: 15rpx 28rpx 23rpx; border: 2rpx solid rgba(226,233,241,.95); border-radius: 32rpx; background: rgba(255,255,255,.98); box-shadow: 0 -15rpx 42rpx rgba(35,65,98,.12); }
.sheet-handle { width: 65rpx; height: 7rpx; margin: 0 auto 13rpx; border-radius: 5rpx; background: #D9E0E8; }
.sheet-heading { display: flex; align-items: center; justify-content: space-between; }
.sheet-heading-copy { display: flex; align-items: baseline; min-width: 0; }
.sheet-heading-copy text:first-child { color: #2A3546; font-size: 28rpx; font-weight: 700; }
.sheet-heading-copy text:last-child { margin-left: 12rpx; color: #98A3B1; font-size: 21rpx; }
.open-map-list { flex-shrink: 0; display: flex; align-items: center; gap: 6rpx; color: #358AF5; font-size: 22rpx; }
.selected-card { margin-top: 17rpx; padding: 22rpx; display: flex; align-items: center; border-radius: 25rpx; background: #FFFFFF; box-shadow: 0 10rpx 28rpx rgba(36,66,100,.07); }
.station-logo { width: 78rpx; height: 78rpx; flex: 0 0 78rpx; display: flex; align-items: center; justify-content: center; border-radius: 21rpx; background: #E8F3FF; }
.cabinet-mini { width: 39rpx; height: 46rpx; padding: 5rpx; display: flex; flex-wrap: wrap; gap: 4rpx; border: 3rpx solid #398BF4; border-radius: 7rpx; }
.cabinet-mini view { width: calc(50% - 2rpx); height: calc(33.333% - 3rpx); border-radius: 2rpx; background: #398BF4; }
.station-copy { min-width: 0; margin-left: 16rpx; display: flex; flex: 1; flex-direction: column; }
.station-name { min-width: 0; display: flex; align-items: center; gap: 9rpx; }
.station-name text:first-child { min-width: 0; overflow: hidden; color: #253043; font-size: 27rpx; font-weight: 650; text-overflow: ellipsis; white-space: nowrap; }
.station-name text:last-child { flex-shrink: 0; padding: 4rpx 8rpx; border-radius: 7rpx; color: #26A778; background: #E5F7F0; font-size: 20rpx; }
.station-address { margin-top: 7rpx; overflow: hidden; color: #8D99A9; font-size: 21rpx; text-overflow: ellipsis; white-space: nowrap; }
.station-meta { margin-top: 8rpx; display: flex; align-items: center; color: #6F7D8F; font-size: 21rpx; }
.station-meta text + text { margin-left: 17rpx; padding-left: 17rpx; border-left: 2rpx solid #E3E8EE; }
.station-stock-row { margin-top: 18rpx; padding-top: 16rpx; display: flex; align-items: center; border-top: 2rpx solid #F0F2F6; }
.stock-item { min-width: 0; display: flex; align-items: baseline; }
.stock-item + .stock-item { margin-left: 28rpx; }
.stock-item text:first-child { color: #3187F3; font-size: 32rpx; font-weight: 700; }
.stock-item text:last-child { margin-left: 6rpx; color: #8E99A8; font-size: 21rpx; white-space: nowrap; }
.go-station { height: 64rpx; margin-left: auto; padding: 0 18rpx; flex-shrink: 0; display: flex; align-items: center; justify-content: center; gap: 6rpx; border-radius: 20rpx; color: #FFFFFF; background: linear-gradient(135deg, #4A9DF8, #3185F0); box-shadow: 0 8rpx 18rpx rgba(49,133,240,.22); font-size: 23rpx; font-weight: 600; }
@media screen and (max-width: 360px) {
  .map-header { padding-right: 22rpx; padding-left: 22rpx; }
  .station-sheet { left: 12rpx; right: 12rpx; padding-right: 20rpx; padding-left: 20rpx; }
  .selected-card { padding: 18rpx; }
  .stock-item + .stock-item { margin-left: 15rpx; }
  .stock-item text:last-child { font-size: 20rpx; }
  .go-station { padding: 0 13rpx; }
}
@media screen and (max-height: 700px) {
  .location-button { bottom: calc(516rpx + env(safe-area-inset-bottom)); }
  .station-sheet { padding-top: 10rpx; padding-bottom: 16rpx; }
  .selected-card { margin-top: 12rpx; }
  .station-stock-row { margin-top: 12rpx; padding-top: 12rpx; }
}


/* Flat map controls retain contrast through outlines; only the bottom sheet keeps a restrained lift. */
.map-help,.location-button{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.search-bar,.selected-card{border:2rpx solid var(--gy-surface-border);box-shadow:none}
.station-sheet{border-color:#E2E9F1;box-shadow:var(--gy-shadow-float)}
.go-station{box-shadow:var(--gy-shadow-brand)}


/* Station directory: a filter-aware list remains usable even when the map provider is unavailable. */
.station-search-input{min-width:0;height:100%;margin-left:11rpx;flex:1;color:#354154;font-size:24rpx}
.station-list-panel{position:absolute;z-index:8;top:calc(var(--status-bar-height, 0px) + 310rpx);right:18rpx;bottom:calc(144rpx + env(safe-area-inset-bottom));left:18rpx;padding:22rpx 20rpx 0;display:flex;flex-direction:column;border:2rpx solid var(--gy-surface-border);border-radius:30rpx;background:#F8FBFF;box-shadow:var(--gy-shadow-float)}
.list-panel-header{display:flex;align-items:center;justify-content:space-between;padding:0 4rpx}
.list-panel-title{min-width:0;display:flex;align-items:baseline;gap:10rpx}
.list-panel-title text:first-child{color:#283447;font-size:29rpx;font-weight:700}
.list-panel-title text:last-child{color:#8B98A8;font-size:21rpx}
.close-list{width:64rpx;height:64rpx;display:flex;align-items:center;justify-content:center;border:2rpx solid var(--gy-surface-border);border-radius:20rpx;background:#FFFFFF}
.list-filter-summary{min-height:52rpx;margin-top:13rpx;padding:0 14rpx;display:flex;align-items:center;gap:9rpx;border-radius:15rpx;color:#6B7C90;background:#EDF5FF;font-size:20rpx}
.list-filter-summary text+text{padding-left:9rpx;border-left:2rpx solid #D8E6F7}
.station-list-scroll{height:0;min-height:0;margin-top:12rpx;flex:1}
.list-station-card{min-height:172rpx;margin-bottom:12rpx;padding:20rpx;display:flex;align-items:center;border:2rpx solid var(--gy-surface-border);border-radius:24rpx;background:#FFFFFF}
.list-station-logo{width:72rpx;height:72rpx;flex-basis:72rpx;border-radius:20rpx}
.list-station-copy{min-width:0;margin-left:14rpx;display:flex;flex:1;flex-direction:column}
.list-station-name{min-width:0;display:flex;align-items:center;gap:8rpx}
.list-station-name text:first-child{min-width:0;overflow:hidden;color:#2C394B;font-size:25rpx;font-weight:650;text-overflow:ellipsis;white-space:nowrap}
.list-station-name text:last-child{flex-shrink:0;padding:4rpx 8rpx;border-radius:8rpx;color:#2E9B74;background:#E7F7F0;font-size:18rpx}
.list-station-address{margin-top:7rpx;overflow:hidden;color:#8996A6;font-size:20rpx;text-overflow:ellipsis;white-space:nowrap}
.list-station-meta,.list-stock-row{display:flex;align-items:center;color:#718094;font-size:19rpx}
.list-station-meta{margin-top:9rpx}.list-station-meta text+text{margin-left:11rpx;padding-left:11rpx;border-left:2rpx solid #E3EAF1}
.list-stock-row{margin-top:8rpx;gap:12rpx}.list-stock-row>text{white-space:nowrap}.list-stock-row text text{color:#348AF4;font-weight:700}
.list-detail-button{height:64rpx;margin-left:11rpx;padding:0 15rpx;flex-shrink:0;display:flex;align-items:center;justify-content:center;border:2rpx solid #B9D7FB;border-radius:18rpx;color:#3287F1;background:#F1F7FF;font-size:21rpx;font-weight:600}
.station-empty{min-height:280rpx;padding:30rpx 20rpx;display:flex;align-items:center;justify-content:center;flex-direction:column;color:#65768A;font-size:23rpx}.station-empty text:nth-child(2){margin-top:16rpx;font-weight:600}.station-empty text:last-child{margin-top:9rpx;color:#9AA7B5;font-size:20rpx}.list-bottom-space{height:12rpx}
@media screen and (max-width:360px){.station-list-panel{right:12rpx;left:12rpx;padding-right:16rpx;padding-left:16rpx}.list-station-card{min-height:164rpx;padding:17rpx}.list-station-logo{width:66rpx;height:66rpx;flex-basis:66rpx}.list-station-copy{margin-left:11rpx}.list-stock-row{gap:8rpx;font-size:18rpx}.list-detail-button{height:60rpx;margin-left:8rpx;padding:0 11rpx;font-size:20rpx}}
@media screen and (max-height:700px){.station-list-panel{top:calc(var(--status-bar-height, 0px) + 292rpx);bottom:calc(136rpx + env(safe-area-inset-bottom))}.list-station-card{min-height:154rpx;padding-top:16rpx;padding-bottom:16rpx}.list-filter-summary{margin-top:9rpx}}

</style>
