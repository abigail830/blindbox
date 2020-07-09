<!--
 * @Author: seekwe
 * @Date: 2020-03-17 11:53:51
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-09 12:47:29
 -->
<template>
	<view class="help-view" v-show="showHelp">
		<view class="help-box">
			<scroll-view :scroll-y="true" class="help-center">
				<image v-if="image" class="preview-image" :src="image" mode="widthFix" />
			</scroll-view>
			<view class="close-icon-box" @click="closeSheet">
				<image class="close-image" src="/static/universal-icon.png" mode />
			</view>
			<view>
				<button @click="goBuy" class="go-btn">抽一个</button>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		data: {
			type: Object,
			default() {
				return {};
			}
		}
	},
	computed: {
		image() {
			return this.data.image || '';
		},
		showHelp() {
			return JSON.stringify(this.data) !== '{}';
		}
	},
	methods: {
		closeSheet() {
			this.$emit('close');
		},
		goBuy() {
			this.$emit('buy');
		}
	}
};
</script>

<style lang='less'>
@import '../../../common/var.less';

.help-view;

.help-box {
	position: relative;
	padding: 0;
	height: 720rpx;
	margin-top: 10% !important;
	background: none;
	.help-center {
		height: 720rpx;
	}
	.preview-image {
		width: 558rpx;
	}
	.close-icon-box {
		bottom: -220rpx;
		// bottom: inherit;
		// left: inherit;
		// top: -80rpx;
		// right: -80rpx;
	}
}
.help-view .go-btn {
	margin-top: 20rpx;
	background-color: @themeColor;
	color: #fff;
}
</style>