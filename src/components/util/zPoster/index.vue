<template>
	<canvas canvas-id="app" :style="canvasStyle"></canvas>
</template>

<script>
import Poster from './util';
import { $to } from '@/common/util';
const canvasId = 'app';
let poster, path, ctx;
export default {
	props: {
		config: {
			type: Object,
			default() {
				return {};
			}
		},
		show: {
			type: Boolean,
			default: false
		},
		scale: {
			type: Number,
			default: 1
		},
		systems: {
			type: Object
		}
	},
	created() {
		ctx = uni.createCanvasContext(canvasId, this);
	},
	computed: {
		canvasStyle() {
			return (
				`width:${this.width}rpx;height:${this.height}rpx;` +
				(this.show
					? ``
					: `position: absolute;left: -99999999px;top: -999999px;`)
			);
		},
		sysInfo() {
			return this.systems || uni.getSystemInfoSync();
		}
	},
	watch: {
		// config: {
		// 	immediate: true,
		// 	deep: true,
		// 	handler(config, oldConfig) {
		// 		this.watchCreate();
		// 	}
		// }
	},
	data() {
		return {
			width: 0,
			height: 0
		};
	},
	methods: {
		save() {
			uni.saveImageToPhotosAlbum({
				filePath: path,
				complete: e => {
					this.$emit('save', e);
				}
			});
		},
		clear() {
			poster.clear();
		},
		watchCreate() {
			if (JSON.stringify(this.config) === '{}') {
				return;
			}
			this.create();
		},
		create() {
			path = '';
			this.width = this.config.width || $to.px2rpx(this.sysInfo.screenWidth);
			this.height = this.config.height || $to.px2rpx(this.sysInfo.screenHeight);
			poster = new Poster(
				{
					width: this.width,
					height: this.height,
					scale: this.scale,
					canvasId: canvasId,
					backgroundColor: this.config.backgroundColor,
					pixelRatio: this.sysInfo.pixelRatio,
					ctx: ctx
				},
				this
			);
			console.log('ctx: ', ctx);
			return poster
				.draw(this.config.views || [])
				.then(tmpPath => {
					path = tmpPath;
					this.$emit('result', { path: tmpPath }, true);
				})
				.catch(err => {
					this.$emit('result', { errMsg: err }, false);
				});
		}
	}
};
</script>

<style>
</style>