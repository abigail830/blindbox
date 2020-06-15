<!--
 * @Author: seekwe
 * @Date: 2020-03-11 13:03:12
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-05-20 11:31:18
 -->
<template>
	<view class="page page-order">
		<view class="tabs">
			<view
				@click="switchTab('ing')"
				:class="{'tab_on':isDone}"
				class="tab"
			>已发货</view>
			<view
				@click="switchTab('wait')"
				:class="{'tab_on':isWait}"
				class="tab"
			>待发货</view>
			<view
				@click="switchTab('no')"
				:class="{'tab_on':isNot}"
				class="tab"
			>未申请</view>
		</view>
		<view
			class="page-order-box"
			v-show="isDone"
		>
			<view
				@click="show(v,k)"
				class="page-order-items"
				v-for="(v,k) in itemsData1"
				:key="k"
			>
				<image
					class="page-order-image"
					:src="padImageURL(v.productImage)"
					mode="aspectFill"
				/>
				<view class="page-order-info">
					<view class="title">{{v.productName}}</view>
					<view class="time">{{v.createTime}}</view>
				</view>
			</view>
		</view>
		<view
			class="page-order-box"
			v-show="isWait"
		>

			<view
				@click="show(v,k)"
				class="page-order-items"
				v-for="(v,k) in itemsData2"
				:key="k"
			>
				<image
					class="page-order-image"
					:src="padImageURL(v.productImage)"
					mode="aspectFill"
				/>
				<view class="page-order-info">
					<view class="title">{{v.productName}}</view>
					<view class="time">{{v.createTime}}</view>
				</view>
			</view>
		</view>
		<view
			class="page-order-box"
			v-show="isNot"
		>
			<view class="not-order-tip">满3个包邮（不包含 0 元商品、偏远地区除外）</view>
			<view
				class="page-order-items"
				v-for="(v,k) in itemsData3"
				:key="k"
			>
				<image
					class="page-order-image"
					:src="padImageURL(v.productImage)"
					mode="aspectFill"
				/>
				<view class="page-order-info">
					<view class="title">{{v.productName}}</view>
					<view class="time">{{v.createTime}}</view>
				</view>
				<label
					class="order-checked"
					v-if="!isDone"
					@click.stop="switchChecked(v)"
				>
					<checkbox
						value="cb"
						:checked="isChecked(v) >= 0"
					/>
				</label>
			</view>
		</view>
		<zOrderInfo
			:info="info"
			@close="close"
			:isShow="isShow"
		/>
		<view
			@click="goDeliverGoods"
			v-if="showChecked"
			class="show-checked-btn"
		>点击发货</view>
	</view>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import { zOrderInfo } from '@/components/box/zOrderInfo';
import { lightGreyPageBackground } from '@/common/var';
import order from './order';
export default {
	components: { zOrderInfo },
	onPullDownRefresh() {
		this.$log('刷新');
		this.itemsPage = 1;
		this.getAll();
	},
	onShow() {
		this.getAll();
	},
	onReachBottom() {
		this.itemsPage++;
		this.$log('下一页', this.itemsPage);
		this.getAll();
	},
	data() {
		return Object.assign(
			{
				// tabName: 'no',
				tabName: 'ing',
				itemsPage: 1,
				info: {},
				itemsData: [],
				checkedArr: [],
				checkedArrInfo: []
			},
			order.data
		);
	},
	computed: {
		showChecked() {
			return !this.isDone && this.checkedArr.length > 0;
		},
		isDone() {
			return this.tabName === 'ing';
		},
		isWait() {
			return this.tabName === 'wait';
		},
		isNot() {
			return this.tabName === 'no';
		},
		isShow() {
			return JSON.stringify(this.info) !== '{}';
		},
		...mapState('current', { currentGoodsData: state => state.goods })
	},
	watch: {
		tabName() {
			this.$log('切换菜单吧');
		}
	},
	created() {
		// uni.startPullDownRefresh();
		// this.getAll();
		// this.getItems();
	},
	methods: Object.assign(
		{
			padImageURL(url) {
				return this.$websiteUrl + url;
			},
			goDeliverGoods() {
				// 先获取商品详细数据吧
				// 把要发货的商品缓存吧
				this.$store.commit('current/setGoodsData', this.checkedArrInfo);
				this.$log('我要发货呀', this.checkedArr);
				this.$log('我要发货呀', this.checkedArrInfo);
				this.$log('我要发货呀', this.checkedArrInfo);
				this.$go('./post');
			},
			switchTab(name) {
				this.tabName = name;
			},
			close() {
				this.info = {};
			},
			show(v, k) {
				this.$log('show', v);
				this.info = v;
			},
			switchChecked(v) {
				const id = v.orderId;
				let index = this.isChecked(v);
				if (index >= 0) {
					this.checkedArr.splice(index, 1);
					this.checkedArrInfo.splice(index, 1);
				} else {
					this.checkedArr.push(id);
					this.checkedArrInfo.push(v);
				}
			},
			isChecked(v) {
				return this.checkedArr.indexOf(v.orderId);
			},
			getItems() {
				// let itemsData = [];
				// let done = this.$loading();
				// this.$api('order.pendingPay');
				// for (let i = 0; i < 10; i++) {
				// 	itemsData.push({
				// 		image: '/static/demo.png',
				// 		id: i,
				// 		title: `产品名称-${this.itemsPage}-${i}`,
				// 		type: '[抽盒]',
				// 		mumber: 1,
				// 		createTime: '2020-03-11 13:14:19'
				// 	});
				// }
				// setTimeout(_ => {
				// 	if (this.itemsPage > 1) {
				// 		this.itemsData = this.itemsData.concat(itemsData);
				// 	} else {
				// 		this.itemsData = itemsData;
				// 	}
				// 	// this.info = itemsData[0];
				// 	uni.stopPullDownRefresh();
				// 	done();
				// }, 1000);
			}
		},
		order.methods
	)
};
</script>

<style lang="less">
@import 'order.less';
</style>