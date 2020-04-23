/*
 * @Author: seekwe
 * @Date: 2020-04-11 21:15:07
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-20 13:50:23
 */
export default {
  get: ['get', '/wx/orders'],
  pay: [
    'post',
    function(drawId = 0) {
      return `/wx/orders/product/${drawId}`;
    },
  ],
  drawProduct: [
    'get',
    function(drawId = 0) {
      return `/wx/products/draw/${drawId}/product`;
    },
  ],
  postPay: ['post', '/wx/orders/transport'],
  pendingPay: ['get', '/wx/orders?status=PENDING_PAY_TRANSPORT'],
  pendingDeliver: ['get', '/wx/orders?status=PENDING_DELIVER'],
  delivered: ['get', '/wx/orders?status=DELIVERED'],
};
