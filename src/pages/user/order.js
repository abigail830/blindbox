/*
 * @Author: seekwe
 * @Date: 2020-04-19 15:32:59
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-20 12:42:13
 */
export default {
  data: {
    itemsData1: [],
    itemsData2: [],
    itemsData3: [],
  },
  methods: {
    getAll() {
      this.getItemsData3();
      this.getItemsData2();
      this.getItemsData1();
    },
    getItemsData3() {
      this.$api('order.delivered').then((e) => {
        this.itemsData1 = e;
      });
    },
    getItemsData2() {
      this.$api('order.pendingDeliver').then((e) => {
        this.itemsData2 = e;
      });
    },
    getItemsData1() {
      this.$api('order.pendingPay').then((e) => {
        this.itemsData3 = e;
      });
    },
  },
};
