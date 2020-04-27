/*
 * @Author: seekwe
 * @Date: 2020-04-03 11:00:42
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-24 20:38:41
 */
export const methods = (_) => {
  return {
    selectTipCard() {
      this.help = {
        title: '提示卡说明',
        type: 'tip',
        content: '没什么好说明的',
      };
    },
    selectShowCard() {
      this.help = {
        type: 'display',
        title: '显示卡说明',
        content: '没什么好说明的',
      };
    },
    selectDiscountCard() {
      this.help = {
        type: 'discount',
        title: '优惠卡说明',
        content: '没什么好说明的',
      };
    },
    submisBtn() {
      this.$log('点击了按钮', this.help.type);
      switch (this.help.type) {
        case 'discount':
          this.getDiscountCard();
          break;
        case 'tip':
          this.getTipCard();
          break;
        case 'display':
          this.getDisplayCard();
          break;
      }
      this.help = {};
    },
    getTipCard() {
      this.$log('买提示');

      if (this.tipState) {
        this.$log('已购买过');
        return;
      }

      let done = this.$loading('获取提示中');
      this.$api((_) => {
        return ['buy.tip', this.series.drawId];
      })
        .then((e) => {
          this.excludedProduct = e;
          done();
        })
        .catch((e) => {
          done();
          this.$alert('积分不足');
        });
    },
    getDiscountCard() {
      this.$log('买优惠');
      if (this.discountState) {
        this.$log('已购买过');
        return;
      }

      let done = this.$loading('兑换优惠卡中');
      this.$api((_) => {
        return ['buy.discount', this.series.drawId];
      })
        .then((e) => {
          this.priceAfterDiscount = e.priceAfterDiscount;
          done();
        })
        .catch((e) => {
          done();
          this.$alert('积分不足');
        });
    },
    getDisplayCard() {
      this.$log('买显示');

      if (this.showState) {
        this.$log('已购买过');
        return;
      }

      let done = this.$loading('显示中');
      this.$api((_) => {
        return ['buy.display', this.series.drawId];
      })
        .then((e) => {
          this.$log('display', e);
          this.product = e;
          done();
        })
        .catch((e) => {
          done();
          this.$alert('积分不足');
        });
    },
  };
};
