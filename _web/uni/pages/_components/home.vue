<template>
	<view class="main">
		<view class="content">
			<swiper class="swiper" :current="current" vertical @change="swiperChange">
				<swiper-item v-for="(item,index) in datas" :key="index">
					<view class="media-content">
						<swiper class="swiper" :current="imageIndex" circular
							style="height: 100%;box-sizing: border-box;"
							:style="{'padding-bottom':item.hotel?'200rpx':'0'}" @change="swiperMediaChange">
							<swiper-item v-for="(media, index) in item.medias" :key="index"
								style="height: 100%;width: 100%;">
								<image style="height: 100%;width: 100%;" :src="media.url" v-if="media.type === 1">
								</image>
								<video style="height: 100%;width: 100%;" :src="media.url"
									v-if="media.type === 2"></video>
							</swiper-item>
						</swiper>
						<view class="bottom">
							<view style="position: absolute;right: 30rpx;top: -260rpx;font-size: 24rpx;">
								<view style="text-align: center;">
									<uv-avatar src="https://via.placeholder.com/200x200.png/2878ff"
										size="36"></uv-avatar>
									<view>试试</view>
								</view>
								<view style="height: 20rpx;"></view>
								<view style="text-align: center;">
									<uv-avatar icon="star-fill" size="36"></uv-avatar>
									<view>20</view>
								</view>
							</view>
							<view class="bottom-content">
								<view style="display: flex;align-items: center;">
									<view style="flex: 1;">
										<view class="title">
											{{item.title}}
										</view>
										<view class="tag"
											style="display: flex;align-items: center;color: #3fabff;margin: 10rpx 0;">
											<view v-for="(t,index) in item.tags" :key="key" style="font-size: 22rpx;">
												{{t}}<text v-if="index<item.tags.length-1">·</text>
											</view>
										</view>
									</view>
								</view>
								<view class="indicator">
									<view class="indicator-item" :class="{'active':imageIndex === index}"
										v-for="(item,index) in item.medias" @tap="imageIndex = index"></view>
								</view>
							</view>
							<view v-if="item.hotel" style="background-color: #000;padding: 0 40rpx;">
								<view>
									<view style="margin-bottom: 10rpx;font-size: 40rpx;">
										{{item.hotel.name}}
									</view>
									<view style="display: flex;align-items: center;margin-bottom: 10rpx">
										<uv-icon color='rgba(255,255,255,0.7)' size='24rpx'
											name="empty-address"></uv-icon>
										<view style="color:rgba(255,255,255,0.7);font-size: 24rpx;">{{item.hotel.addr}}
										</view>
										<uv-icon color='rgba(255,255,255,0.7)' size='24rpx'
											name="arrow-right"></uv-icon>
									</view>
									<view style="display: flex;align-items: center;">
										<view style="font-size: 50rpx;color: #f1ca5d;margin-right: 10rpx;">
											{{item.hotel.start}}
										</view>
										<view style="width: 0;flex: 1">
											<uv-rate :readonly="true" :allowHalf="true" size='24rpx' :count="5"
												v-model="item.hotel.start" activeColor="#f1ca5d"></uv-rate>
											<view
												style="font-size: 22rpx;color: rgba(255,255,255,0.7);margin-left: 6rpx;;">
												999+评论>
											</view>
										</view>
										<view>
											<uv-button :custom-style="{
												background: '#f1d152',
												color: '#000',
											}" type="primary" color="#f1d152" size="small" shape="circle" text="查看酒店"></uv-button>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
				</swiper-item>
			</swiper>
		</view>
		<view class="top-bar">
			<view class="top-bar-item" v-for="item in bars" :class="{'active':activeBar===item.key}"
				@tap="activeBar=item.key" :key="item.key">
				{{item.title}}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'home',
		data() {
			return {
				current: 0,
				activeBar: 2,
				imageIndex: 0,
				bars: [{
					key: 0,
					title: '酒店/民宿'
				}, {
					key: 1,
					title: '攻略'
				}, {
					key: 2,
					title: '景点'
				}],
				datas: [{
					title: '昨夜星辰昨夜风，画楼西畔桂堂东',
					tags: ['网红打卡点', '绝了', '酒店推荐', '美不胜收'],
					hotel: {
						name: '北海大酒店',
						start: 4.5,
						addr: '北海市海城区银滩路步行街231号'
					},
					medias: [{
						type: 2,
						url: 'https://cdn.uviewui.com/uview/resources/video.mp4',
						title: '昨夜星辰昨夜风，画楼西畔桂堂东',
						poster: 'https://gd-hbimg.huaban.com/4147fc6a4e8d197229f634274c79947ad50169731563c6-rMBPgr_fw658webp'
					}, {
						type: 1,
						url: 'https://q7.itc.cn/q_70/images03/20240506/06d5954320e247ef99d7bac9530ba7be.jpeg',
						title: '身无彩凤双飞翼，心有灵犀一点通'
					}, {
						type: 1,
						url: 'https://img0.baidu.com/it/u=1745064536,1448839045&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=749',
						title: '谁念西风独自凉，萧萧黄叶闭疏窗，沉思往事立残阳'
					}]
				}, {
					title: '昨夜星辰昨夜风，画楼西畔桂堂东',
					tags: ['网红打卡点', '绝了', '酒店推荐', '美不胜收'],
					hotel: {
						name: '北海大酒店',
						start: 4.5,
						addr: '北海市海城区银滩路步行街231号'
					},
					medias: [{
						type: 1,
						url: 'https://q7.itc.cn/q_70/images03/20240506/06d5954320e247ef99d7bac9530ba7be.jpeg',
						title: '身无彩凤双飞翼，心有灵犀一点通'
					}, {
						type: 1,
						url: 'https://img0.baidu.com/it/u=1745064536,1448839045&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=749',
						title: '谁念西风独自凉，萧萧黄叶闭疏窗，沉思往事立残阳'
					}]
				}]
			}
		},
		methods: {
			swiperChange(e) {
				const newIndex = e.detail.current;
				this.current = newIndex;
				this.imageIndex = 0
			},
			swiperMediaChange(e) {
				this.imageIndex = e.detail.current;
			},
		}
	}
