/*
 * @Author: seekwe
 * @Date: 2020-03-29 21:26:19
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-03 17:03:51
 */
let goods = [],
  address = {},
  draws = {},
  draw = {},
  items = [];
if (process.env.NODE_ENV === 'development') {
  goods = uni.getStorageSync('current/goods') || [];
  address = uni.getStorageSync('current/address') || {};
  draws = uni.getStorageSync('current/draws') || [];
  items = uni.getStorageSync('current/items') || [];
  draw = uni.getStorageSync('current/draw') || {};
}
export default {
  namespaced: true,
  state: {
    series: uni.getStorageSync('current/series') || {},
    // todo dev 后期
    goods: goods,
    address: address,
    draws: draws,
    draw: draw,
    items: items,
  },
  getters: {
    draw(state) {
      console.log(state.draws);

      if (!state.draws.length) {
        return {};
      }
      return state.draws[Math.floor(Math.random() * state.draws.length)];
    },
  },
  mutations: {
    setDraws(state, value) {
      state.draws = value;
      if (process.env.NODE_ENV === 'development') {
        uni.setStorage({
          key: 'current/draws',
          data: value,
        });
      }
    },
    setItems(state, value) {
      state.items = value;
      if (process.env.NODE_ENV === 'development') {
        uni.setStorage({
          key: 'current/items',
          data: value,
        });
      }
    },
    setDraw(state, value) {
      state.draw = value;
      if (process.env.NODE_ENV === 'development') {
        uni.setStorage({
          key: 'current/draw',
          data: value,
        });
      }
    },
    setSeriesData(state, value) {
      state.series = value;
      uni.setStorage({
        key: 'current/series',
        data: value,
      });
    },
    setGoodsData(state, value) {
      state.goods = JSON.parse(JSON.stringify(value));
      uni.setStorage({
        key: 'current/goods',
        data: state.goods,
      });
    },
    setAddress(state, value) {
      state.address = JSON.parse(JSON.stringify(value));
      console.log('address', address);

      uni.setStorage({
        key: 'current/address',
        data: state.address,
      });
    },
  },
};
