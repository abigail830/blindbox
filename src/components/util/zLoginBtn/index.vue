<!--
 * @Author: seekwe
 * @Date: 2020-03-16 11:46:28
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:17:15
 -->
<template>
	<button
		class="z-login-btn"
		:class="customClass"
		@click="click"
		@getuserinfo="getuserinfo"
		:open-type="btnOpenType"
	>
		<slot />
	</button>
</template>

<script>
export default {
	props: {
		customClass: {
			type: String,
			default: ''
		},
		openType: {
			type: String,
			default: 'getUserInfo'
		},
		authState: {
			type: Boolean,
			default: false
		}
	},
	computed: {
		btnOpenType() {
			return this.authState ? '' : this.openType;
		}
	},
	methods: {
		click() {
			console.log(this.authState, 'clickclickclickclickclickclickclickclick');
			this.$emit('click');
		},
		getuserinfo({ detail }) {
			console.log(detail);

			this.$emit('click', detail);
		}
	}
};
</script>

<style>
</style>