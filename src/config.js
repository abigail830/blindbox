/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 15:03:07
 */

let apiHost, websiteUrl;
let [appName, apiTimeout, autoTimeout] = ['盲盒', 20000, 0]; //小程序默认名称, 接口超时时间ms, 重新授权获取用户信息间隔时间ms(0不会过期)

if (process.env.NODE_ENV !== 'development') {
  apiHost = 'http://47.104.146.206'; // 接口域名
  websiteUrl = 'http://47.104.146.206'; // 静态资源域名
} else {
  apiHost = 'http://47.104.146.206'; // 测试接口域名
  websiteUrl = 'http://47.104.146.206'; // 测试静态资源域名
}

export default {
  websiteUrl,
  apiHost,
  appName,
  apiTimeout,
  autoTimeout
};
