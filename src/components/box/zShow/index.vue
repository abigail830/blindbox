<!--
 * @Author: seekwe
 * @Date: 2020-03-02 16:19:39
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:17:05
 -->
<template>
	<div class="z-show-view">
		<view>
			<image class="z-show-image" :src="image" mode="widthFix" />
		</view>
		<view class="z-show-items">
			<view class="z-show-item" v-for="(v,k) in 12" :key="k">{{v}}</view>
		</view>
	</div>
</template>

<script>
export default {
	data() {
		return {
			image: 'https://127.0.0.1/s.jpg'
		};
	}
};
</script>

<style lang="less">
@import 'index.less';
</style>