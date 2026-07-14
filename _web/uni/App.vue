<script>
export default {
  onLaunch() {
    console.log('Energy swap mini program launch')
  },
  onShow() {},
  onHide() {}
}
</script>

<style>
page,
uni-page-body {
  width: 100%;
  height: 100%;
  min-height: 100%;
  background: #F5F8FC;
  color: #283445;
  font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Helvetica Neue", Arial, sans-serif;
  line-height: 1.4;
  -webkit-font-smoothing: antialiased;
  --gy-text-primary: #243044;
  --gy-text-secondary: #536174;
  --gy-text-muted: #8794A5;
  --gy-blue: #3389F7;
  --gy-card-radius: 27rpx;
  --gy-surface-border: #E8EEF5;
  --gy-card-shadow: 0 2rpx 8rpx rgba(35, 67, 104, .035);
  --gy-shadow-brand: 0 5rpx 14rpx rgba(51, 137, 247, .12);
  --gy-shadow-float: 0 4rpx 12rpx rgba(35, 67, 104, .06);
  --gy-shadow-divider: 0 1rpx 0 rgba(35, 67, 104, .075);
}
/* Flat surface hierarchy: borders and tonal layers replace heavy floating cards. */
.fixed-page-header { box-shadow: var(--gy-shadow-divider) !important; }

view,
text,
input,
button {
  box-sizing: border-box;
}
</style>
