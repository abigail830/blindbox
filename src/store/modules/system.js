/*
 * @Author: seekwe
 * @Date: 2020-03-01 20:57:24
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:19:16
 */

let data = uni.getStorageSync('system') || {};

let soundEffects =
  typeof data.soundEffects === 'boolean' ? data.soundEffects : true;
let barrage = typeof data.barrage === 'boolean' ? data.barrage : true;

export default {
  namespaced: true,
  state: {
    // 音效
    soundEffects: soundEffects,
    // 弹幕
    barrage: barrage
  },
  getters: {},
  mutations: {
    changeSoundEffects(state, value) {
      state.soundEffects = value;
      uni.setStorage({
        key: 'system',
        data: state
      });
    },
    changeBarrage(state, value) {
      state.barrage = value;
      uni.setStorage({
        key: 'system',
        data: state
      });
    }
  }
};
