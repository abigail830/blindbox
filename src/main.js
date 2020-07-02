/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-19 12:42:31
 */

import Vue from 'vue';
import App from './App';
import Store from './store';
import cfg from './config';
import { ajax as Api } from './apis';
import util from './common/util';
import { __setMp } from './common/util';
// import * as Sentry from './common/sentry';

// Sentry.init({
//   dsn:
//     'https://305cce999f6944e881b6e01596413035@o408543.ingest.sentry.io/5282481',
// });

Object.assign(Vue.prototype, util);

// #ifdef MP-WEIXIN
if (process.env.NODE_ENV === 'development') {
  uni.setEnableDebug({
    enableDebug: true,
  });
}
// #endif

Vue.config.productionTip = false;
Vue.prototype.$websiteUrl = cfg.websiteUrl;
Vue.prototype.$store = Store;
Vue.prototype.$api = Api;
Vue.prototype.$getuserinfo = async function(e) {
  const { errMsg, userInfo, iv, encryptedData } = e;
  if (errMsg === 'getUserInfo:ok') {
    this.$log('登录成功', userInfo);
    let res = await this.$api(
      'user.update',
      {},
      { headers: { iv: iv, encryptedData: encryptedData } }
    );
    let old = Store.getters.userInfo;
    this.$store.commit(
      'USER_SIGNIN',
      Object.assign({ authState: true }, old, this.userInfo, userInfo)
    );
  }
};

Vue.prototype.$eventHub = Vue.prototype.$eventHub || new Vue();
App.mpType = 'app';

const app = new Vue({
  ...App,
});
__setMp(app);
app.$mount();
