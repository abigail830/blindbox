<!--
 * @Author: seekwe
 * @Date: 2020-03-01 17:15:28
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-27 15:39:04
 -->
<template>
	<view class="page page-lottery">
		<view class="items">
			<view class="items-box">
				<view
					class="item"
					v-for="(v,k) in itemsData"
					:key="k"
					@click="goInfo(v,k)"
				>
					<view class="item-tag">
						<image
							src="/static/top-icon.png"
							mode="aspectFill"
							class="item-icon"
							v-if="v.shownInAd"
						/>
					</view>
					<img
						class="item-image"
						mode="widthFix"
						:src="v.image"
					/>
					<view class="item-title">{{v.activityName}}</view>
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

			this.$api('activities.all').then(e => {
				let itemsData = e.map(e => {
					let data = e;
					data.image = this.$websiteUrl + e.mainImgUrl;
					return data;
				});
				this.itemsData = itemsData;
				this.$nextTick(_ => {
					uni.stopPullDownRefresh();
					done();
				});
			});
			// setTimeout(_ => {
			// 	for (let i = 0; i < 5; i++) {
			// 		itemsData.push({
			// 			image: '/static/demo.png',
			// 			id: i,
			// 			isTop: i <= 2,
			// 			title: `(${this.itemsPage})我的标题:` + i
			// 		});
			// 	}
			// 	if (this.itemsPage > 1) {
			// 		this.itemsData = this.itemsData.concat(itemsData);
			// 	} else {
			// 		this.itemsData = itemsData;
			// 	}

			// }, 1000);
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