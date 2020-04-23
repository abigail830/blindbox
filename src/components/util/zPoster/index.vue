<template>
	<canvas id="app" canvas-id="app" :style="canvasStyle"></canvas>
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
	created() {},
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
	mounted() {
		ctx = uni.createCanvasContext(canvasId, this);
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
			if (poster) poster.clear();
		},
		watchCreate() {
			if (JSON.stringify(this.config) === '{}') {
				return;
			}
			this.create();
		},
		create() {
			path = '';
			this.$nextTick(_ => {
				let config = JSON.parse(JSON.stringify(this.config));
				this.width = config.width || $to.px2rpx(this.sysInfo.screenWidth);
				this.height = config.height || $to.px2rpx(this.sysInfo.screenHeight);
				// console.warn('this.config', this.config);

				this.$nextTick(_ => {
					poster = new Poster(
						Object.assign({}, config, {
							width: this.width,
							height: this.height,
							scale: this.scale,
							canvasId: canvasId,
							pixelRatio: this.sysInfo.pixelRatio,
							ctx: ctx
						}),
						this
					);
					return poster
						.draw(config.views || [])
						.then(tmpPath => {
							path = tmpPath;
							this.$emit('result', { path: tmpPath }, true);
						})
						.catch(err => {
							this.$emit('result', { errMsg: err }, false);
						});
				});
			});
		}
	}
};
</script>

<style>
</style>