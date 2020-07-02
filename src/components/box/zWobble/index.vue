<!--
 * @Author: seekwe
 * @Date: 2020-03-02 16:50:58
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-16 11:35:31
 -->
<template>
	<view class="z-wobble-box">
		<view :class="imageBoxClass">
			<img
				:class="imageClass"
				mode="aspectFit"
				:src="boxImage"
			/>
		</view>
		<view
			:class="{'on':boxOn==='','off':boxOn==='on'}"
			class="z-wobble-card"
		>
			<view class="image-box">
				<image
					class="z-wobble-card-image"
					mode="aspectFit"
					:src="image"
				/>
			</view>
		</view>
	</view>
</template>

<script>
import { $shake } from '@/common/util';
let stop, play;
export default {
	props: {
		image: String,
		boxImage: String,
		music: String
	},
	data() {
		return {
			boxOn: null
		};
	},
	computed: {
		imageBoxClass() {
			return this.boxOn === ''
				? `z-wobble-image-box out`
				: `z-wobble-image-box in`;
		},
		imageClass() {
			return `z-wobble-image ${this.boxOn}`;
		}
	},
	mounted() {
		[stop, play] = $shake(
			_ => {
				this.start();
			},
			500,
			this.music
		);
	},
	methods: {
		wobbleStart() {
			play();
		},
		start() {
			this.$log('开始摇晃盒子');
			this.boxOn = 'on';
			setTimeout(() => {
				this.wobbleEnd();
			}, 1000);
		},
		wobbleEnd() {
			this.boxOn = '';
			this.$emit('end');
		}
	}
};
</script>

<style lang="less" scope>
@import 'index.less';
</style>