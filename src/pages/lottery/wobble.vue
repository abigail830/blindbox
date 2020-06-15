<!--
 * @Author: seekwe
 * @Date: 2020-03-02 16:48:38
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-03 17:30:31
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
import { posterCopywriting } from '@/config';
export default {
	components: { zWobble, zPoster },
	data() {
		return {
			posterImage: '',
			posterConfig: {},
			state: false,
			title: '',
			// boxImage: 'http://res.paquapp.com/boxonline/newbox/398newbox.png',
			boxImage: '',
			// 	'https://blindbox.fancier.store/images/series/6c86ded6-1424-402f-a99e-2d49ee72d8ce-boxImage.png',
			image: ''
		};
	},
	mounted() {
		// if (process.env.NODE_ENV === 'development') {
		// 	this.$refs.wobble.wobbleStart();
		// }
		// this.poster();
	},
	beforeDestroy(){
		show = false;
	},
	onLoad(opt) {
		this.boxImage = opt.img;
		this.getDrawProduct(opt.id);
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
		getDrawProduct(id) {
			this.$api(_ => {
				return ['order.drawProduct', id];
			})
				.then(e => {
					if (e.status === 404) {
						if (!show) return;
						// 继续请求
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
			let padding = 94;

			width = 750;
			height = 1008;

			let views = [];
			views.push({
				type: 'rect',
				x: padding,
				y: padding,
				radius: 20,
				width: width - padding * 2,
				height: height - padding * 2,
				background: '#fff'
			});
			views.push({
				type: 'img',
				src: this.avatarUrl,
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
			views.push({
				type: 'img',
				src: this.image,
				width: 314,
				height: 397,
				positionY: true,
				x: (750 - 314) / 2,
				y: '+40'
			});
			config = {
				width: width,
				backgroundColor: '#f7b52c',
				backgroundColor: '#000',
				backgroundImage: '/static/p-bg.jpg',
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
			this.$nextTick(this.$refs['poster'].create);
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