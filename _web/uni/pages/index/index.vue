<template>
	<view class="home">
		<view class="content">
			<home v-if="activeTab === 0"></home>
		</view>
		<view class="tab">
			<view class="tab-item" :class="{'active':activeTab===0}" @tap="activeTab=0">
				闲逛
			</view>
			<view class="tab-item" :class="{'active':activeTab===1}" @tap="activeTab=1">
				购票
			</view>
			<view class="tab-item" :class="{'active':activeTab===2}" @tap="activeTab=2">
				<view class="plus">
					<uv-icon name="plus" color="#fff" size="14"></uv-icon>
				</view>
			</view>
			<view class="tab-item" :class="{'active':activeTab===3}" @tap="activeTab=3">
				消息
			</view>
			<view class="tab-item" :class="{'active':activeTab===4}" @tap="activeTab=4">
				我的
			</view>
		</view>
	</view>
</template>

<script>
	import home from '../_components/home.vue'
	export default {
		data() {
			return {
				activeTab: 0
			}
		},
		components: {
			home
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style scoped lang="less">
	uni-page-body {
		height: 100%;
		width: 100%;
	}

	.home {
		flex-direction: column;
		display: flex;
		position: relative;
		background-color: #000;
		height: 100%;
		width: 100%;

		.content {
			flex: 1;
			height: 0;
			width: 100%;
		}

		.tab {
			box-sizing: border-box;
			padding: 30rpx;
			align-items: center;
			color: #fff;
			display: flex;
			width: 100%;

			.tab-item.active {
				color: #fff;
			}

			.tab-item {
				color: #999;
				text-align: center;
				flex: 1;

				.plus {
					display: inline-block;
					border-radius: 10rpx;
					border: #fff 1rpx solid;
					padding: 8rpx 18rpx;
				}
			}

		}
	}
</style>