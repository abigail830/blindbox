<!--
 * @Author: seekwe
 * @Date: 2020-03-02 15:30:29
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-22 22:29:13
 -->

<template>
	<view class="page page-info">
		<view class="info-view">
			<view class="info-header">产品名称</view>
			<view>
				<view class="info-box">
					<image class="info-cover" src="/static/demo.png" mode="widthFix" />
					<view class="boxs">
						<view :class="{'on':k}" v-for="(v,k) in items" :key="k" class="box">
							<view class="mini-icon">{{k+1}}</view>
						</view>
						<view class="box placeholder" v-for="i in (4-(items.length) %4)" :key="i">
							<view class="mini-icon">{{items.length+i+1}}</view>
							<image src="/static/been-icon.png" mode="aspectFill" class="been-icon" />
						</view>
					</view>
				</view>
				<view class="data">
					<view class="data-code">产品序列号：00000000</view>
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
export default {
	components: { zHomeInfo },
	data() {
		return {
			items: [],
			images: [],
			showInfoView: true
		};
	},
	methods: {
		chooseIt() {
			this.$log('去吧，比卡丘');
			this.$go('./buy');
		},
		closeInfo() {
			this.showInfoView = false;
		},
		goLook() {
			this.$log('预览');
			this.showInfoView = true;
		},
		getInfo() {
			let items = [];
			let images = [];
			// 这里假装有 5 个,就补了三个
			for (let i = 0; i < 5; i++) {
				items.push(i);
				images.push('/static/demo.png');
			}
			this.items = items;
			this.images = images;
		}
	},
	onLoad(data) {
		this.$log('data', data);
		this.getInfo();
	}
};
</script>

<style lang="less">
@import 'info.less';
</style>