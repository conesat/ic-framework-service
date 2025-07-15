<template>
	<view class="main">
		<view style="width: 100%;height: 100%;position: absolute;left: 0;top: 0;">
			<video object-fit="cover" :controls="false" :show-play-btn="false" :muted="true" :autoplay="true" :loop="true" style="width: 100%;height: 100%;" src="/static/video.mp4"></video>
		</view>
		<view class="content">
			<image class="logo" src="/static/imgs/long-logo.png"></image>
			<view class="start">
				<uv-button @tap="toIndex" :custom-style="{
					borderRadius: '40rpx'
				}" type="primary" :plain="true" :hairline="true" text="开启旅程"></uv-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {}
		},
		onLoad() {

		},
		methods: {
			toIndex() {
				uni.redirectTo({
					url: '/pages/index/index'
				})
			}
		}
	}
</script>

<style scoped>
	.main {
		height: 100%;
		width: 100%;
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;

		.content {
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;
			position: absolute;
			display: flex;
			flex-direction: column;
			align-items: center;


			.logo {
				width: 300rpx;
				height: 40rpx;
				margin-top: 200rpx;
				margin-left: auto;
				margin-right: auto;
				margin-bottom: 50rpx;
			}

			.start {
				position: fixed;
				bottom: 20rpx;
				width: 200rpx;
				left: calc(50% -100rpx);
			}
		}
	}
</style>