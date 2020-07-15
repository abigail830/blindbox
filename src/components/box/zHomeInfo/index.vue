<!--
 * @Author: seekwe
 * @Date: 2020-03-17 11:53:51
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-14 11:24:27
 -->
<template>
	<view class="z-home-info-box">
		<view
			class="sheet-view"
			:class="{'sheet-show':isShowBottom,'sheet-hide':!isShowBottom}"
			@touchmove.stop.prevent
			@click="closeSheet"
		>
			<scroll-view
				scroll-y="true"
				class="sheet-box"
				:class="{'sheet-box_on':isShowBottom}"
				@click.stop
			>
				<image v-for="(v,k) in images" :key="k" class="content-image" :src="v" mode="widthFix" />
			</scroll-view>
			<view class="close-icon-box" @click="closeSheet">
				<image class="close-image" src="/static/universal-icon.png" mode="" />
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		images: {
			type: Array,
			default() {
				return [];
			}
		},
		isShowBottom: {
			type: Boolean,
			default: true
		}
	},
	methods: {
		closeSheet() {
			this.$emit('close');
			this.$log('closeSheet');
		}
	}
};
</script>

<style lang='less'>
@import '../zHomeAll/index.less';
.sheet-box {
}
.z-home-info-box {
	.content-image {
		width: 100%;
		display: block;
	}
	.sheet-box_on {
		height: calc(100vh - 150rpx);
		border-radius: 0;
		background-color: rgba(255, 254, 254, 0.815);
	}
	.close-icon();
}
</style>