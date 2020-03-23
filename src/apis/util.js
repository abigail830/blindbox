/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:14:48
 */
import { ajax } from './index';
import { getToken } from './index';
import { fly } from './index';
import $store from '../store';
import util from '@/common/util';

export default {
  async checkSession() {
    let err = null;
    const token = $store.getters.token;
    const authStatus = $store.getters.authStatus;
    if (!token || !authStatus) {
      fly.lock();
    }
    if (!token) {
      util.$log('token 都没有，需要再次获取');
      // token 都没有，需要再次获取
      err = await getToken(
        _ => {
          util.$log('session_key 已重新获取');
        },
        _ => {}
      );
    } else if (!authStatus) {
      // 本地还没获取过用户信息，需要保证 session_key 有效期
      wx.checkSession({
        async success() {
          util.$log('session_key 未过期');
          fly.unlock();
        },
        async fail() {
          util.$log('session_key 已经失效');
          err = await getToken(
            _ => {
              util.$log('session_key 已重新获取');
            },
            _ => {}
          );
        }
      });
    }
    if (err != null) {
      console.error(err);
    }
  },
  async getLoginCode() {
    return await new Promise((resolve, reject) => {
      wx.login({
        success(res) {
          if (res.code) {
            resolve(res.code);
          }
        }
      });
    });
  },
  async getUserInfo() {
    return await ajax('user.info');
  },
  async getUserAuthInfo() {
    const [res, err] = await util.$awaitWrap(util.$app.getSetting());
    if (err === null && res.authSetting['scope.userInfo']) {
      uni.getUserInfo({
        success(infoRes) {
          util.$log('获取最新用户资料', infoRes);
          ajax('user.update', infoRes.userInfo, {
            headers: { iv: infoRes.iv, encryptedData: infoRes.encryptedData }
          }).then(e => {
            $store.dispatch(
              'USER_SIGNIN',
              Object.assign({ authState: true }, infoRes.userInfo)
            );
          });
        }
      });
    } else {
      util.$log('getauthSetting', res, err);
    }
  }
};
