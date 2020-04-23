/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-20 13:57:10
 */

let apiHost, websiteUrl;

// 售完的盒子数量
export const soldOut = 2;

export const posterCopywriting = '我是一句祝福的文案，运营请告诉我写什么！';

export const music = 'https://blindbox.fancier.store/images/Dribble2much.mp3';

let [appName, apiTimeout, autoTimeout] = ['王牌化身福盒机', 20000, 60]; //小程序默认名称, 接口超时时间ms, 重新授权获取用户信息间隔时间ms(0不会过期)

if (process.env.NODE_ENV !== 'development') {
  apiHost = 'https://blindbox.fancier.store'; // 接口域名
  websiteUrl = 'https://blindbox.fancier.store'; // 静态资源域名
} else {
  apiHost = 'https://blindbox.fancier.store'; // 测试接口域名
  websiteUrl = 'https://blindbox.fancier.store'; // 测试静态资源域名
}

// 微信订阅活动模块 ID
export const ActivityTmplids = ['EtU35HZ2aNqUGpLdHl5PBc9VV1P9tqD-bgCIIIXGjes'];

export default {
  websiteUrl,
  apiHost,
  appName,
  apiTimeout,
  autoTimeout,
};
