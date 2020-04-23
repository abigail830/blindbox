<!--
 * @Author: seekwe
 * @Date: 2020-03-01 17:14:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-30 12:51:19
 -->
<template>
	<view class="category-view">
		<view class="category-header">
			<view class="series">
				<view
					@click="setSeriesActive(v.id)"
					:class="{'series-box_on':seriesActive===v.id}"
					class="series-box"
					v-for="(v,k) in seriesShowData"
					:key="k"
				>
					<image class="series-box-image" mode="aspectFill" :src="v.image" />
					<view class="series-box-title one-text">{{v.title}}</view>
				</view>
				<view v-if="seriesData.length>4" @click="isShowBottom = true" class="series-arrow"></view>
			</view>
		</view>
		<zCategoryList :seriesActive="seriesActive" :height="scrollHeight" />
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
import { mapState, mapGetters } from 'vuex';
import zHomeAll from '@/components/box/zHomeAll';
import zCategoryList from '@/components/box/zCategoryList/index';
export default {
	components: { zCategoryList, zHomeAll },
	data() {
		return {
			scrollHeight: '',
			isShowBottom: false,
			seriesActive: ''
		};
	},
	computed: {
		seriesShowData() {
			return this.seriesData.slice(0, 4);
		},
		seriesData() {
			return this.series.items || [];
		},
		...mapState(['series']),
		...mapGetters(['userInfo', 'authState', 'banState'])
	},
	onHide() {
		this.isShowBottom = false;
	},
	mounted() {
		this.scrollHeight = this.$to.px2rpx(this.$systems.windowHeight) - 200;
		if (this.seriesData.length <= 0) {
			this.$log('需要更新系列数据');
		}
		this.defSeriesActive();
	},
	methods: {
		defSeriesActive() {
			this.$log('seriesActive', this.seriesActive);
			if (!this.seriesActive) {
				this.$log('设置一个默认系列');
				this.seriesActive = this.seriesData[0].id;
			}
		},
		setSeriesActive(id) {
			this.seriesActive = id;
			this.$log('加载指定系列:', id);
		}
	}
};
</script>

<style lang="less">
@import './ranking.less';
</style>