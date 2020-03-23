<template>
	<view class="page page-lottery">
		<view class="items">
			<view class="items-box">
				<view class="item" v-for="(v,k) in itemsData" :key="k" @click="goInfo(v,k)">
					<view class="item-tag">
						<image src="/static/top-icon.png" mode="aspectFit" class="item-icon" v-if="v.isTop" />
					</view>
					<img class="item-image" mode="widthFix" :src="v.image" />
					<view class="item-title">{{v.title}}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { onShareAppMessage } from '@/common/mixins';
export default {
	data() {
		return {
			itemsData: [],
			itemsPage: 1
		};
	},
	onShareAppMessage: onShareAppMessage,
	onPullDownRefresh() {
		this.$log('刷新');
		this.itemsPage = 1;
		this.getItems();
		// setTimeout(_ => {
		// 	uni.stopPullDownRefresh();
		// }, 2000);
	},
	onReachBottom() {
		this.$log('下一页');
		this.itemsPage++;
		this.getItems();
	},
	mounted() {
		this.getItems();
	},
	methods: {
		getItems() {
			let done = this.$loading();
			let itemsData = [];

			setTimeout(_ => {
				for (let i = 0; i < 5; i++) {
					itemsData.push({
						image: '/static/demo.png',
						id: i,
						isTop: i <= 2,
						title: `(${this.itemsPage})我的标题:` + i
					});
				}
				if (this.itemsPage > 1) {
					this.itemsData = this.itemsData.concat(itemsData);
				} else {
					this.itemsData = itemsData;
				}
				uni.stopPullDownRefresh();
				done();
			}, 1000);
		},
		goInfo(v) {
			this.$go('./info?id=' + v.id);
		}
	}
};
</script>

<style lang='less'>
@import 'activity.less';
</style>