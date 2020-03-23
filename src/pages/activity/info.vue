<template>
	<view class="page page-info">
		<view>ID: {{id}}</view>
		<form report-submit @submit="formSubmit">
			<button formType="submit" class="page-info-btn">活动开始提醒我</button>
		</form>
	</view>
</template>

<script>
export default {
	data() {
		return {
			id: 0
		};
	},
	onLoad(opt) {
		this.$log(opt);
		this.id = opt.id;
	},
	methods: {
		formSubmit({ detail }) {
			this.$log('formId:', detail.formId);
			this.$back();
		}
	}
};
</script>

<style lang="less">
@import '../../common/var.less';

.page-info-btn {
	background-color: @themeColor;
	color: #fff;
	width: 600rpx;
	height: 82rpx;
	position: absolute;
	margin: auto;
	left: 0;
	right: 0;
	font-size: 35.6rpx;
	line-height: 82rpx;
	bottom: 10vh;
	border-radius: 50rpx;
}
</style>