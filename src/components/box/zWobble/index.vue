<!--
 * @Author: seekwe
 * @Date: 2020-03-02 16:50:58
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-23 08:42:14
 -->
<template>
	<view class="z-wobble-box">
		<view :class="imageBoxClass">
			<img
				:class="imageClass"
				mode="aspectFit"
				src="http://res.paquapp.com/boxonline/newbox/398newbox.png"
			/>
		</view>
		<view :class="{'on':boxOn===''}" class="z-wobble-card"></view>
	</view>
</template>

<script>
import { $shake } from '@/common/util';

export default {
	data() {
		return {
			boxOn: null
		};
	},
	computed: {
		imageBoxClass() {
			return this.boxOn === ''
				? `z-wobble-image-box out`
				: `z-wobble-image-box`;
		},
		imageClass() {
			return `z-wobble-image ${this.boxOn}`;
		}
	},
	mounted() {
		$shake(_ => {
			this.wobbleStart();
		});
	},
	methods: {
		wobbleStart() {
			this.boxOn = 'on';
			setTimeout(() => {
				this.wobbleEnd();
			}, 1000);
		},
		wobbleEnd() {
			this.boxOn = '';
			// this.boxOn = '';
		}
	}
};
</script>

<style lang="less" scope>
@import 'index.less';
</style>