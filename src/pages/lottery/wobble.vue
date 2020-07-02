<!--
 * @Author: seekwe
 * @Date: 2020-03-02 16:48:38
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-02 13:18:40
 -->
<template>
	<view class="page wobble-view">
		<view
			v-if="!state"
			class="wobble-tip"
		>摇晃手机即可开盒</view>
		<zWobble
			:boxImage="boxImage"
			:name="name"
			:image="image"
			:music="shakeMusic"
			@end="wobbleEnd"
			ref="wobble"
		/>
		<view v-if="!state">
			<button
				class="wobble-btn"
				@click="wobbleStart"
			>开 盒</button>
		</view>
		<view v-else>
			<button
				class="wobble-btn"
				@click="reStartGame"
			>再抽一个</button>
			<button
				class="wobble-btn"
				@click="poster"
			>生成海报</button>
		</view>
		<zPoster
			ref="poster"
			@result="posterResult"
			:config="posterConfig"
			:show="false"
		/>
		<!-- <image :src="posterImage" mode="widthFix" class="demo" /> -->
	</view>
</template>

<script>
let show = true;
import { $shake } from '@/common/util';
import { mapState, mapGetters } from 'vuex';
import zPoster from '@/components/util/zPoster';
import zWobble from '@/components/box/zWobble';
import { posterCopywriting, shakeMusic, posterBottomHeight } from '@/config';
console.log('shakeMusic', shakeMusic);

export default {
	components: { zWobble, zPoster },
	data() {
		return {
			posterImage: '',
			posterConfig: {},
			state: false,
			shakeMusic: shakeMusic,
			title: '',
			// boxImage: 'http://res.paquapp.com/boxonline/newbox/398newbox.png',
			boxImage: '',
			// 	'https://blindbox.fancier.store/images/series/6c86ded6-1424-402f-a99e-2d49ee72d8ce-boxImage.png',
			image: '',
			posterBgImage: '/static/p-bg.jpg'
		};
	},
	mounted() {
		if (process.env.NODE_ENV === 'development') {
			this.$refs.wobble.wobbleStart();
			this.title = '超级用户';
			this.image =
				'https://blindbox.fancier.store/images/product/98f6f9f7-60a2-452b-a265-44695ad5d755gray.png';
			setTimeout(() => {
				this.poster();
			});
		}
		// this.poster();
	},
	beforeDestroy() {
		show = false;
	},
	onLoad(opt) {
		this.boxImage = opt.img;
		this.getDrawProduct(opt.id);
		this.getPosterBgImage(opt.sid);
	},
	computed: {
		nickName() {
			return this.userInfo.nickName || '';
		},
		avatarUrl() {
			return this.userInfo.avatarUrl || '';
		},
		...mapGetters(['userInfo'])
	},
	methods: {
		async getPosterBgImage(id) {
			// let posterBgImage = '';
			this.$log('需要单独去获取海报背景');
			let childSeries = await this.$api(_ => {
				return ['products.childSeries', id];
			});
			this.$log('需要单独去获取海报背景', childSeries);
			this.posterBgImage = this.$websiteUrl + childSeries.posterBgImage;
		},
		getDrawProduct(id) {
			this.$api(_ => {
				return ['order.drawProduct', id];
			})
				.then(e => {
					if (e.status === 404) {
						if (!show) return;
						// 继续请求
						if (process.env.NODE_ENV !== 'development')
							setTimeout(_ => {
								this.getDrawProduct(id);
							}, 500);
					} else {
						this.title = e.name;
						this.image = this.$websiteUrl + e.productImage;
						// this.boxImage = e.productImage;
					}
					console.warn(e);
				})
				.catch(e => {
					console.warn(e);
				});
		},
		getPosterConfig() {
			let config = {};
			let width = this.$to.px2rpx(this.$systems.windowWidth);
			let height = this.$to.px2rpx(this.$systems.windowHeight);
			let padding = 63;

			width = 750;
			height = 1008;
			height = 1600;

			let views = [];
			views.push({
				type: 'rect',
				x: padding,
				y: padding,
				radius: 20,
				width: width - padding * 2,
				height: 1238,
				// height: height - padding * 2,
				background: '#fff'
			});
			views.push({
				type: 'img',
				src: this.avatarUrl || this.userInfo.avatarUrl,
				width: 130,
				height: 130,
				x: '+40',
				y: '+40',
				radius: '100%'
			});
			views.push({
				type: 'text',
				x: '+40',
				positionX: true,
				y: '+40',
				text: this.nickName,
				color: '#000',
				width: 310,
				fontSize: 36,
				maxLine: 1
			});
			views.push({
				type: 'text',
				x: padding + 40,
				y: 280,
				text: posterCopywriting,
				color: '#000',
				width: 480,
				fontSize: 36,
				height: 100,
				maxLine: 2
			});
			if (this.image)
				views.push({
					type: 'img',
					src: this.image,
					width: 314*2,
					height: 397*2,
					positionY: true,
					x: (750 - 314*2) / 2,
					y: '+40'
				});
			views.push({
				type: 'text',
				x: padding,
				y: '+14',
				text: this.title,
				color: '#000',
				center: true,
				positionY: true,
				width: width - padding * 2,
				fontSize: 32,
				height: 100,
				maxLine: 1
			});
			config = {
				width: width,
				backgroundColor: '#f7b52c',
				backgroundColor: '#000',
				backgroundImage: this.posterBgImage,
				height: height,
				now: +new Date(),
				views: views
			};
			this.posterConfig = config;
		},
		getPosterViewsConfig() {},
		poster() {
			this.$loading('海报生成中...');
			this.getPosterConfig();
			this.getPosterViewsConfig();
			this.$refs['poster'].clear();
			// this.posterConfig.height = this.posterConfig.height + posterBottomHeight;
			this.$nextTick(() => {
				this.$refs['poster'].create();
				// this.posterConfig.height =
					// this.posterConfig.height - posterBottomHeight;
			});
			this.$log('createPoster', this.posterImage);
		},
		posterResult(e, state) {
			if (state) {
				this.posterImage = e.path;
				this.$log('保存海报', this.posterImage);
				this.$refs.poster.save();
				this.$nextTick(_ => {
					this.$loading()();
				});
				uni.previewImage({
					urls: [this.posterImage],
					longPressActions: {}
				});
			}
		},
		reStartGame() {
			this.$back();
			// this.$go('index/info');
		},
		wobbleEnd() {
			this.state = true;
		},
		wobbleStart() {
			this.$refs.wobble.wobbleStart();
		}
	}
};
</script>

<style lang="less">
@import '../../common/var.less';

.page {
	background-color: #fff;
}
.wobble-view {
	padding: 160rpx 60rpx;
	position: relative;
}
.wobble-btn {
	background: @themeColor;
	color: #fff;
	font-size: 36rpx;
	margin-top: 50rpx;
}
.wobble-tip {
	font-size: 26rpx;
	text-align: center;
	color: @lightGreyColor;
	position: absolute;
	margin: auto;
	width: 100%;
	top: 60rpx;
	left: 0;
}
</style>