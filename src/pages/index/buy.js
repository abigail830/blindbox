/*
 * @Author: seekwe
 * @Date: 2020-04-03 11:00:42
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-16 11:46:01
 */

import { CardDescription } from '@/config';
export const methods = (_) => {
  return {
    selectTipCard() {
      this.help = CardDescription.tips;
    },
    selectShowCard() {
      this.help = CardDescription.display;
    },
    selectDiscountCard() {
      this.help = CardDescription.discount;
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
      if (this.tipState) {
        this.$log('已购买过');
        return;
      }
      let done = this.$loading('获取提示中');
      this.$api((_) => {
        return ['buy.tip', this.drawId];
      })
        .then((e) => {
          this.excludedProduct = e;
          done();
        })
        .catch((e) => {
          this.$log('err', e);
          let msg = '积分不足';
          if (e == 'NO_TIPS_AVAILABLE') {
            msg = '唯一商品不支持提示卡';
          }
          done();
          this.$alert(msg);
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
        return ['buy.discount', this.drawId];
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
      if (this.showState) {
        this.$log('已购买过');
        return;
      }

      let done = this.$loading('显示中');
      this.$api((_) => {
        return ['buy.display', this.drawId];
      })
        .then((e) => {
          this.$log('display', e);
          this.product = e;
          done();
        })
        .catch(() => {
          done();
          this.$alert('积分不足');
        });
    },
  };
};
