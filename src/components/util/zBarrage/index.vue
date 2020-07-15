
<template>
	<view :style="{'position: fixed':fixed}" class="zbarrage-view">
		<block v-for="(item,k) in items" :key="k">
			<text v-if="item.display" class="aon" :style="item.style">
				<text class="aon-text">{{item.text}}</text>
			</text>
		</block>
	</view>
</template>

<script>
let lists = [];
let id = 0;
let topRange = [];
export default {
	name: 'zbarrage',
	data() {
		return {
			items: [],
			id: 0
		};
	},
	props: {
		topRange: {
			type: Array,
			default() {
				return [];
			}
		},
		fixed: {
			type: Boolean,
			default: true
		},
		closeRandomColor: {
			type: Boolean,
			default: false
		}
	},
	watch: {
		topRange: {
			immediate: true,
			handler(n, o) {
				this.setTopRange();
			}
		}
	},
	mounted() {
		let cycle = setInterval(() => {
			if (lists.length <= 0) return;
			let t = +new Date();
			let runSum = 0;
			lists.forEach(e => {
				if (e.display) {
					if (e.endTime < t) {
						e.display = false;
					} else {
						runSum++;
					}
				}
			});
			if (runSum === 0) {
				this.clear();
			}
		}, 10000);
		this.$once('hook:beforeDestroy', _ => {
			clearInterval(cycle);
		});
		this.items = lists;
	},
	methods: {
		setTopRange() {
			topRange = [].concat(
				this.topRange.sort(function(a, b) {
					return Math.random() - 0.5;
				})
			);
		},
		clear() {
			lists = [];
			this.items = lists;
			this.$emit('clear', {});
		},
		randTop() {
			if (this.topRange.length > 0) {
				if (topRange.length === 0) {
					this.setTopRange();
				}
				return topRange.shift();
			}
			return Math.ceil(Math.random() * 100);
		},
		add(
			text = '',
			top = this.randTop(),
			time = Math.random() * (10 - 3) + 3,
			color = this.getRandomColor()
		) {
			if (typeof top === 'number') {
				top = top + '%';
			}

			lists.push(this.itemData(text, top, time, color));
		},
		itemData(text, top, time, color) {
			id++;
			let style = `animation: first ${time}s linear forwards;top:${top};color:${color};right:0;`;
			 style += `-webkit-animation: first ${time}s linear forwards;top:${top};color:${color};`;
			return {
				id,
				text,
				top,
				time,
				color,
				style,
				endTime: +new Date() + time * 1000,
				display: true
			};
		},
		getRandomColor() {
			if (this.closeRandomColor) return '';
			let rgb = [];
			for (let i = 0; i < 3; ++i) {
				let color = Math.floor(Math.random() * 256).toString(16);
				color = color.length == 1 ? '0' + color : color;
				rgb.push(color);
			}
			return '#' + rgb.join('');
		}
	}
};
</script>

<style lang="less">
@import 'index.less';
</style>