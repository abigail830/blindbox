<!--
 * @Author: seekwe
 * @Date: 2020-03-17 15:35:11
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-27 16:03:19
 -->
<template>
	<view class="page page-buy">
		<view class="buy-view">
			<view class="buy-header">
				<text :class="{'delete_state':tipName}">{{title}}</text>
				<view>
					<text
						v-if="tipState"
						class="card-state"
					>提示卡</text>
					<text
						v-if="showState"
						class="card-state"
					>显示卡</text>
					<text
						v-if="discountState"
						class="card-state"
					>优惠卡</text>
				</view>
			</view>
			<image
				class="buy-cover"
				:src="image"
				mode="aspectFill"
			/>
			<view class="buy-code">产品序列号：{{id}}</view>
			<view class="card-box">
				<view class="card-box-bg">
					<view
						class="card"
						@click="selectTipCard"
					>
						<image
							:class="{'grey':tipState}"
							class="card-image"
							src="/static/cue-card.png"
							mode="aspectFill"
						/>
						<text class="card-sum">{{series.tipsCouponCost}}</text>
					</view>
					<view
						class="card"
						:class="{'grey':showState}"
						@click="selectShowCard"
					>
						<image
							class="card-image"
							src="/static/graphics-card.png"
							mode="aspectFill"
						/>
						<text class="card-sum">{{series.displayCouponCost}}</text>
					</view>
					<view
						class="card"
						:class="{'grey':discountState}"
						@click="selectDiscountCard"
					>
						<image
							class="card-image"
							src="/static/concession-card.png"
							mode="aspectFill"
						/>
						<text class="card-sum">{{series.discountCouponCost}}</text>
					</view>
				</view>
			</view>
			<view class="buy-btns">
				<button
					class="buy-btn"
					@click="back"
				>换一盒</button>
				<button
					class="buy-btn"
					@click="purchase"
				>就买它</button>
			</view>
			<view class="tip-view">温馨提示：我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。我是一段温馨的提示哦。</view>
		</view>
		<view
			class="help-view"
			v-show="showHelp"
		>
			<view class="help-box">
				<view class="help-title">{{help.title}}</view>
				<scroll-view
					:scroll-y="true"
					class="help-center"
				>
					<zParser :html="help.content" />
				</scroll-view>
				<button
					class="ok-btn"
					@click="submisBtn"
				>确定</button>
				<view
					class="close-icon-box"
					@click="help={}"
				>
					<image
						class="close-image"
						src="/static/universal-icon.png"
						mode
					/>
				</view>
			</view>
		</view>
		<zHomeBuy
			:priceAfterDiscount="priceAfterDiscount"
			:price="price"
			:series="series"
			:isShowBottom="showPurchase"
			@close="closeInfo"
		/>
	</view>
</template>

<script>
import zParser from '@/components/util/zParse';
import zHomeBuy from '@/components/box/zHomeBuy';
import { mapState } from 'vuex';
import { methods } from './buy';
export default {
	components: { zParser, zHomeBuy },
	data() {
		return {
			help: {},
			id: '', // 系列 id
			paymentState: false,
			showPurchase: false,
			series: {},
			product: {},
			priceAfterDiscount: 100,
			discount: {},
			excludedProduct: {}
		};
	},
	computed: {
		tipName() {
			return this.tipState && !this.showState;
		},
		tipState() {
			return JSON.stringify(this.excludedProduct) !== '{}';
		},
		showState() {
			return JSON.stringify(this.product) !== '{}';
		},
		discountState() {
			return +this.priceAfterDiscount !== 100;
		},
		isShow() {
			return JSON.stringify(this.product) !== '{}';
		},
		finalPrice() {
			return (this.price / 100) * this.priceAfterDiscount;
		},
		price() {
			return this.series.price || 99999999999;
		},
		drawId() {
			return this.series.drawId;
		},
		showHelp() {
			return JSON.stringify(this.help) !== '{}';
		},
		title() {
			return (
				(this.tipName
					? this.excludedProduct.excludedProductName
					: this.showState
					? this.product.productName
					: this.series.seriesName) || '--'
			);
		},
		image() {
			const img = this.isShow
				? this.product.productImage
				: this.series.boxImage;
			this.$log('产品图片', img);

			return !!img ? this.$websiteUrl + img : '';
		}
		// ...mapState('current', {
		// 	series: state => state.series
		// })
	},
	onLoad(o) {
		this.$log(o, 'current series', JSON.stringify(this.series));
		if (o.id) {
			this.id = o.id;
			this.selectId();
			this.info();
		} else {
			this.$log('没有 id 是不是回到其他页面');
		}
	},
	beforeDestroy() {
		if (!this.paymentState) {
			this.$log('没有发起支付关闭了页面,需要告诉后端放弃该盒子');
			this.cancelDraw();
		} else {
			this.$log('发起支付关闭了页面');
		}
	},
	methods: Object.assign(
		{
			info() {
				this.$api(_ => ['buy.info', this.drawId]);
			},
			async selectId() {
				const done = this.$loading();
				try {
					let res = await this.$api(_ => {
						return ['buy.get', this.id];
					});

					this.series = res;
				} catch (e) {
					console.error('err', e);
					this.series = {};
					this.$back();
				}
				done();
				this.$log('selectId', this.series);
			},
			cancelDraw() {
				this.$api(_ => ['buy.cancel', this.drawId]);
			},
			closeInfo(payment = false) {
				this.showPurchase = false;
				if (payment) {
					//discount
					this.paymentState = true;
					this.purchasePost();
				}
			},
			purchase() {
				this.showPurchase = true;
			},
			back() {
				this.$back();
			},
			purchasePost(discount = false) {
				const done = this.$loading('支付中');
				this.$api(_ => {
					return ['order.pay', this.drawId];
				})
					.then(e => {
						e.success = res => {
							// this.$back();
							this.ok();
						};
						e.fail = res => {
							if (process.env.NODE_ENV === 'development') {
								this.ok();
							}
							console.error(res);
						};
						e.signType = 'MD5';
						e.package = 'prepay_id=' + e.prepayId;
						if (!e.timeStamp) {
							e.timeStamp = e.preOrderTime;
						}
						uni.requestPayment(e);
					})
					.finally(_ => {
						done();
					});
			},
			ok() {
				this.$go(
					'lottery/wobble?id=' +
						this.drawId +
						'&img=' +
						this.$websiteUrl +
						this.series.boxImage,
					true
				);
			}
		},
		methods()
	)
};
</script>

<style lang="less">
@import 'buy.less';
</style>