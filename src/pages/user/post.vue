<!--
 * @Author: seekwe
 * @Date: 2020-04-13 17:41:24
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-27 16:10:24
 -->
<template>
	<view class="page page-order page-order-post">
		<view class="page-order-post-box">
			<view
				class="page-order-items"
				v-for="(v,k) in itemsData"
				:key="k"
			>
				<image
					class="page-order-image"
					:src="padImageURL(v.productImage)"
					mode="aspectFill"
				/>
				<view class="page-order-info">
					<view class="title">{{v.productName}}</view>
					<view class="time">{{v.createTime}}</view>
				</view>
			</view>
		</view>
		<view class="page-order-post-main">
			<view class="page-order-post-title">共计商品：{{count}}件</view>
			<view class="page-order-post-address">
				<view class="address-title">收货地址：</view>
				<view
					class="address-info"
					@click="setGetAddress"
				>
					<text v-if="notAddress">点击选择收货地址</text>
					<view v-else>
						<view>{{address.receiver}} {{address.mobile}}</view>
						<view>{{address.area}} {{address.detailAddress}}</view>
					</view>
				</view>
			</view>
			<button
				class="page-order-post-btn"
				@click="send"
			>确定发货</button>
		</view>
	</view>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
export default {
	data() {
		return {
			// addressList: []
		};
	},
	onShow() {
		if (this.notAddress) {
			this.$log('获取一次地址');
			this.getAddress();
		}
	},
	methods: {
		padImageURL(url) {
			return this.$websiteUrl + url;
		},
		async send() {
			let res = [null, { confirm: true }];
			if (this.freeShipping) {
				if (this.count < 3) {
					res = await uni.showModal({
						title: '温馨提示',
						content:
							'亲，订单满三个商品包邮哦，0元商品不算计在内哦，请支付8元运费哦'
					});
				}
			} else {
				// res = await uni.showModal({
				// 	title: '温馨提示',
				// 	content: `亲，您的地区不支持包邮哦，请支付${this.transportFee}元运费哦`
				// });
			}
			if (res[1].confirm) {
				this.postOrder();
			}
		},
		postOrder() {
			if (this.notAddress) {
				this.$alert('请点选择收货地址');
				return;
			}
			this.$log('提交订单', this.address);
			let data = Object.assign(
				{
					orderIdList: this.orderIDs
				},
				this.address
			);
			this.$api('order.postPay', data).then(e => {
				if (!e.paySign) {
					this.$back();
					return;
				}
				e.success = res => {
					this.$back();
				};
				e.fail = res => {
					console.error(res);
					// this.$back();
				};
				e.signType = 'MD5';
				e.package = 'prepay_id=' + e.prepayId;
				if (!e.timeStamp) {
					e.timeStamp = e.preOrderTime;
				}
				uni.requestPayment(e);
			});
		},
		setGetAddress() {
			this.$go('./address?type=select');
		},
		async getAddress() {
			this.$log('Loading');
			const done = this.$loading();
			try {
				const res = await this.$api('address.get');
				// this.addressList = res;
				this.$store.commit(
					'current/setAddress',
					res.find(e => e.isDefaultAddress) || {}
				);
				done();
			} catch (err) {
				done();
				this.$alert('网络繁忙，请稍后再试');
				this.$log(err);
			}
		}
	},
	computed: {
		freeShipping() {
			return this.transportFee == 0;
		},
		transportFee() {
			return this.address.transportFee;
		},
		notAddress() {
			return JSON.stringify(this.address) === '{}';
		},
		count() {
			return this.itemsData.length;
		},
		orderIDs() {
			return this.itemsData.map(e => {
				return e.orderId;
			});
		},
		...mapState('current', {
			itemsData: state => state.goods,
			address: state => state.address
		})
	}
};
</script>

<style lang="less">
@import 'order.less';
.address-title {
	color: #666;
	margin-right: 10rpx;
}
.address-info {
	flex: 1;
}
.page-order-post-address {
	display: flex;
	min-height: 80rpx;
	margin-bottom: 35rpx;
	font-size: 21rpx;
}
.page-order.page-order-post {
	background: #fff;
	padding: 0;
	.page-order-post-box {
		padding: @paadingSize;
		background-color: @lightGreyPageBackground;
	}
}
.page-order-post-main {
	padding: 44rpx 90rpx;
}
.page-order-post-title {
	font-size: 24rpx;
	font-weight: bold;
	margin-bottom: 35rpx;
}
.page-order-post-btn {
	background: @themeColor;
	.radius();
	color: #fff;
	font-size: 30rpx;
}
</style>