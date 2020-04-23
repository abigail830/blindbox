<!--
 * @Author: seekwe
 * @Date: 2020-03-09 22:13:51
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-19 17:09:15
 -->
<template>
	<view class="page home-page">
		<!-- <zAddTip :duration="10" /> -->
		<view class="header">
			<swiper
				class="banner"
				:indicator-color="swiperConfig.indicatorColor"
				:indicator-dots="swiperConfig.indicatorDots"
				:indicator-active-color="swiperConfig.indicatorActiveColor"
				:autoplay="swiperConfig.autoplay"
				:interval="swiperConfig.interval"
				:duration="swiperConfig.duration"
			>
				<swiper-item
					v-for="(v,k) in swiperItems"
					:key="k"
				>
					<view class="banner-item">
						<image
							@click="goActivity(v)"
							class="banner-image"
							mode="aspectFill"
							:src="v.image"
						/>
					</view>
				</swiper-item>
			</swiper>
			<zBarrage
				:closeRandomColor="true"
				ref="zBarrage"
				:topRange="['10rpx','66rpx','120rpx','190rpx','260rpx']"
			/>
		</view>
		<view class="tabs">
			<view
				@click="switchTab('new')"
				class="tab"
				:class="{'tab_on':isNew}"
			>
				新品
				<view class="tab-icon">◆ NEW ◆</view>
			</view>
			<text class="tab-separate"></text>
			<view
				@click="switchTab('all')"
				class="tab tab_all"
				:class="{'tab_on':!isNew}"
			>
				全部
				<view class="tab-icon">◆ ALL ◆</view>
			</view>
		</view>
		<view class="items">
			<view
				class="items-box"
				v-show="isNew"
			>
				<view
					class="item"
					v-for="(v,k) in itemsNewData"
					:key="k"
					@click="goInfo(v,k)"
				>
					<view class="item-tag">
						<image
							src="/static/top-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isTop"
						/>
						<image
							src="/static/new-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isNewSeries"
						/>
						<image
							src="/static/advance-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isPresale"
						/>
					</view>
					<img
						class="item-image"
						mode="widthFix"
						:src="v.image"
					/>
					<view class="item-title">
						<view class="item-price">{{v.price}}</view>
						{{v.name}}
					</view>
				</view>
			</view>
			<view
				class="items-box"
				v-show="!isNew"
			>
				<view class="series">
					<view
						@click="setSeriesActive('all')"
						class="series-box"
						:class="{'series-box_on':isSeriesAll}"
					>
						<image
							class="series-box-image"
							mode="aspectFill"
							src="/static/all-icon.png"
						/>
						<view class="series-box-title">ALL</view>
					</view>
					<view
						@click="setSeriesActive(v.id)"
						:class="{'series-box_on':seriesActive===v.id}"
						class="series-box"
						v-for="(v,k) in seriesShowData"
						:key="k"
					>
						<image
							class="series-box-image"
							mode="aspectFill"
							:src="v.image"
						/>
						<view class="series-box-title one-text">{{v.title||" "}}</view>
					</view>
					<view
						class="series-box"
						@click="isShowBottom=true"
					>
						<view class="series-box-image">•••</view>
						<view class="series-box-title">更多</view>
					</view>
				</view>
				<view
					class="item"
					v-for="(v,k) in itemsData"
					:key="k"
					@click="goInfo(v,k)"
				>
					<view class="item-tag">
						<image
							src="/static/top-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isTop"
						/>
						<image
							src="/static/new-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isNewSeries"
						/>
						<image
							src="/static/advance-icon.png"
							mode="aspectFit"
							class="item-icon"
							v-if="v.isPresale"
						/>
					</view>
					<img
						class="item-image"
						mode="widthFix"
						:src="v.image"
					/>
					<view class="item-title">
						<view class="item-price">{{v.price}}</view>
						{{v.name}}
					</view>
				</view>
			</view>
		</view>
		<zHomeAll
			@setSeriesActive="setSeriesActive"
			:seriesData="seriesData"
			:seriesActive="seriesActive"
			@close="isShowBottom=false"
			:isShowBottom="isShowBottom"
		/>
	</view>
</template>

<script>
import { computed, data, methods } from './home';
import { mapState, mapGetters } from 'vuex';
import { onShareAppMessage } from '@/common/mixins';
import zAddTip from '@/components/util/zWeappAddTip';
import zBarrage from '@/components/util/zBarrage';
import zHomeAll from '@/components/box/zHomeAll';
import { themeColor } from '@/common/var';

export default {
	components: { zAddTip, zHomeAll, zBarrage },
	onShareAppMessage: onShareAppMessage,
	onPullDownRefresh() {
		this.$log('刷新');
		this.itemsNewPage = 0;
		this.getItems();
		this.itemsDataPage = 0;
		this.setSeriesActive('all');
		setTimeout(uni.stopPullDownRefresh, 1000);
	},
	onReachBottom() {
		this.$log('下一页');
		if (this.isNew) {
			this.itemsNewPage++;
			this.getItems();
		} else if (this.seriesActive == 'all') {
			this.itemsDataPage++;
			this.setSeriesActive('all');
		}
	},
	onHide() {
		this.isShowBottom = false;
	},
	computed: computed(),
	mounted() {
		this.getItems();
		this.setSeriesActive('all');
		// this.setSeriesActive('28b0242a-bc6a-4bda-89fa-dc6fa5290156');
		this.$log('设置弹幕');
		this.setBarrage();
		setInterval(_ => {
			this.setBarrage();
		}, 6000);
	},
	data: data,
	methods: methods()
};
</script>

<style lang='less'>
@import 'home.less';
</style>