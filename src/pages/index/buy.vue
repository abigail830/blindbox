<!--
 * @Author: seekwe
 * @Date: 2020-03-17 15:35:11
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 21:03:25
 -->
<template>
	<view class="page page-buy">
		<view class="buy-view">
			<view class="buy-header">产品名称</view>
			<image class="buy-cover" src="/static/demo.png" mode="aspectFill" />
			<view class="buy-code">产品序列号：00000000</view>
			<view class="card-box">
				<view class="card-box-bg">
					<view class="card">
						<image class="card-image" src="/static/cue-card.png" mode="aspectFill" />
						<text class="card-sum">000</text>
					</view>
					<view class="card">
						<image class="card-image" src="/static/graphics-card.png" mode="aspectFill" />
						<text class="card-sum">000</text>
					</view>
					<view class="card">
						<image class="card-image" src="/static/concession-card.png" mode="aspectFill" />
						<text class="card-sum">000</text>
					</view>
				</view>
			</view>
			<view class="buy-btns">
				<button class="buy-btn">换一盒</button>
				<button class="buy-btn">就买它</button>
			</view>
			<view class="tip-view">温馨提示：我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。</view>
		</view>
	</view>
</template>

<script>
export default {};
</script>

<style lang="less">
@import 'buy.less';
</style>