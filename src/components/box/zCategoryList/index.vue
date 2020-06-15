<!--
 * @Author: seekwe
 * @Date: 2020-03-11 18:17:03
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-11 11:57:25
 -->
<template>
	<view class="z-category-list-view-box">
		<view class="z-category-list-left">
			<scroll-view
				:scroll-y="true"
				:style="{'height':height+'rpx' }"
			>
				<view
					class="item-box one-text"
					v-for="(item,index) in leftArray"
					:key="index"
					:class="{'active':index===leftIndex}"
					:data-index="index"
					@click="leftTap"
				>{{item.name}}</view>
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
				<view
					class="cover-box"
					v-for="(v,i) in itemsData"
					:key="i"
					@click="tapItem(v,i)"
				>
					<image
						class="cover-image"
						:src="v.image"
						mode="aspectFill"
					/>
					<view class="cover-title">{{v.title}}</view>
				</view>
			</view>
			<view
				class="z-category-tools"
				:style="{'bottom':$bottom+'rpx'}"
			>
				<zLoginBtn
					:authState="!!nickName"
					@run="createPoster"
					v-if="itemsData.length>0"
					:customClass="'poster-btn'"
				>生成海报</zLoginBtn>
			</view>
		</scroll-view>
		<zPoster
			ref="poster"
			@result="posterResult"
			:config="posterConfig"
			:show="false"
		/>
		<!-- <view @click="posterImage = ''" v-show="posterImage" class="poster-image-block">
			<image
				@longpress.stop="savePoster"
				class="poster-image"
				v-if="!!posterImage"
				:src="posterImage"
				mode="aspectFit"
		/>-->
		<!-- <view class="close-icon-box">
				<image class="close-image" src="/static/universal-icon.png" mode />
		</view>-->
		<!-- </view> -->
		<image
			@click="posterImage=''"
			v-if="debug&&posterImage"
			:src="posterImage"
			mode="widthFix"
			class="demo"
		/>
		<zRankingPreview
			@buy="goBuy"
			:data="preview"
			@close="closePreview"
		/>
	</view>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import zPoster from '@/components/util/zPoster';
import zLoginBtn from '@/components/util/zLoginBtn';
import zRankingPreview from '@/components/box/zRankingPreview';
import { all as $all } from '@/apis/index';
import { posterCopywritingAll } from '@/config';
let padding = 94;
let preview = {};
// let width = this.$to.px2rpx(this.$systems.windowWidth);
// let height = this.$to.px2rpx(this.$systems.windowHeight);

