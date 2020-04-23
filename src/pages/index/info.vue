<!--
 * @Author: seekwe
 * @Date: 2020-03-02 15:30:29
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-19 12:44:34
 -->
<template>
	<view class="page page-info">
		<view class="info-view">
			<view class="info-header">{{series.name}}</view>
			<view>
				<view class="info-box">
					<image class="info-cover" :src="headerImage" mode="widthFix" />
					<view class="boxs">
						<view :class="{'on':k}" v-for="(v,k) in items" :key="k" class="box" @click="select(v,k)">
							<view class="mini-icon">{{k+1}}</view>
							<image :src="v.image" mode="aspectFill" class="been-icon" :class="{'off':!v.id}" />
						</view>
						<!-- <view class="box placeholder" v-for="i in (4-(items.length) %4)" :key="i">
							<view class="mini-icon">{{items.length+i+1}}</view>
							<image src="/static/been-icon.png" mode="aspectFill" class="been-icon" />
						</view>-->
					</view>
				</view>
				<view class="data">
					<view class="data-code">
						产品序列号：
						<view>{{series.id}}</view>
					</view>
					<view class="data-look" @click="goLook">
						<image class="look-icon" src="/static/look-icon.png" mode="widthFix" />
						<view>系列预览</view>
					</view>
				</view>
			</view>
			<button @click="chooseIt" class="info-btn">就选 Ta</button>
		</view>
		<zHomeInfo :isShowBottom="showInfoView" @close="closeInfo" :images="images" />
	</view>
</template>

<script>
import zHomeInfo from '@/components/box/zHomeInfo';
import { mapState } from 'vuex';
import { soldOut } from '@/config';
import { shuffle } from '@/common/util';
export default {
	components: { zHomeInfo },
	data() {
		return {
			items: [],
			showInfoView: false
		};
	},
	computed: {
		headerImage() {
			return this.$websiteUrl + this.series.matrixHeaderImage;
		},
		productVolume() {
			return this.series.productVolume || 12;
		},
		images() {
			return [this.$websiteUrl + this.series.longImage];
		},
		cellImage() {
			return this.$websiteUrl + this.series.matrixCellImage;
		},
		...mapState('current', {
			series: state => state.series
		})
	},
	methods: {
		select(v, k) {
			this.$log('去吧，比卡丘', v, k);
			if (v.state) {
				this.$go('./buy?id=' + this.series.id);
			}
		},
		chooseIt() {
			this.$log('去吧，比卡丘');
			this.$go('./buy?id=' + this.series.id);
		},
		closeInfo() {
			this.showInfoView = false;
		},
		goLook() {
			this.$log('预览');
			this.showInfoView = true;
		},
		getInfo() {
			console.log('series', this.series, this.productVolume);
			let items = [];
			for (let i = 0; i < this.productVolume; i++) {
				items.push({
					image: this.cellImage,
					id: i,
					state: true
				});
			}
			const column = 4;
			// this.series.columnSize
			let pad = column - (items.length % column);
			if (column <= pad) {
				pad = 0;
			}
			let pad2 = soldOut - pad;
			if (pad2 > 0) {
				items = items.slice(pad2);
				pad = pad + pad2;
			}

			for (let i = 0; i < pad; i++) {
				items.push({
					image: '/static/been-icon.png',
					id: 0,
					state: false
				});
			}

			this.$log('需要加几个售完', pad2);

			this.items = shuffle(items);
		}
	},
	onLoad(data) {
		this.$log('data', data);
		// 后端没有提供根据 id 获取数据, 只能自己维护
		this.getInfo();
	}
};
</script>

<style lang="less">
@import 'info.less';
</style>