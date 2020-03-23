<!--
 * @Author: seekwe
 * @Date: 2020-03-11 13:39:45
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:16:31
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
					<view class="box-info-title">商品名称</view>
					<view class="box-info-value">商品{{info.title}}名称</view>
				</view>
				<view class="box-line">
					<view class="box-info-title">商品名称: {{info.title}}</view>
				</view>
				<view class="box-line">
					<view class="box-info-title">商品名称: {{info.title}}</view>
				</view>
				<view class="box-line">
					<view class="box-info-title">商品名称: {{info.title}}</view>
				</view>
			</view>
		</scroll-view>
		<scroll-view :class="{scrollC_active:isShow}" class="scrollC" @click="closeDraw"></scroll-view>
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
		moveHandle() {}
	}
};
</script>

<style lang='less'>
@import 'index.less';
</style>
