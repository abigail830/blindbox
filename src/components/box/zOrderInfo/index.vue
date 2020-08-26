<!--
 * @Author: seekwe
 * @Date: 2020-03-11 13:39:45
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-20 15:45:00
 -->
<template>
	<view class="order-info-view-box">
		<scroll-view
			class="scrollB"
			:class="{'scrollB_active':isShow==true}"
			@touchmove.stop.prevent="moveHandle"
			scroll-y
		>
			<view class="order-info-view-box-content">
				<view class="box-line">
					<view class="box-info-title">商品名称：</view>
					<view class="box-info-value">{{info.productName}}</view>
				</view>
				<view class="box-line">
					<view class="box-info-title">收货人 ：</view>
					<view class="box-info-value">{{info.receiver}}</view>
				</view>
				<view class="box-line">
					<view class="box-info-title">联系电话：</view>
					<view class="box-info-value">{{info.mobile}}</view>
				</view>
				<view class="box-line box-line-mini">
					<view class="box-info-title">收货地址：</view>
					<view class="box-info-value">{{info.area}} {{info.detailAddress}}</view>
				</view>
				<view class="box-line box-line-mini">
					<view class="box-info-title">物流信息：</view>
					<view
						class="box-info-value"
						@click="copy"
					>{{info.shippingTicket}} - {{info.shippingCompany}}</view>
				</view>
			</view>
		</scroll-view>
		<scroll-view
			:class="{scrollC_active:isShow}"
			class="scrollC"
			@click="closeDraw"
		></scroll-view>
	</view>
</template>

<script>
export default {
	name: 'z-order-info',
	props: {
		isShow: Boolean,
		info: {
			type: Object,
			default() {
				return {};
			}
		}
	},
	methods: {
		closeDraw() {
			this.$emit('close');
		},
		moveHandle() {},
		copy() {
			console.log(this.info);
			uni.setClipboardData({
				data: this.info.shippingCompany,
				success: function(res) {
					uni.getClipboardData({
						success: function(res) {
							uni.showToast({
								title: '复制成功'
							});
						}
					});
				}
			});
		}
	}
};
</script>

<style lang='less'>
@import 'index.less';
</style>
