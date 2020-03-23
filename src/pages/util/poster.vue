<!--
 * @Author: seekwe
 * @Date: 2020-03-09 10:00:17
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:18:54
 -->
<template>
	<view class="page" :style="{'background-color':corol}">
		<image @longpress="longpress" class="image" style="width:750rpx" mode="widthFix" :src="posterUrl" />
		<zPoster ref="poster" @save="save" @fail="fail" @success="success" :config="config" />
	</view>
</template>

<script>
import qr from '@/static/qr.jpg';
import zPoster from '@/components/util/zPoster/index.vue';
const corol = '#f7b52c';
const marginSize = 94;
const paddingSize = 20;
let [width, height] = [0, 0];
let headeTop = 0;
export default {
	components: { zPoster },
	data() {
		return {
			corol: corol,
			config: {},
			items: [],
			posterUrl: ''
		};
	},
	mounted() {
		this.config = this.getPoster(qr);
		this.$log('this.config', this.config);
	},
	methods: {
		longpress() {
			this.$refs['poster'].save();
		},
		save(e) {
			this.$log(e);
		},
		fail(err) {
			console.log('fail', err);
		},
		success(e) {
			this.posterUrl = e;
			console.log('success');
		},
		getBaseConfig() {
			width = this.$to.px2rpx(this.$systems.windowWidth);
			height = this.$to.px2rpx(this.$systems.windowHeight);
			console.log(width, height);

			let config = {
				width: width,
				backgroundColor: '#f7b52c',
				height: height,
				views: []
			};
			headeTop = marginSize;
			config.views.push({
				type: 'rect',
				x: marginSize,
				y: headeTop,
				background: '#fff',
				width: width - marginSize * 2,
				height: height - marginSize * 2,
				radius: 20
			});
			config.views.push({
				type: 'img',
				src:
					'http://thirdwx.qlogo.cn/mmopen/vi_32/89Q1NzeOSf882QuupOW8LsfVfLnJh9bSkmv55CDzwyygRhAkMtNxibfRVQfic5mLdljhcn8Y4dVDqA3jYQa9DBdQ/132',
				width: 130,
				height: 130,
				y: (headeTop = headeTop + 20),
				x: '+' + paddingSize,
				radius: '100%'
			});
			config.views.push({
				type: 'text',
				x: '+20',
				y: (headeTop += 36),
				positionX: true,
				text: '我叫 NNTX 的啦啦啦啦啦',
				color: '#000',
				width: 460,
				fontSize: 36,
				maxLine: 1
			});
			config.views.push({
				type: 'text',
				x: marginSize + paddingSize,
				y: (headeTop += 110),
				text:
					'我是一句祝福的文案，运营请告诉我写什么！这里是待定啦！！！这里是待定啦！！',
				color: '#000',
				width: width - marginSize * 2 - paddingSize * 2,
				fontSize: 30,
				maxLine: 2
			});
			headeTop += 100;
			return config;
		},
		resetConfig(config) {
			config.views[0].height = headeTop;
			return config;
		},
		getPoster(qr) {
			let config = this.getBaseConfig();
			let [imageWidth, imageHeight] = [150, 150];

			let w = width - marginSize * 2 - paddingSize * 2;
			let h = imageHeight * (w / imageWidth);

			config.views.push({
				type: 'img',
				src:
					'http://thirdwx.qlogo.cn/mmopen/vi_32/89Q1NzeOSf882QuupOW8LsfVfLnJh9bSkmv55CDzwyygRhAkMtNxibfRVQfic5mLdljhcn8Y4dVDqA3jYQa9DBdQ/132',
				width: w,
				height: h,
				y: headeTop,
				x: marginSize + paddingSize
			});

			let [qrW, qrH] = [160, 160];
			config.views.push({
				type: 'img',
				src: qr,
				width: qrW,
				height: qrH,
				y: headeTop + h + 100,
				x: width / 2 - qrW / 2
			});
			headeTop += h - marginSize + paddingSize;

			return this.resetConfig(config);
		}
	}
};
</script>

<style>
@import 'poster.less';
</style>