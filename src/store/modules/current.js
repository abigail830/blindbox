/*
 * @Author: seekwe
 * @Date: 2020-03-29 21:26:19
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-13 18:22:54
 */
let goods = [],
  address = {};
if (process.env.NODE_ENV === 'development') {
  goods = uni.getStorageSync('current/goods') || [];
  address = uni.getStorageSync('current/address') || {};
}
export default {
  namespaced: true,
  state: {
    series: uni.getStorageSync('current/series') || {},
    // todo dev 后期
    goods: goods,
    address: address
  },
  getters: {},
  mutations: {
    setSeriesData(state, value) {
      console.log(value);
      state.series = value;
      uni.setStorage({
        key: 'current/series',
        data: value
      });
    },
    setGoodsData(state, value) {
      state.goods = JSON.parse(JSON.stringify(value));
      uni.setStorage({
        key: 'current/goods',
        data: state.goods
      });
    },
    setAddress(state, value) {
      state.address = JSON.parse(JSON.stringify(value));
      console.log('address', address);

      uni.setStorage({
        key: 'current/address',
        data: state.address
      });
    }
  }
};
