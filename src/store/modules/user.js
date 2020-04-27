/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-26 11:33:19
 */

export default {
  namespaced: true,
  state: uni.getStorageSync('user') || {
    username: '影浅',
    authState: false,
    banState: false,
    nickName: '',
    id: '--',
    author: 'seekwe@gmail.com',
  },
  getters: {},
};
