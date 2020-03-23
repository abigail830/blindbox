/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:14:19
 */

import qs from 'qs';
import cfg from '../config';

export const onShareAppMessage = function(res) {
  // res.from === 'button' // 来自页面内分享按钮
  // getShareAppMessageConfig 返回格式: {title: "标题", path: "路径", imageUrl: "图片", param: "参数,支持对象或字符串"}
  // noinspection JSUnresolvedVariable
  const getCfg = this.getShareAppMessageConfig;
  const url = this.$mp.page.route;
  let data = {
    title: cfg.appName,
    path: '/' + url
  };

  if (typeof getCfg === 'function') {
    let config = getCfg(res);
    if (this.$is.object(config)) {
      const paramType = typeof config.param;
      if (paramType === 'object') {
        data.path = data.path + '?' + qs.stringify(config.param);
      } else if (paramType === 'string') {
        data.path = data.path + '?' + config.param;
      }
      Object.assign(data, config);
    }
  }
  this.$log('分享信息', data);
  return data;
};