let width = 750;
let height = 1008;
let posterBox = {
	width: width - padding * 2,
	height: height - padding * 2
};
// if (process.env.NODE_ENV === 'development') {
// 	preview = {
// 		id: 'ccff6c8f-3b84-4cdc-a548-2c2f7421eea2',
// 		image:
// 			'https://blindbox.fancier.store/images/product/ccff6c8f-3b84-4cdc-a548-2c2f7421eea2.png'
// 	};
// }
export default {
	components: {
		zPoster,
		zLoginBtn,
		zRankingPreview
	},
	props: {
		height: {
			type: Number,
			default: 0
		},
		seriesActive: {
			type: String,
			default: ''
		}
	},
	data() {
		return {
			debug: process.env.NODE_ENV === 'development',
			$bottom: 50,
			seriesItems: [],
			leftArray: [],
			mainArray: [],
			leftIndex: 0,
			scrollTop: 0,
			posterConfig: {},
			old: {
				scrollTop: 0
			},
			posterImage: '',
			preview: preview
		};
	},
	computed: {
		showPreview() {
			return JSON.stringify(this.preview) !== '{}';
		},
		itemsData() {
			return this.mainArray[this.leftIndex] || [];
		},
		nickName() {
			return this.userInfo.nickName || '';
		},
		avatarUrl() {
			return this.userInfo.avatarUrl || '';
		},
		posterBgImage() {
			let posterBgImage = '/static/p-bg.jpg';

			let data = this.leftArray[this.leftIndex];
			if (data) {
				posterBgImage = this.$websiteUrl + data.posterBgImage;
			}
			return posterBgImage;
		},
		currentSeries() {
			return this.seriesItems[this.leftIndex] || {};
		},
		...mapGetters(['userInfo'])
	},
	watch: {
		seriesActive: {
			immediate: true,
			handler(n, o) {
				if (!!n) {
					this.getListData();
				}
			}
		}
	},
	mounted() {
		if (process.env.NODE_ENV === 'development') {
			setTimeout(this.createPoster, 2000);
		}
		this.$bottom =
			this.$systems.safeArea.height - this.$systems.safeArea.bottom + 100;
		// this.$log(this.$systems.safeArea);
	},
	methods: {
		closePreview() {
			this.preview = {};
		},
		goBuy() {
			this.$store.commit('current/setSeriesData', this.currentSeries);
			this.$go('index/info?id=' + this.currentSeries.id);
			this.closePreview();
		},
		posterResult(e, state) {
			console.timeEnd('poster');
			this.$loading()();
			this.$log('保存海报', e, state);
			if (state) {
				this.posterImage = e.path;
				this.$refs.poster.save();
				if (process.env.NODE_ENV !== 'development') {
					uni.previewImage({
						urls: [this.posterImage],
						longPressActions: {}
					});
				}
			}
		},
		getPosterConfig() {
			let [config, views] = [{}, []];
			views.push({
				type: 'rect',
				x: padding,
				y: padding,
				radius: 20,
				width: posterBox.width,
				height: posterBox.height,
				background: '#fff'
			});
			views.push({
				type: 'img',
				src: this.avatarUrl,
				width: 130,
				height: 130,
				x: '+40',
				y: '+40',
				radius: '100%'
			});
			views.push({
				type: 'text',
				x: '+40',
				positionX: true,
				y: '+40',
				text: this.nickName,
				// center: true,
				color: '#000',
				width: 310,
				fontSize: 36,
				maxLine: 1
			});
			views.push({
				type: 'text',
				x: padding + 40,
				y: 280,
				text: posterCopywritingAll,
				color: '#000',
				width: 480,
				fontSize: 36,
				height: 100,
				maxLine: 2
			});
			this.posterConfig = {
				width: width,
				backgroundColor: '#f7b52c',
				backgroundColor: '#000',
				backgroundImage: this.posterBgImage,
				height: height,
				views: views
			};
		},
		getPosterViewsConfig() {
			let views = [];
			let proportion = 2;
			let y = 400;
			let x = padding + 10;
			let data = (this.mainArray[this.leftIndex] || []).filter(e => !!e.image);
			this.$log('this.mainArray', this.mainArray[this.leftIndex]);
			let len = data.length;
			for (let i = 0; i < len; i++) {
				// for (let i = 0; i < 17; i++) {
				let index = i % 4;
				if (i >= 4 && index === 0) {
					y = y + 397 / proportion;
				}

				if (index === 0) {
					x = padding + 10;
				} else {
					x = x + 259 / proportion + 6;
				}
				// const isGray = data[i].gray;
				views.push({
					type: 'img',
					src: data[i].image,
					// src: isGray ? data[i].grayImage : data[i].image,
					title: data[i].title,
					width: 259 / proportion,
					height: 331.52 / proportion,
					x: x,
					y: y
				});

				views.push({
					type: 'text',
					text: data[i].title,
					width: 259 / proportion,
					x: x,
					center: true,
					fontSize: 16,
					y: y + 331.52 / proportion,
					maxLine: 1
				});
			}
			let diffH = y - (posterBox.height - 120);
			if (diffH > 0) {
				this.posterConfig.views[0].height =
					this.posterConfig.views[0].height + diffH;
				this.posterConfig.height = this.posterConfig.height + diffH;
			}
			this.$log('y', y, diffH);
			this.posterConfig.views.push(...views);
		},
		async createPoster(e) {
			this.$log(e);
			// 如果没有登录需要登录获取昵称
			if (!this.nickName) {
				if (!(e && e.rawData)) {
					return;
				}
				await this.$getuserinfo(e);
			}
			this.$loading('海报生成中...');
			this.$nextTick(_ => {
				this.$log('海报生成中...', this.nickName);
				this.getPosterConfig();
				this.getPosterViewsConfig();
				this.$refs['poster'].clear();
				this.$api(_ => {
					return ['user.shareCollection', this.seriesActive];
				});
				setTimeout(_ => {
					console.time('poster');
					this.$refs['poster'].create();
					// this.$nextTick();
				}, 100);
			});
		},
		tapItem(v, i) {
			if (v.id) {
				this.$log('点击了,显示详情', v, i);
				this.preview = v;
			}
		},
		getListData() {
			const done = this.$loading();
			let [left, main] = [[], []];

			this.$api(_ => {
				return ['products.series', this.seriesActive];
			}).then(async e => {
				let apis = [];
				e.forEach((e, k) => {
					left.push({ name: e.name, posterBgImage: e.posterBgImage });
					console.log('获取数据', e.name);
					let id = e.id;
					apis[k] = this.$api(_ => {
						return ['products.childProducts', id];
					})
						.then(async e => {
							let flags = [];
							try {
								flags = await this.$api(_ => [
									'products.productsWithBuyFlag',
									id
								]);
							} catch (e) {
								console.error('获取是否抽中过失败', e);
							}
							let data = e.map(e => {
								const id = e.id;
								let gray = true;
								for (const flag of flags) {
									if (id == flag.id) {
										gray = !flag.buy;
										break;
									}
								}
								return {
									id: id,
									title: `${e.name}`,
									image:
										this.$websiteUrl +
										(gray ? e.productGrayImage : e.productImage),
									// grayImage: this.$websiteUrl + e.productGrayImage,
									// 海报产品是否灰色
									gray: gray
								};
							});
							// todo dev
							// data = data.concat(data, data);
							if (!!data.length && !!(data.length % 2)) {
								this.$log('需要补位');
								data.push({
									id: 0,
									title: '',
									image: '',
									productImage: ''
								});
							}
							main[k] = data;
						})
						.catch(e => {});
				});
				this.seriesItems = e;
				await $all(apis);
				this.leftArray = left;
				this.mainArray = main;
				done();
				this.leftTap(0);
			});
		},
		leftTap(e) {
			let index = !e ? 0 : e.currentTarget.dataset.index;
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