<!--
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-14 11:35:21
 -->
<script>
import { music, shakeMusic } from './config';
import { $alert } from '@/common/util';
const innerAudioContext = uni.createInnerAudioContext();
// const innerAudioContext = uni.getBackgroundAudioManager();
innerAudioContext.autoplay = true;
innerAudioContext.loop = true;
// innerAudioContext.src = '/static/music.mp3';
innerAudioContext.src = music;
innerAudioContext.onPlay(() => {
	console.log('开始播放');
});
innerAudioContext.onPause(() => {
	console.log('暂停播放');
});
innerAudioContext.onError(res => {
	console.error(res.errMsg, res.errCode);
	uni.showModal({
		title: '背景音乐播放失败',
		content: res.errCode + ': ' + res.errMsg,
		success: function(res) {
			if (res.confirm) {
			} else if (res.cancel) {
			}
		}
	});
});

import cfg from './config';
import appApi from './apis/util';
import { mapState, mapGetters } from 'vuex';
export default {
	onLaunch() {},
	onShow() {
		this.musciPlay();
		this.getProductsRoles();
	},
	onHide() {
		this.musciPause();
	},
	async mounted() {
		console.warn('全部播放音乐');
		this.musci();
		await this.checkAuth();
		uni.downloadFile({
			url: shakeMusic,
			success: res => {
				if (res.statusCode === 200) {
					this.$store.commit('system/setShakeMusic', res.tempFilePath);
				}
			}
		});
		// uni.request({
		// 	header: {
		//   'content-type': 'audio/mpeg'
		// },
		// 	url: shakeMusic,
		// 	success: res => {
		// 		this.$log('摇一摇音乐加载成功');
		// 	}
		// });
	},
	watch: {
		soundEffects: {
			handler: function(val, oldVal) {
				if (val) {
					this.musciPlay();
				} else {
					this.musciPause();
				}
			},
			immediate: true
		}
	},
	computed: {
		soundEffects() {
			return this.system.soundEffects;
		},
		...mapState(['system'])
	},
	methods: {
		musciPlay() {
			if (this.soundEffects) {
				this.$log('播放背景音乐');

				innerAudioContext.play();
			} else {
				this.$log('不播放背景音乐');
			}
		},
		musciPause() {
			this.$log('暂停背景音乐');

			innerAudioContext.pause();
		},
		musci() {
			// const innerAudioContext = uni.getBackgroundAudioManager();
		},
		async checkAuth() {
			await appApi.checkSession();
			try {
				let res = await appApi.getUserInfo();
				this.$log('访问验证', res);
				if (!res || !!res.errorMsg) {
					this.$alert('获取用户信息失败，请关闭重新打开', 10000 * 100, null, {
						mask: true
					});

					this.$log('获取到用户信息失败,需要手动登录');
					return;
				}
				if (res) {
					const user = res;
					if (user.nickname || user.nickName) {
						if (cfg.autoTimeout === 0 && this.$store.getters.authState) {
							this.$log(
								'不用重新获取用户信息',
								JSON.stringify(this.$store.getters.userInfo)
							);
							return;
						}

						this.$store.commit(
							'USER_SIGNIN',
							Object.assign({}, this.$store.getters.userInfo, user)
						);
						// if (!this.$store.getters.authState) {
						//   this.$log('没有离线用户信息，需要更新');
						//   this.$store.dispatch('USER_SIGNIN', user);
						//   return;
						// }
						if (!user.lastLoginDate) {
							user.lastLoginDate = '';
							await appApi.getUserAuthInfo();
							return;
						}
						const updateTime = new Date(user.lastLoginDate); //.replace(/-/g, '/')
						const now = new Date().getTime();
						const diffTime = now - updateTime;
						this.$log('距离上次获取用户信息相隔: ' + diffTime / 1000 + 's');
						if (diffTime > cfg.autoTimeout) {
							this.$log('需要更新用户信息，重置授权状态');
							// this.$store.commit('USER_SIGNIN', { authState: false });
							await appApi.getUserAuthInfo();
						}
					} else {
						this.$log('用户信息没有授权状态');
						this.$store.dispatch('USER_SIGNIN', { authState: false });
					}
					return;
				}
				const isBan = res.code === 402;
				if (isBan) {
					this.$store.dispatch('USER_SIGNIN', { banState: true });
				}
				let duration = util.$PLATFORM === 'h5' || isBan ? 9999999 : null;
				uni.hideTabBar({ animation: true });
				if (util.$PLATFORM === 'h5') {
					this.$log('h5 需要独立的登录页面');
				}
				this.$alert(res.errorMsg, duration, null, {
					mask: true
				});
			} catch (e) {
				console.error(e);
			}
		},
		getProductsRoles() {
			this.$api('products.roles').then(e => {
				let data = e.map(e => {
					let data = Object.assign(
						{
							id: 0,
							image: cfg.websiteUrl + e.roleImage,
							title: e.name
						},
						e
					);
					return data;
				});
				this.$store.commit('series/setSeriesData', data);
			});
		}
	}
};
</script>

<style lang="less">
@import './common/app.less';
.demo {
	position: absolute;
	width: 750rpx;
	left: 0;
	top: 200rpx;
	top: 0;
	z-index: 11111111111111;
	background: #000;
}
</style>
