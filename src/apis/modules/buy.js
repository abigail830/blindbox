/*
 * @Author: seekwe
 * @Date: 2020-04-03 10:35:52
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-03 11:57:27
 */
export default {
  get: [
    'put',
    function(seriesId = 0) {
      return `/wx/products/draw/${seriesId}`;
    }
  ],
  info: ['get', '/wx/products/draw/'],
  discount: [
    'post',
    function(drawId = 0) {
      /// 扣减积分以兑换优惠券,返回折后价格 / 折扣描述 / 剩余积分;
      return `/wx/products/use-discount/${drawId}`;
    }
  ],
  display: [
    'post',
    function(drawId = 0) {
      /// 扣减积分以兑换优惠券,返回折后价格 / 折扣描述 / 剩余积分;
      return `/wx/products/use-display/${drawId}`;
    }
  ],
  tip: [
    'post',
    function(drawId = 0) {
      // 扣减积分以兑换提示券,
      return `/wx/products/use-tips/${drawId}`;
    }
  ],
  cancel: ['delete', '/wx/products/draw/']
};
