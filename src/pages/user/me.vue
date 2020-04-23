<!--
 * @Author: seekwe
 * @Date: 2020-03-01 20:59:35
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-16 19:21:49
 -->
<template>
	<view class="page page-me">
		<view class="header" v-if="!!nickName">
			<view class="username">
				<zLoginBtn @click="clickLoginBtn" :authState="false" customClass="avatar">
					<img class="avatar" mode="widthFix" :src="userInfo.avatarUrl" />
				</zLoginBtn>
				<view class="username-box">
					<view class="nickname">{{nickName}}</view>
					<view class="userid">会员号: {{userInfo.id}}</view>
				</view>

				<image
					class="service-image"
					@click="show('service')"
					src="/static/service-icon.png"
					mode="widthFix"
				/>
			</view>
			<view class="integral">
				<view class="integral-title">积分</view>
				<view class="integral-value">{{bonus}}</view>
			</view>
			<view class="header-list">
				<view class="page-list-item" @click="show('order')">
					<view class="page-list-icon">
						<image mode="widthFix" src="/static/order-icon.png" class="order-icon" />
					</view>订单
					<view class="page-list-item_right">
						<view class="page-list-item-arrow"></view>
					</view>
				</view>
				<view class="page-list-item" @click="show('address')">
					<view class="page-list-icon">
						<image mode="widthFix" src="/static/lbs-icon.png" class="lbs-icon" />
					</view>收货地址
					<view class="page-list-item_right">
						<view class="page-list-item-arrow"></view>
					</view>
				</view>
			</view>
		</view>
		<view class="header" v-else>
			<button
				class="login-btn"
				@--click="show('login')"
				@getuserinfo="getuserinfo"
				open-type="getUserInfo"
			>点击登录</button>
		</view>
		<view class="page-me-list">
			<view class="page-list-item" @click="show('agreement')">
				使用协议
				<view class="page-list-item_right">
					<view class="page-list-item-arrow"></view>
				</view>
			</view>
			<view class="page-list-item" @click="show('rule')">
				规则
				<view class="page-list-item_right">
					<view class="page-list-item-arrow"></view>
				</view>
			</view>
			<view class="page-list-item">
				音效
				<view class="page-list-item_right">
					<switch @change="changeSoundEffects" :checked="system.soundEffects" />
				</view>
			</view>
			<view class="page-list-item">
				弹幕
				<view class="page-list-item_right">
					<switch @change="changeBarrage" :checked="system.barrage" />
				</view>
			</view>
		</view>
		<view class="help-view" v-show="showHelp">
			<view class="help-box">
				<view class="help-title">{{help.title}}</view>
				<scroll-view :scroll-y="true" class="help-center">
					<zParser :html="help.content" />
				</scroll-view>
				<view class="close-icon-box" @click="help={}">
					<image class="close-image" src="/static/universal-icon.png" mode />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import zParser from '@/components/util/zParse';
import zLoginBtn from '@/components/util/zLoginBtn';
import { onShareAppMessage } from '@/common/mixins';
import { help } from './me';

export default {
	components: { zParser, zLoginBtn },
	data() {
		return {
			help: {}
		};
	},
	computed: {
		showHelp() {
			return typeof this.help === 'object' && !!this.help.title;
		},
		nickName() {
			return this.userInfo.nickName || '';
		},
		bonus() {
			return this.userInfo.bonus;
		},
		...mapState(['system']),
		...mapGetters(['userInfo', 'authState', 'banState'])
	},
	mounted() {
		setTimeout(_ => {
			this.$log('page-me', this.userInfo);
		}, 2000);
		setTimeout(_ => {
			this.$log('page-me', this.userInfo, this.system, mapState(['system']));
		}, 15000);
	},
	methods: {
		clickLoginBtn(e) {
			console.log('clickLoginBtn', e);
		},
		async getuserinfo(e) {
			await this.$getuserinfo(e.detail);
		},
		show(type) {
			this.$log(type);
			switch (type) {
				case 'login':
					this.$go('./login');
					break;
				case 'order':
					this.$go('./order');
					break;
				case 'address':
					this.$go('./address');
					break;
				default:
					if (typeof help[type]) {
						this.help = help[type];
					} else {
						this.help = {};
					}
			}
		},
		changeSoundEffects({ detail }) {
			this.$store.commit('system/changeSoundEffects', detail.value);
		},
		changeBarrage({ detail }) {
			this.$store.commit('system/changeBarrage', detail.value);
		}
	}
};
</script>

<style lang='less'>
@import 'me.less';
</style>