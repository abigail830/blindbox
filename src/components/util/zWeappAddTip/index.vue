<!--
 * @Author: seekwe
 * @Date: 2020-03-09 18:28:09
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:17:28
 -->
<template>
	<view>
		<view class="z-add-tips-box" v-if="showTip">
			<view class="z-add-tips-content" @tap="hideTip">
				<text>{{tip}}</text>
			</view>
		</view>
	</view>
</template>

<script>
const SHOW_TIP = 'SHOW_TIP';
export default {
	data() {
		return {
			showTip: false
		};
	},
	mounted() {
		this.showTip = !uni.getStorageInfoSync().keys.includes(SHOW_TIP);
		setTimeout(_ => {
			this.hideTip();
		}, (this.duration > 0 ? this.duration : 9999999) * 1000);
	},
	props: {
		tip: {
			type: String,
			default: '点击「添加小程序」，下次访问更便捷'
		},
		duration: {
			type: Number,
			default: 5,
			required: false
		}
	},
	methods: {
		hideTip() {
			uni.setStorageSync(SHOW_TIP, true);
			this.showTip = false;
		}
	}
};
</script>

<style lang="less" scoped>
@themeColor: #34b5e2;
@import '../../../common/var.less';
.z-add-tips-box {
	position: fixed;
	top: 0;
	right: 0;
	z-index: 99999;
	opacity: 0.8;
	display: flex;
	justify-content: flex-end;
	align-items: flex-end;
	flex-direction: column;
	width: 600rpx;
	animation: opacityC 1s linear infinite;
}
.z-add-tips-content::before {
	content: '';
	position: absolute;
	width: 0;
	height: 0;
	top: -36rpx;
	right: 105rpx;
	border-width: 20rpx;
	border-style: solid;
	display: block;
	border-color: transparent transparent @themeColor transparent;
}
.z-add-tips-content {
	border-width: 0rpx;
	margin-top: 20rpx;
	position: relative;
	background-color: @themeColor;
	box-shadow: 0 10rpx 20rpx -10rpx @themeColor;
	border-radius: 12rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 18rpx 20rpx;
	margin-right: 40rpx;
}
.z-add-tips-content > text {
	color: #fff;
	font-size: 28rpx;
	font-weight: 400;
}
@keyframes opacityC {
	0% {
		opacity: 0.8;
	}
	50% {
		opacity: 1;
	}
}
</style>
