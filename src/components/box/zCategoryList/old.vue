<!--
 * @Author: seekwe
 * @Date: 2020-03-11 17:52:46
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:15:46
 -->
<template>
	<view class="z-category-list-view-box">
		<view class="left">
			<scroll-view :scroll-y="true" :style="{'height':scrollHeight }">
				<view
					class="item"
					v-for="(item,index) in leftArray"
					:key="index"
					:class="{'active':index==leftIndex }"
					:data-index="index"
					@click="leftTap"
				>{{item}}</view>
			</scroll-view>
		</view>
		<view class="main">
			<scroll-view
				scroll-y="true"
				:style="{'height':scrollHeight }"
				@scroll="mainScroll"
				:scroll-into-view="scrollInto"
				:scroll-with-animation="animation"
			>
				<view class="item" v-for="(item,index) in mainArray" :key="index" :id="'item-'+index">
					<view class="title">
						<view>{{item.title}}</view>
					</view>
					<view class="goods" v-for="(item2,index2) in item.list" :key="index2">
						<view>-----</view>
						<view>
							<view>明星标题{{index2+1}}</view>
							<view class="describe">明星标题描述内容</view>
							<view class="money">明星标题的价格</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			patchSize: 0,
			scrollHeight: '500px',
			leftArray: [],
			mainArray: [],
			topArr: [],
			leftIndex: 0,
			scrollInto: '',
			animation: true
		};
	},
	created() {
		let topHeight = this.$to.rpx2px(200);
		this.patchSize = topHeight;
		this.scrollHeight = `${this.$systems.windowHeight - topHeight}px`;
	},
	mounted() {
		setTimeout(this.getListData, 0);
	},
	watch: {
		leftIndex(n) {
			this.animation = true;
		}
	},
	methods: {
		getListData() {
			let [left, main] = [[], []];

			for (let i = 0; i < 10; i++) {
				left.push(`系列${i + 1}`);

				let list = [];
				for (let j = 0; j < i + 1; j++) {
					list.push(j);
				}
				main.push({
					title: `明星系列标题${i + 1}`,
					list
				});
			}
			this.leftArray = left;
			this.mainArray = main;

			this.$nextTick(() => {
				this.getElementTop();
			});
		},
		getElementTop() {
			let p_arr = [];
			let new_p = selector => {
				return new Promise((resolve, reject) => {
					let view = uni
						.createSelectorQuery()
						.in(this)
						.select(selector);
					view
						.boundingClientRect(data => {
							resolve(data.top);
						})
						.exec();
				});
			};

			this.mainArray.forEach((item, index) => {
				p_arr.push(new_p(`#item-${index}`));
			});
			Promise.all(p_arr).then(data => {
				this.topArr = data;
			});
		},
		mainScroll(e) {
			let top = e.detail.scrollTop;
			let index = 0;
			for (let i = this.topArr.length - 1; i >= 0; i--) {
				if (top + 2 >= this.topArr[i] - this.patchSize) {
					index = i;
					break;
				}
			}
			this.leftIndex = index < 0 ? 0 : index;
		},
		leftTap(e) {
			let index = e.currentTarget.dataset.index;
			this.animation = false;
			this.$nextTick(_ => (this.scrollInto = `item-${index}`));
		}
	}
};
</script>

<style lang='less'>
@import 'old.less';
</style>