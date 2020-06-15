<!--
 * @Author: seekwe
 * @Date: 2020-03-10 21:11:30
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-05-28 19:12:20
 -->
<template>
	<view class="page page-info">
		<view class="page-info-content">
			<image
				:src="image"
				mode2="aspectFit"
				mode="widthFix"
				class="content-image"
				v-if="image"
			/>
			<!-- <zParser :html="centent" /> -->
		</view>
		<form demo-submit="formSubmit">
			<button
				v-if="state===0"
				formType="submit"
				@click="formSubmit"
				class="page-info-btn"
			>活动开始提醒我</button>
			<button
				v-if="state===1"
				class="page-info-btn page-info-btn-ban "
			>预约成功</button>
			<button
				v-if="state===2"
				class="page-info-btn page-info-btn-ban "
			>活动已过期</button>
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
			info: {},
			state: 0
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
			return this.$websiteUrl + (this.info.contentImgUrl||this.info.ContentImgUrl||this.info.mainImgUrl);
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
			}).then(async e => {
				this.info = e;
				try {
					const end =
						e.activityStartDate &&
						+new Date(e.activityStartDate) - +new Date() < 0;
						
					if (end) {
						this.state = 2;
						console.error("结束了");
						return;
					}

					let data = await this.$api(() => [
						'activities.acceptNotify',
						this.id
					]);
					this.state = !!data.register?1:0;
				} catch (e) {}
			});
		},
		ok() {
			this.state = 1;
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
						this.ok();
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
					this.ok();
					// this.$back();
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
	bottom: 8vh;
	border-radius: 50rpx;
	&.page-info-btn-ban {
		background-color: #ebebeb;
	}
}
// .page-info-content {
// 	// padding: 20rpx 50rpx;
// 	// margin-bottom: 10vh;
// }
.content-image {
	max-width: 100%;
	width: 750rpx;
	// height: 367rpx;
	display: block;
}
</style>