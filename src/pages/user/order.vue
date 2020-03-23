<!--
 * @Author: seekwe
 * @Date: 2020-03-11 13:03:12
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:18:21
 -->
<template>
	<view class="page page-order">
		<view @click="show(v,k)" class="page-order-items" v-for="(v,k) in itemsData" :key="k">
			<image class="page-order-image" :src="v.image" mode="aspectFill" />
			<view class="page-order-info">
				<view class="title">{{v.type}} {{v.title}}</view>
				<view class="time">{{v.createTime}}</view>
			</view>
		</view>
		<zOrderInfo :info="info" @close="close" :isShow="isShow" />
	</view>
</template>

<script>
import { zOrderInfo } from '@/components/box/zOrderInfo';
import { lightGreyPageBackground } from '@/common/var';
export default {
	components: { zOrderInfo },
	onPullDownRefresh() {
		this.$log('刷新');
		this.itemsPage = 1;
		this.getItems();
	},
	onReachBottom() {
		this.itemsPage++;
		this.$log('下一页', this.itemsPage);
		this.getItems();
	},
	data() {
		return {
			itemsPage: 1,
			info: {},
			itemsData: []
		};
	},
	computed: {
		isShow() {
			return JSON.stringify(this.info) !== '{}';
		}
	},
	watch: {},
	created() {
		uni.startPullDownRefresh();
		// this.getItems();
	},
	methods: {
		close() {
			this.info = {};
		},
		show(v, k) {
			this.$log('show', v);
			this.info = v;
		},
		getItems() {
			let itemsData = [];
			let done = this.$loading();
			for (let i = 0; i < 10; i++) {
				itemsData.push({
					image: '/static/demo.png',
					title: `产品名称-${this.itemsPage}-${i}`,
					type: '[抽盒]',
					mumber: 1,
					createTime: '2020-03-11 13:14:19'
				});
			}
			setTimeout(_ => {
				if (this.itemsPage > 1) {
					this.itemsData = this.itemsData.concat(itemsData);
				} else {
					this.itemsData = itemsData;
				}
				this.info = itemsData[0];
				uni.stopPullDownRefresh();
				done();
			}, 1000);
		}
	}
};
</script>

<style lang="less">
@import '../../common/var.less';
.page-order {
	background-color: @lightGreyPageBackground;
	padding: @paadingSize;
	color: @fontColor;
}
.page-order-items {
	.radius();
	display: flex;
	background: #fff;
	margin-bottom: @paadingSize;
	padding: @paadingSize;
	height: 156rpx;
	box-sizing: border-box;
}
.page-order-image {
	width: 106rpx;
	height: 94rpx;
	border-radius: 5rpx;
	overflow: hidden;
}
.page-order-info {
	padding-left: 20rpx;
	.title {
		font-size: 28rpx;
	}
	.time {
		margin-top: 20rpx;
		font-size: 24rpx;
		color: @lightGreyColor;
	}
}
</style>