/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-16 19:27:22
 */

import Vue from 'vue';
import util from '@/common/util';

export const SET_TOKEN = (state, token) => {
  state.token = token;
  uni.setStorage({
    key: 'token',
    data: token,
  });
};
export const USER_SIGNIN = (state, user) => {
  const nickname = user.nickName || user.nickName;
  if (nickname) {
    user.authState = true;

    if (nickname === ' ') {
      user.nickName = '用户名';
    }
  }
  if (!user.banState) {
    user.banState = false;
  }
  if (
    user.avatarUrl ===
      'https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoFHUBxzOaItdlaIftJBqA7oh6qZXEiaCiaSmp5MhKK6GSWGYnl0aj6IiaVL39taagjAmgqvfMkDDicCA/132' ||
    user.avatarUrl ===
      'http://thirdwx.qlogo.cn/mmopen/vi_32/89Q1NzeOSf882QuupOW8LsfVfLnJh9bSkmv55CDzwyygRhAkMtNxibfRVQfic5mLdljhcn8Y4dVDqA3jYQa9DBdQ/132'
  ) {
    user.avatarUrl =
      'http://thirdwx.qlogo.cn/mmopen/vi_32/89Q1NzeOSf882QuupOW8LsfVfLnJh9bSkmv55CDzwyygRhAkMtNxibfRVQfic5mLdljhcn8Y4dVDqA3jYQa9DBdQ/132';
    user.nickName = '小小影';
  }

  util.$log('更新用户信息', user);
  Object.assign(state.user, user);
  uni.setStorage({
    key: 'user',
    data: Object.assign({}, state.user, { banState: false }),
  });
};

export const USER_SIGNOUT = (state) => {
  Object.keys(state.user).forEach((k) => Vue.delete(state.user, k));
};
