<template>
	<view class="lucky-view">
		<zShow />

		<button class="btn" @click="goWobble">抽一盒</button>
	</view>
</template>

<script>
import zShow from '@/components/box/zShow';
export default {
	components: { zShow },
	methods: {
		goWobble() {
			this.$go('./wobble');
		}
	}
};
</script>

<style lang="less">
@import 'lucky.less';
</style>