/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:19:36
 */

import Vue from 'vue';
import util from '@/common/util';

export const SET_TOKEN = (state, token) => {
  state.token = token;
  uni.setStorage({
    key: 'token',
    data: token
  });
};
export const USER_SIGNIN = (state, user) => {
  util.$log('更新用户信息', user);
  const nickname = user.nickName || user.nickName;
  if (nickname) {
    user.authState = true;

    if (nickname === ' ') {
      user.nickName = '用户名';
    }
  }
  console.log(nickname, nickname, nickname);

  if (!user.banState) {
    user.banState = false;
  }
  Object.assign(state.user, user);
  uni.setStorage({
    key: 'user',
    data: Object.assign({}, state.user, { banState: false })
  });
};

export const USER_SIGNOUT = state => {
  Object.keys(state.user).forEach(k => Vue.delete(state.user, k));
};
