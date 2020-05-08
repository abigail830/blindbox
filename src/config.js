/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-05-07 11:33:01
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

// 购买盒子页面底部提示文案
export const buyBootomTips = '我是一段温馨的提示哦。我是一段温馨的提示哦。。';

// 卡券说明文案 content 支持 html
export const CardDescription = {
  tips: {
    title: '提示卡说明',
    type: 'tip',
    content: '没什么好说明的',
  },
  display: {
    type: 'display',
    title: '显示卡说明',
    content: '没什么好说明的',
  },
  discount: {
    type: 'discount',
    title: '优惠卡说明',
    content: '没什么好说明的',
  },
};

// 我的页面规则文案 content 支持 html
export const RuleDescription = {
  agreement: {
    title: '使用协议',
    content: `我是协议的啦,你想写什么<br>
这是测试的啦!!!!
`,
  },
  service: {
    title: '联系客服',
    content: `<p style="text-align: center;">
QQ: 378223838<br><br>
电话: 15976566645<br><br>
微信: seekwe<br><br>
<br>
<img style="max-width:100%;" src="https://wx.qlogo.cn/mmopen/vi_32/icXdI0QO7eV1m1T5dUHofNTfBShe30JXriaKTQUPLh0uz6q5xFERee9I65VCaxibIVGAm9xvsRAY4LUJL82L7fdibg/132" />
</p>`,
  },
  rule: {
    title: '规则',
    content: `我的规则,<b>你想</b>写什么<br>
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试的啦我的规则,你想写什么
这是\n 测试我的规则,你想写什么
这是\n 测试的啦
`,
  },
};

export default {
  websiteUrl,
  apiHost,
  appName,
  apiTimeout,
  autoTimeout,
};
