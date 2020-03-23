<!--
 * @Author: seekwe
 * @Date: 2020-03-10 10:14:15
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:16:16
 -->

<template>
	<view class="z-home-all-box">
		<view
			class="sheet-view"
			:class="{'sheet-show':isShowBottom,'sheet-hide':!isShowBottom}"
			@touchmove.stop.prevent="moveHandle"
			@click="closeSheet()"
		>
			<scroll-view
				scroll-y="true"
				class="sheet-box"
				:class="{'sheet-box_on':isShowBottom}"
				@click.stop="stopEvent()"
			>
				<view class="series series-items">
					<view
						@click="setSeriesActive('all')"
						class="series-box"
						:class="{'series-box_on':isSeriesAll}"
					>
						<img class="series-box-image" mode="aspectFill" src="/static/all-icon.png" />
						<!-- <view class="series-box-image">ALL</view> -->
						<view class="series-box-title">ALL</view>
					</view>
					<view
						@click="setSeriesActive(v.id)"
						:class="{'series-box_on':seriesActive===v.id}"
						class="series-box"
						v-for="(v,k) in seriesData"
						:key="k"
					>
						<img class="series-box-image" mode="aspectFill" :src="v.image" />
						<view class="series-box-title one-text">{{v.title}}</view>
					</view>
					<view class="series-box placeholder" v-for="i in (5-(seriesData.length +1) %5)" :key="i"></view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>
<script>
export default {
	props: {
		isShowBottom: {
			type: Boolean,
			default: false
		},
		seriesActive: {
			type: [String, Number],
			default: 'all'
		},
		seriesData: {
			type: Array,
			default() {
				return [];
			}
		}
	},
	computed: {
		isSeriesAll() {
			return this.seriesActive === 'all';
		}
	},
	methods: {
		setSeriesActive(id) {
			this.$emit('setSeriesActive', id);
			this.closeSheet();
		},
		closeSheet() {
			this.$emit('close');
		},
		stopEvent() {},
		moveHandle() {}
	}
};
</script>

<style lang="less">
@import 'index.less';
</style>
