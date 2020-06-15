<!--
 * @Author: seekwe
 * @Date: 2020-03-02 15:30:29
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-05 13:37:44
 -->
<template>
	<view class="page page-info">
		<view
			v-if="loading"
			class="info-loading"
		>
		</view>
		<view
			v-else
			class="info-view"
		>
			<view class="info-header">{{series.name}}</view>
			<view class="info-header-timeout">剩余抽盒时间: {{countDown}} s</view>
			<view>
				<view class="info-box">
					<image
						class="info-cover"
						:src="headerImage"
						mode="widthFix"
					/>
					<view :class='["boxs","box-"+columnSize]'>
						<view
							:class="{'on':k,'box':true}"
							v-for="(v,k) in items"
							:key="k"
							@click="select(v,k)"
						>
							<view class="mini-icon">{{k+1}}</view>
							<image
								:src="v.image"
								mode="aspectFill"
								class="been-icon"
								:class="{'off':!v.state}"
							/>
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
					<view
						class="data-look"
						@click="goLook"
					>
						<image
							class="look-icon"
							src="/static/look-icon.png"
							mode="widthFix"
						/>
						<view>系列预览</view>
					</view>
				</view>
			</view>
			<button
				@click="chooseIt"
				class="info-btn"
			>就选 Ta</button>
		</view>
		<zHomeInfo
			:isShowBottom="showInfoView"
			@close="closeInfo"
			:images="images"
		/>
	</view>
</template>

<script>
let timer, show, done, infoID;
import zHomeInfo from '@/components/box/zHomeInfo';
import { mapState } from 'vuex';
import { soldOut, boxExtractionTimeout } from '@/config';
import { shuffle } from '@/common/util';

