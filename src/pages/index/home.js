/**
 * @Author: seekwe
 * @Date:   2020-03-10 14:27:18
 * @Last Modified by:   seekwe
 * @Last Modified time: 2020-03-16 15:17:27
 */

import { mapState, mapGetters } from 'vuex';
import { themeColor } from '@/common/var';
export const computed = _ => {
  return {
    isNew() {
      return this.tabActive === 'new';
    },
    isSeriesAll() {
      return this.seriesActive === 'all';
    },
    seriesShowData() {
      return this.seriesData.slice(0, 3);
    },
    seriesData() {
      return this.series.items || [];
    },
    ...mapState(['system', 'series']),
    ...mapGetters(['userInfo', 'authState', 'banState'])
  };
};

export const data = _ => {
  return {
    seriesActive: 'all',
    isShowBottom: false, // 显示全部系列
    tabActive: 'all', // 默认 tab: all, new
    swiperConfig: {
      indicatorColor: 'rgba(197,200,198, 1)',
      indicatorActiveColor: themeColor,
      indicatorDots: true,
      autoplay: true,
      interval: 2000,
      duration: 500
    },
    swiperItems: [
      {
        image: '/static/demo.png'
      },
      {
        image: '/static/demo.png'
      },
      {
        image: '/static/demo.png'
      },
      {
        image: '/static/demo.png'
      }
    ],
    itemsNewData: [],
    itemsNewPage: 1
  };
};

export const methods = _ => {
  return {
    setBarrage() {
      if (!this.system.barrage) return;
      this.$refs.zBarrage.add('我是后面来的啦' + +new Date());
    },
    switchTab(key) {
      this.tabActive = key;
    },
    setSeriesActive(id) {
      this.seriesActive = id;
      this.$log('加载指定系列:', id);
    },
    getSeriesData() {
      let seriesData = [];
      for (let i = 0; i < 33; i++) {
        seriesData.push({
          id: i,
          image: '/static/demo-icon.png',
          title: '明星名字'
        });
      }
      this.$store.commit('series/setSeriesData', seriesData);
    },
    goInfo(type, id) {
      this.$go('./info?id=' + id);
    },
    reloadItems() {
      this.itemsNewPage = 1;
      this.getItems();
    },
    getItems() {
      let done = this.$loading();
      let itemsNewData = [];

      setTimeout(_ => {
        for (let i = 0; i < 5; i++) {
          itemsNewData.push({
            image: '/static/demo.png',
            id: i,
            price: '100',
            isTop: i <= 2,
            isNew: i < 2,
            isAdvance: i < 3 && i >= 1,
            title: `(${this.itemsNewPage})我的标题:` + i
          });
        }
        if (this.itemsNewPage > 1) {
          this.itemsNewData = this.itemsNewData.concat(itemsNewData);
        } else {
          this.itemsNewData = itemsNewData;
        }
        done();
      }, 1000);
    }
  };
};
