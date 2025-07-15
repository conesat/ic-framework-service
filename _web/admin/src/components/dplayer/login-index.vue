<template>
  <div style="width: 100%;height: 100%">
    <div ref="videoRef" style="transition: opacity 0.4s linear"
         :style="{'opacity': loaded ? 1 : 0}"></div>
    <template v-if="loaded">
      <div class="play">
        <t-progress theme="circle"
                    size="50"
                    :strokeWidth="2"
                    :label="false"
                    :percentage="percentage"
                    trackColor="rgba(255,255,255,0)"
                    color="rgba(255,255,255,0.2)"></t-progress>
        <div @click.stop="playOrPause"
             style="position: absolute;top: 0;left: 0;line-height: 50px;width: 50px;text-align: center;">
          <PlayIcon v-if="paused" size="40"/>
          <PauseIcon v-else size="40"/>
        </div>
      </div>
      <t-popconfirm content="视频有声音哦，打开声音听一下吧？" :visible="showVolumePop" placement="left" @confirm="setVolume" @cancel="showVolumePop=false">
        <div @click.stop="setVolume" class="voice">
          <SoundMute1Icon v-if="volume === 0" size="20"/>
          <SoundIcon v-else size="20"/>
        </div>
      </t-popconfirm>
    </template>

    <div class="blur"></div>
  </div>
</template>

<script setup>
import {PlayIcon, PauseIcon, SoundMute1Icon, SoundIcon} from "tdesign-icons-vue-next"
import DPlayer from 'dplayer'
import Hls from 'hls.js';
import {ref, reactive, onBeforeUnmount, onMounted, nextTick} from 'vue'

const loaded = ref(false)
const paused = ref(false)
const showVolumePop = ref(true)
const volume = ref(0)
const percentage = ref(0)
const videoRef = ref()
const videoTarget = ref()
const state = reactive({
  instance: null
})

const props = defineProps({
  // 是否自动播放
  autoplay: {
    type: Boolean,
    default: true
  },
  // 主题色
  theme: {
    type: String,
    default: '#0093ff'
  },
  // 视频是否循环播放
  loop: {
    type: Boolean,
    default: true
  },
  // 语言(可选值: 'en', 'zh-cn', 'zh-tw')
  lang: {
    type: String,
    default: 'zh-cn'
  },
  // 是否开启截图(如果开启，视频和视频封面需要允许跨域)
  screenshot: {
    type: Boolean,
    default: false
  },
  // 是否开启热键
  hotkey: {
    type: Boolean,
    default: false
  },
  // 视频是否预加载(可选值: 'none', 'metadata', 'auto')
  preload: {
    type: String,
    default: 'auto'
  },
  // 默认音量
  volume: {
    type: Number,
    default: 0
  },
  // 可选的播放速率，可以设置成自定义的数组
  playbackSpeed: {
    type: Array,
    default: []
  },
  // 视频信息
  video: {
    type: Object,
  },
  // 外挂字幕
  subtitle: {
    type: Object,
    default: {}
  },
  // 显示弹幕
  danmaku: {
    type: Object,
    default: {}
  },
  // 自定义右键菜单
  contextmenu: {
    type: Array,
    default: []
  },
  // 自定义进度条提示点
  highlight: {
    type: Array,
    default: []
  },
  // 阻止多个播放器同时播放，当前播放器播放时暂停其他播放器
  mutex: {
    type: Boolean,
    default: false
  }
})
onMounted(() => {
  let player = {
    preventClickToggle: true,
    container: videoRef.value,
    autoplay: props.autoplay,
    theme: props.theme,
    loop: props.loop,
    lang: props.lang,
    screenshot: props.screenshot,
    hotkey: props.hotkey,
    preload: props.preload,
    volume: props.volume,
    playbackSpeed: props.playbackSpeed,
    video: props.video,
    contextmenu: props.contextmenu,
    highlight: props.highlight,
    mutex: props.mutex,
  }
  if (props.subtitle.url) {
    player.subtitle = props.subtitle
  }
  if (props.danmaku) {
    player.danmaku = props.danmaku
  }
  state.instance = new DPlayer(player)

  state.instance.on('timeupdate', function (e) {
    if (e.target.duration) {
      percentage.value = e.target.currentTime / e.target.duration * 100;
    }
  });
  state.instance.on('loadeddata', function (e) {
    videoTarget.value = e.target
    resizeTarget();
  });
  state.instance.on('canplay', function (e) {
    loaded.value = true
  });
  state.instance.on('pause', function (e) {
    paused.value = true
  });
  state.instance.on('play', function (e) {
    paused.value = false
  });
})
const setVolume = () => {
  showVolumePop.value = false
  if (volume.value === 0) {
    volume.value = 1
  } else {
    volume.value = 0
  }
  state.instance.volume(volume.value, true, false);
}

const playOrPause = (e) => {
  paused.value = !paused.value;
  state.instance.toggle()
}

const resizeTarget = () => {
  const vH = videoTarget.value.clientHeight;
  const vW = videoTarget.value.clientWidth;
  const bH = document.body.clientHeight;
  const bW = document.body.clientWidth;
  if (bH - vH > bW - vW) {
    const sc = bH / vH;
    videoTarget.value.style.height = bH + 'px'
    videoTarget.value.style.width = vW * sc + 'px'
    videoTarget.value.style.marginLeft = -(vW * sc - vW) / 2 + 'px'
  } else {
    const sc = bW / vW;
    videoTarget.value.style.width = bW + 'px'
    videoTarget.value.style.height = vH * sc + 'px'
    videoTarget.value.style.marginTop = -(vH * sc - vH) / 2 + 'px'
  }
}
// 销毁
onBeforeUnmount(() => {
  state.instance.destroy()
})
console.log(props.video.url)
window.addEventListener('resize', resizeTarget)
</script>

<style lang='less' scoped>
// 禁用视频右键菜单
:deep(.dplayer-menu) {
  display: none !important;
}

:deep(.dplayer-mask) {
  display: none !important;
}

//隐藏底部操作栏
:deep(.dplayer-controller) {
  display: none;
}

//隐藏底部操作栏动画
:deep(.dplayer-controller-mask) {
  display: none;
}

/* 隐藏播放按钮 */
:deep(.dplayer-bezel-icon.dplayer-bezel-transition) {
  display: none !important;
}

.blur {
  background-blend-mode: multiply;
  backdrop-filter: blur(4px);
  position: fixed;
  top: 0;
  left: 0;
  width: 470px;
  height: 100%;
}

.light {
  .blur {
    background-image: linear-gradient(90deg, rgba(100, 100, 100, 0.9), rgba(100, 100, 100, 0.6) 30%, transparent)
  }
}

.dark {
  .blur {
    background-image: linear-gradient(90deg, rgba(0, 0, 0, .9), rgba(0, 0, 0, .6) 30%, transparent);
  }
}

.play {
  z-index: 999;
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.voice {
  z-index: 999;
  position: fixed;
  bottom: 100px;
  right: 30px;
  width: 50px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  cursor: pointer;
}
</style>