export default {
	components: { zHomeInfo },
	data() {
		return {
			loading: true,
			// items: [],
			// drawGroup: [],
			showInfoView: false,
			drawListId: '',
			countDown: boxExtractionTimeout
		};
	},
	created() {
		done = this.$loading();
	},
	mounted() {
		timer = setInterval(() => {
			if (this.countDown > 0) {
				this.countDown--;
			} else if (this.countDown <= 0 && show) {
				clearInterval(timer);
				uni.showModal({
					title: '温馨提示',
					content: '抽盒超时，请重新进入抽盒',
					showCancel: false,
					success: res => {
						this.$go('index/home', 'reLaunch');
					}
				});
			}
		}, 1000);
	},
	beforeDestroy() {
		clearInterval(timer);
		if (this.drawListId) {
			this.$log('解锁库存', this.drawListId);
			this.$api(_ => ['buy.unlock', this.drawListId]);
		}
	},
	computed: {
		headerImage() {
			return this.$websiteUrl + this.series.matrixHeaderImage;
		},
		productVolume() {
			return this.series.totalSize || this.series.productVolume || 12;
		},
		images() {
			return [this.$websiteUrl + this.series.longImage];
		},
		columnSize() {
			return this.series.columnSize || 0;
		},
		cellImage() {
			return this.$websiteUrl + this.series.matrixCellImage;
		},
		...mapState('current', {
			series: state => state.series,
			items: state => state.items,
			drawGroup: state => state.draws
		})
	},
	methods: {
		select(v, k) {
			this.$log('去吧，比卡丘', v, k);
			if (v.state) {
				this.$store.commit('current/setDraw', Object.assign({}, v));
				this.cancelBox(k, v);
			}
		},
		chooseIt() {
			this.$log('去吧，比卡丘,随机获取一个');
			if (this.drawGroup.length === 0) {
				uni.showModal({
					title: '温馨提示',
					content: '库存不足',
					showCancel: false,
					success: res => {
						this.$go('index/home', 'reLaunch');
					}
				});
				return;
			}

			let drawGroup = [].concat(this.drawGroup);
			const k = Math.floor(Math.random() * drawGroup.length);
			// let draw = drawGroup.splice(k, 1)[0];
			let draw = drawGroup[k];
			let index = 0;
			for (let v in this.items) {
				if (this.items[v].drawId === draw.drawId) {
					index = v;
					// this.items[v].state = false;
					// this.items[v].id = 0;
					// this.items[v].image = '/static/been-icon.png';
					break;
				}
			}
			this.cancelBox(index, draw);
		},
		cancelBox(index, draw) {
			// this.drawGroup = drawGroup;
			// this.$store.commit('current/setDraws', drawGroup);
			this.$store.commit('current/setDraw', draw);
			this.$go(
				'./buy?id=' +
					this.series.id +
					'&drawId=' +
					draw.drawId +
					'&index=' +
					index
			);
		},
		closeInfo() {
			this.showInfoView = false;
		},
		goLook() {
			this.$log('预览');
			this.showInfoView = true;
		},
		async getInfo(id = 0) {
			console.log('series', this.series, this.productVolume);
			if (!id) {
				id = this.series.id;
			}

			try {
				let group = await this.$api(_ => ['buy.lock', id]);
				this.drawListId = group.drawListId;
				let drawGroup = group.drawGroup;
				let items = [];
				let draws = [];
				for (let i in drawGroup) {
					items.push({
						image: this.cellImage,
						id: i,
						drawId: drawGroup[i].drawId,
						state: true,
						boxImage: drawGroup[i].boxImage,
						discountCouponCost: drawGroup[i].discountCouponCost,
						drawStatus: drawGroup[i].drawStatus,
						drawTime: drawGroup[i].drawTime,
						price: drawGroup[i].price,
						seriesId: drawGroup[i].seriesId,
						seriesName: drawGroup[i].seriesName,
						tipsCouponCost: drawGroup[i].tipsCouponCost,
						displayCouponCost: drawGroup[i].displayCouponCost
					});
					draws.push(drawGroup[i]);
				}
				let l = items.length;
				this.$log('当前有商品', l, this.productVolume);
				// this.drawGroup = [].concat(draws);
				this.$store.commit('current/setDraws', draws);
				if (l < this.productVolume) {
					for (let index = l; index < this.productVolume; index++) {
						items.push({
							image: '/static/been-icon.png',
							id: 0,
							state: false
						});
					}
				}

				const column = this.columnSize;
				// this.series.columnSize
				let pad = column - (items.length % column);
				if (column <= pad) {
					pad = 0;
				}

				this.$log(pad, column);
				// let pad2 = this.productVolume - drawGroup.length;
				// let pad2 = pad;
				// // let pad2 = soldOut - pad;
				// if (pad2 > 0) {
				// 	items = items.slice(pad2);
				// 	pad = pad + pad2;
				// }

				for (let i = 0; i < pad; i++) {
					items.push({
						image: '/static/been-icon.png',
						id: 0,
						state: false
					});
				}

				// this.items = shuffle(items);

				this.$store.commit('current/setItems', shuffle(items));
			} catch (e) {
				console.error('接口报错了', e);
				// this.$back();
				this.$nextTick(_ => {
					this.$alert('库存追加中，请稍等');
				});
			}
			setTimeout(() => {
				if (!!done) done();
				this.loading = false;
			});
		}
	},
	onShow() {
		show = true;
		// 检查是否有库存
		if (boxExtractionTimeout - 2 > this.countDown) {
			console.log('检查是否有库存', this.drawGroup.length);
			if (this.drawGroup.length <= 0) {
				this.countDown = boxExtractionTimeout;
				this.getInfo(infoID);
			}
		}
	},
	onHide() {
		show = false;
	},
	onLoad(data) {
		this.$log('data', data);
		infoID = data.id;
		// 后端没有提供根据 id 获取数据, 只能自己维护
		this.getInfo(infoID);
	}
};
</script>

<style lang="less">
@import 'info.less';
</style>