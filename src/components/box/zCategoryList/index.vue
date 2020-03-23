<!--
 * @Author: seekwe
 * @Date: 2020-03-11 18:17:03
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-18 16:40:00
 -->
<template>
	<view class="z-category-list-view-box">
		<view class="z-category-list-left">
			<scroll-view :scroll-y="true" :style="{'height':height+'rpx' }">
				<view
					class="item-box"
					v-for="(item,index) in leftArray"
					:key="index"
					:class="{'active':index===leftIndex}"
					:data-index="index"
					@click="leftTap"
				>{{item}}</view>
			</scroll-view>
		</view>
		<scroll-view
			class="z-category-list-main"
			:scroll-y="true"
			:style="{'height':height+'rpx' }"
			:key="index"
			:scroll-with-animation="true"
			@scroll="scroll"
			:scroll-top="scrollTop"
		>
			<view id="#position"></view>
			<view class="main-box">
				<view class="cover-box" v-for="(v,i) in itemsData" :key="i" @click="tapItem(v,i)">
					<image class="cover-image" :src="v.image" mode="aspectFill" />
					<view class="cover-title">{{v.title}}</view>
				</view>
			</view>
			<view class="z-category-tools">
				<view @click="createPoster" class="poster-btn">生成海报</view>
			</view>
		</scroll-view>
		<zPoster ref="poster" @result="posterResult" :config="posterConfig" :show="true" />
		<view @click="posterImage = ''" v-show="posterImage" class="poster-image-block">
			<image
				@click.stop
				class="poster-image"
				v-if="!!posterImage"
				:src="posterImage"
				mode="aspectFill"
			/>
		</view>
	</view>
</template>

<script>
import zPoster from '@/components/util/zPoster';
export default {
	components: {
		zPoster
	},
	props: {
		height: {
			type: Number,
			default: 0
		}
	},
	data() {
		return {
			leftArray: [],
			mainArray: [],
			leftIndex: 0,
			scrollTop: 0,
			posterConfig: {},
			old: {
				scrollTop: 0
			},
			posterImage: ''
		};
	},
	computed: {
		itemsData() {
			return this.mainArray[this.leftIndex] || [];
		}
	},
	mounted() {
		this.getListData();
	},
	methods: {
		posterResult(e, state) {
			this.$log('--', e, state);
			if (state) {
				this.posterImage = e.path;
			}
		},
		getPosterConfig() {
			let config = {};
			let width = this.$to.px2rpx(this.$systems.windowWidth);
			let height = this.$to.px2rpx(this.$systems.windowHeight);
			config = {
				width: width,
				backgroundColor: '#f7b52c',
				height: height,
				views: []
			};
			this.posterConfig = config;
		},
		async createPoster() {
			// const done = this.$loading();
			this.getPosterConfig();
			this.$refs['poster'].create();
			// done();
			this.$log('createPoster', this.posterImage);
		},
		tapItem(v, i) {
			this.$log('点击了');

			this.$log(v, i);
		},
		getListData() {
			let [left, main] = [[], []];

			for (let i = 0; i < 20; i++) {
				left.push(`产品系列名称${i + 1}`);

				let list = [];
				for (let j = 0; j < 30; j++) {
					list.push({
						title: `${i}.明星标题${j + 1}`,
						image: '/static/demo.png'
					});
				}
				main.push(list);
			}
			this.leftArray = left;
			this.mainArray = main;
		},
		leftTap(e) {
			let index = e.currentTarget.dataset.index;
			this.leftIndex = +index;
			this.scrollTop = this.old.scrollTop;
			this.$nextTick(function() {
				this.scrollTop = 0;
			});
		},
		scroll(e) {
			this.old.scrollTop = e.detail.scrollTop;
		}
	}
};
</script>

<style lang='less'>
@import 'index.less';
</style>