/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-23 08:51:28
 */

import Vue from 'vue';
import App from './App';
import Store from './store';
import cfg from './config';
import { ajax as Api } from './apis';
import appApi from './apis/util';
import util from './common/util';
import { __setMp } from './common/util';

Object.assign(Vue.prototype, util);

// #ifdef MP-WEIXIN
if (process.env.NODE_ENV === 'development') {
  uni.setEnableDebug({
    enableDebug: true
  });
}
// #endif

Vue.config.productionTip = false;
Vue.prototype.$websiteUrl = cfg.websiteUrl;
Vue.prototype.$store = Store;
Vue.prototype.$api = Api;
Vue.prototype.$eventHub = Vue.prototype.$eventHub || new Vue();
App.mpType = 'app';

const app = new Vue({
  components: {},
  async mounted() {
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
      this.$log(
        '过期时间|授权状态|远程用户信息',
        cfg.autoTimeout,
        Store.getters.authState,
        res
      );
      if (res) {
        const user = res;
        if (user.nickname || user.nickName) {
          if (cfg.autoTimeout === 0 && Store.getters.authState) {
            this.$log(
              '不用重新获取用户信息',
              JSON.stringify(Store.getters.userInfo)
            );
            return;
          }

          this.$store.commit(
            'USER_SIGNIN',
            Object.assign({}, Store.getters.userInfo, user)
          );
          // if (!Store.getters.authState) {
          //   this.$log('没有离线用户信息，需要更新');
          //   this.$store.dispatch('USER_SIGNIN', user);
          //   return;
          // }
          if (!user.update_time) {
            user.update_time = '';
            this.$log('我也不知道为什么更新时间都没有,需求文档写的需要的');
            await appApi.getUserAuthInfo();
            return;
          }
          const updateTime = new Date(user.update_time.replace(/-/g, '/'));
          const now = new Date().getTime();
          const diffTime = now - updateTime;
          this.$log('距离上次获取用户信息相隔: ' + diffTime / 1000 + 's');
          if (diffTime > cfg.autoTimeout) {
            this.$log('需要更新用户信息，重置授权状态');
            this.$store.commit('USER_SIGNIN', { authState: false });
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
  methods: {},
  ...App
});
__setMp(app);
app.$mount();