</script>

<style scoped>
	.main {
		position: relative;
		width: 100%;
		height: 100%;
		color: #fff;

		.content {
			width: 100%;
			height: 100%;
			position: relative;

			.swiper {
				width: 100%;
				height: 100%;
				position: relative;

				.media-content {
					width: 100%;
					height: 100%;
					position: relative;

					.bottom {
						width: 100%;
						position: absolute;
						bottom: 0;
						border-top-right-radius: 40rpx;
						border-top-left-radius: 40rpx;
						box-sizing: border-box;

						.indicator {
							display: flex;
							width: 100%;
							box-sizing: border-box;

							.indicator-item.active {
								background-color: rgba(200, 200, 200, 0.9);
							}

							.indicator-item {
								flex: 1;
								margin: 10rpx;
								height: 4rpx;
								background-color: rgba(200, 200, 200, 0.5);
								border-radius: 10rpx;
							}
						}

						.bottom-content {
							background: linear-gradient(to bottom, rgba(0, 0, 0, 0), rgba(0, 0, 0, 1));
							border-top-right-radius: 40rpx;
							border-top-left-radius: 40rpx;
							backdrop-filter: blur(6rpx);
							-webkit-backdrop-filter: blur(6rpx);
							padding: 30rpx 40rpx;
							padding-bottom: 10rpx;
							color: #fff;
						}
					}
				}

			}
		}

		.top-bar {
			color: rgba(255, 255, 255, 0.6);
			position: absolute;
			top: 0;
			box-sizing: border-box;
			padding: 20rpx 40rpx;
			padding-bottom: 80rpx;
			width: 100%;
			text-align: right;
			display: flex;
			align-items: center;
			justify-content: end;
			background: linear-gradient(to bottom, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.1), transparent);


			.top-bar-item.active {
				color: #fff;
			}

			.top-bar-item {
				margin: 10rpx 20rpx;
			}
		}
	}
</style>