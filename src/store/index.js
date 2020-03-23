/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:19:31
 */

import Vue from 'vue';
import Vuex from 'vuex';
import * as actions from './actions';
import * as getters from './getters';
import * as mutations from './mutations';
import modules from './modules';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: uni.getStorageSync('token') || '' // 用户权鉴
  },
  actions,
  getters,
  mutations,
  modules,
  strict: process.env.NODE_ENV !== 'production' //使用严格模式
});
