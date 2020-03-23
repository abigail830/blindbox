<template>
	<div class="wobble-view">
		<zWobble ref="wobble" />
		<button @click="wobbleStart">模拟摇晃</button>
	</div>
</template>

<script>
import zWobble from '@/components/box/zWobble';
export default {
	components: { zWobble },
	mounted() {
		this.$refs.wobble.wobbleStart();
	},
	methods: {
		wobbleStart() {
			this.$refs.wobble.wobbleStart();
		}
	}
};
</script>

<style lang="less">
</style>