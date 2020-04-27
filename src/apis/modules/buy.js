/*
 * @Author: seekwe
 * @Date: 2020-04-03 10:35:52
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-25 16:54:05
 */
export default {
  get: [
    'put',
    function(seriesId = 0) {
      return `/wx/products/draw/${seriesId}`;
    },
  ],
  info: [
    'get',
    function(drawId = 0) {
      return `/wx/products/draw/${drawId}`;
    },
  ],
  discount: [
    'post',
    function(drawId = 0) {
      /// 扣减积分以兑换优惠券,返回折后价格 / 折扣描述 / 剩余积分;
      return `/wx/products/use-discount/${drawId}`;
    },
  ],
  display: [
    'post',
    function(drawId = 0) {
      /// 扣减积分以兑换优惠券,返回折后价格 / 折扣描述 / 剩余积分;
      return `/wx/products/use-display/${drawId}`;
    },
  ],
  tip: [
    'post',
    function(drawId = 0) {
      // 扣减积分以兑换提示券,
      return `/wx/products/use-tips/${drawId}`;
    },
  ],
  cancel: [
    'delete',
    function(drawId = 0) {
      return `/wx/products/draw/${drawId}`;
    },
  ],
};
