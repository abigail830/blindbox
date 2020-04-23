/*
 * @Author: seekwe
 * @Date: 2020-03-11 23:09:59
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-25 11:59:24
 */

let seriesData = uni.getStorageSync('seriesData') || [
  {
    id: 0,
    image: '/static/demo-icon.png',
    title: '明星'
  },
  {
    id: 0,
    image: '/static/demo-icon.png',
    title: '明星'
  }
];
if (process.env.NODE_ENV === 'development') {
  // todo 测试数据
  // for (let i = 0; i < 33; i++) {
  //   seriesData.push({
  //     id: i,
  //     image: '/static/demo-icon.png',
  //     title: '明星名字'
  //   });
  // }
}
export default {
  namespaced: true,
  state: {
    items: seriesData
  },
  mutations: {
    // 设置系列数据
    setSeriesData(state, value) {
      state.items = value;
      uni.setStorage({
        key: 'seriesData',
        data: value
      });
    }
  }
};
