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
      const len = this.seriesData.length;

      return len > 2 ? this.seriesData.slice(0, 3) : this.seriesData;
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
    tabActive: 'new', // 默认 tab: all, new
    swiperConfig: {
      indicatorColor: 'rgba(197,200,198, 1)',
      indicatorActiveColor: themeColor,
      indicatorDots: true,
      autoplay: true,
      interval: 2000,
      duration: 500
    },
    swiperItems: [],
    itemsNewData: [],
    itemsData: [],
    itemsDataPage: 1,
    itemsNewPage: 1
  };
};

export const methods = _ => {
  return {
    setBarrage() {
      if (!this.system.barrage) return;

      this.$api('home.barrage').then(e => {
        e.map((e, k) => {
          if (k === 0) {
            this.$refs.zBarrage.add(e);
            return;
          }
          setTimeout(_ => {
            this.$refs.zBarrage.add(e);
          }, Math.ceil(Math.random() * 2000));
        });
      });
    },
    switchTab(key) {
      this.tabActive = key;
    },
    setSeriesActive(id) {
      this.seriesActive = id;
      if (id != 'all') {
        this.$log('加载指定系列:', id);
        this.itemsDataPage = 1;
        this.$api(_ => {
          return ['products.series', id];
        }).then(e => {
          this.itemsData = e.map(e => {
            e.image = this.$websiteUrl + e.seriesImage;
            return e;
          });
        });
      } else {
        this.$log('加载全部系列:', id);
        this.$api(_ => {
          return ['products.all', 10, this.itemsDataPage];
        }).then(e => {
          let itemsData = e.map(e => {
            e.image = this.$websiteUrl + e.seriesImage;
            return e;
          });
          if (this.itemsDataPage > 1) {
            this.itemsData = this.itemsData.concat(itemsData);
          } else {
            this.itemsData = itemsData;
          }
        });
      }
    },
    goInfo(data, id) {
      this.$store.commit('current/setSeriesData', data);
      this.$go('./info?id=' + data.id);
    },
    reloadItems() {
      this.itemsNewPage = 1;
      this.getItems();
    },
    getSwiper() {
      this.$api('home.swiper').then(e => {
        let swiperItems = e.map(e => {
          let data = e;
          data.image = this.$websiteUrl + e.mainImgUrl;
          return data;
        });

        this.swiperItems = swiperItems;
      });
    },
    goActivity(e) {
      this.$go('activity/info?id=' + e.id);
    },
    getItems() {
      this.getSwiper();
      let done = this.$loading();
      this.$api('home.newSeries')
        .then(e => {
          this.itemsNewData = e.map(e => {
            e.image = this.$websiteUrl + e.seriesImage;
            return e;
          });
          done();
        })
        .catch(e => {
          done();
        });
    }
  };
};
