<!--
 * @Author: seekwe
 * @Date: 2020-03-17 11:53:51
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-11 15:23:12
 -->
<template>
	<view class="z-home-buy-box">
		<view
			class="sheet-view"
			:class="{'sheet-show':isShowBottom,'sheet-hide':!isShowBottom}"
			@touchmove.stop.prevent
			@click="closeSheet()"
		>
			<scroll-view
				scroll-y="true"
				class="sheet-box"
				:class="{'sheet-box_on':isShowBottom}"
				@click.stop
			>
				<view class="buy-header">
					<text class="buy-header-title">订单确定</text>全场满3个包邮(0元商品除外),不足3个将收取一定运费!
				</view>
				<div class="buy-main-box">
					<view class="title">{{series.name}}</view>
					<view class="price-box">
						<text class="price-box-text">
							价格：
							<text class="price-box-value">￥{{price}}</text>
						</text>

						<text class="price-box-text">
							数量：
							<text class="price-box-value">1</text>
						</text>
						<text class="price-box-text" v-if="priceAfterDiscount<100">
							折扣：
							<text class="price-box-value">{{priceAfterDiscount/10}}折</text>
						</text>
					</view>
					<view class="buy-tip">
						盲盒类商品拆盒后（含在线拆盒）不支持
						无理由退货换货
					</view>
					<view class="statistics">小计：￥{{finalPrice}}</view>
					<button class="buy-btn" @click="closeSheet(true)">去支付 ￥{{finalPrice}}</button>
				</div>
			</scroll-view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		price: {
			type: Number,
			default: 999999999
		},
		priceAfterDiscount: {
			type: Number,
			default: 999999999
		},
		series: {
			type: Object,
			default() {
				return {};
			}
		},
		isShowBottom: {
			type: Boolean,
			default: true
		}
	},
	computed: {
		finalPrice() {
			return (this.price / 100) * this.priceAfterDiscount;
		}
	},
	methods: {
		payment() {
			this.$emit('payment');
		},
		closeSheet(payment = false) {
			this.$emit('close', payment);
		}
	}
};
</script>

<style lang='less'>
@import '../zHomeAll/index.less';

.z-home-buy-box {
	.sheet-box {
		padding: 28rpx;
		box-sizing: border-box;
	}
	.price-box {
		font-size: 21rpx;
		color: #929292;
		margin-bottom: 30rpx;
		display: flex;
	}
	.price-box-text {
		flex: 1;
	}
	.price-box-value {
		font-size: 30rpx;
		color: #333;
	}
	.buy-main-box {
		width: 560rpx;
		margin: auto;
	}
	.buy-tip {
		font-size: 21rpx;
		text-align: center;
		margin-bottom: 50rpx;
	}
	.buy-btn {
		font-size: 32rpx;
		color: #fff;
		line-height: 90rpx;
		height: 90rpx;
		background-color: @themeColor;
		border-radius: 10rpx;
		box-shadow: 0 0 1rpx #fff;
	}
	.buy-header {
		color: #e8543c;
		font-size: 20.8rpx;
		text-align: right;
		margin-bottom: 46rpx;
	}
	.buy-header-title {
		color: #929292;
		float: left;
	}
	.statistics {
		color: #e8543c;
		font-size: 27.8rpx;
		text-align: right;
		margin-bottom: 46rpx;
		margin-right: 22rpx;
	}
	.title {
		font-size: 32rpx;
		color: @fontColor;
		margin-bottom: 54rpx;
	}
	.sheet-box_on {
		height: 698rpx;
		height: 578rpx;
		border-radius: 15rpx 15rpx 0 0;
		background-color: #ebebeb;
	}
	.close-icon();
}
</style>