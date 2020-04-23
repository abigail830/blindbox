<!--
 * @Author: seekwe
 * @Date: 2020-03-10 21:11:30
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-11 20:24:11
 -->
<template>
	<view class="page page-info">
		<view class="page-info-content">
			<image :src="image" mode="aspectFit" class="content-image" v-if="image" />
			<zParser :html="centent" />
		</view>
		<form demo-submit="formSubmit">
			<button formType="submit" @click="formSubmit" class="page-info-btn">活动开始提醒我</button>
		</form>
	</view>
</template>

<script>
import zParser from '@/components/util/zParse';
import { ActivityTmplids } from '@/config';
import { onShareAppMessage } from '@/common/mixins';
export default {
	onShareAppMessage: onShareAppMessage,
	components: { zParser },
	data() {
		return {
			id: 0,
			info: {}
		};
	},
	onLoad(opt) {
		this.$log(opt);
		this.id = opt.id;
		this.getInfo();
	},
	computed: {
		centent() {
			return this.info.activityDescription;
		},
		image() {
			return this.$websiteUrl + this.info.mainImgUrl;
		}
	},
	methods: {
		getShareAppMessageConfig() {
			this.$log('发起分享');
			this.$api(_ => {
				return ['user.shareActivity', this.id];
			});
			return {
				title: this.info.activityName,
				path: '/pages/activity/info',
				imageUrl: this.image,
				param: 'id=' + this.id
			};
		},
		getInfo() {
			this.$api(_ => {
				return ['activities.get', this.id];
			}).then(e => {
				this.info = e;
			});
		},
		formSubmit({ detail }) {
			uni.requestSubscribeMessage({
				tmplIds: ActivityTmplids,
				complete: e => {
					this.$log(e);
				},
				fail: e => {
					if (
						e.errMsg ==
						'requestSubscribeMessage:fail 开发者工具暂时不支持此 API 调试，请使用真机进行开发'
					) {
						this.$back();
					} else {
						this.$alert(e.errMsg);
					}
				},
				success: async e => {
					await this.$api(_ => {
						return [
							'activities.notice',
							this.id,
							'/pages/activity/info?id=' + this.id
						];
					});
					this.$back();
				}
			});
		}
	}
};
</script>

<style lang="less">
@import '../../common/var.less';

.page-info-btn {
	background-color: @themeColor;
	color: #fff;
	width: 600rpx;
	height: 82rpx;
	position: static;
	position: fixed;
	margin: auto;
	left: 0;
	right: 0;
	font-size: 35.6rpx;
	line-height: 82rpx;
	bottom: 10vh;
	border-radius: 50rpx;
}
.page-info-content {
	padding: 20rpx 50rpx;
	margin-bottom: 9vh;
}
.content-image {
	max-width: 100%;
	height: 367rpx;
}
</style>