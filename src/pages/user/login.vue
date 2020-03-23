<!--
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:17:59
 -->
<template>
	<view class="page page-login">
		<open-data class="avatar" type="userAvatarUrl"></open-data>
		<!-- <view class="content">我们需要获取以下信息</view> -->
		<button class="login-btn" @getuserinfo="getuserinfo" open-type="getUserInfo">微信授权登录</button>
	</view>
</template>

<script>
import { themeColor } from '@/common/var';
import { mapState, mapGetters } from 'vuex';
export default {
	data() {
		return {};
	},
	computed: {
		...mapGetters(['userInfo'])
	},
	created() {
		// uni.setNavigationBarColor({
		// 	frontColor: '#515151',
		// 	backgroundColor: '#ebebeb',
		// 	// backgroundColor: themeColor,
		// 	animation: {
		// 		duration: 400,
		// 		timingFunc: 'easeIn'
		// 	}
		// });
	},
	mounted() {},
	methods: {
		getuserinfo(e) {
			const { errMsg, userInfo } = e.detail;
			if (errMsg === 'getUserInfo:ok') {
				this.$log('登录成功', userInfo, e);
				// this.$store.commit('USER_SIGNIN', userInfo);
			}
			this.$back();
		}
	}
};
</script>

<style lang='less'>
@import '../../common/var.less';
.page-login {
	background-color: #ebebeb;
	// background: @themeColor;
	color: @fontColor;
	text-align: center;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding-bottom: 30vh;
}
.avatar {
	border-radius: 100%;
	width: 150rpx;
	display: block;
	height: 150rpx;
	overflow: hidden;
	margin-bottom: 80rpx;
}
.login-btn {
	width: 300rpx;
	color: #fff;
	font-size: 38rpx;
	background: @themeColor;
}
</style>
