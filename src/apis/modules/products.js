/*
 * @Author: seekwe
 * @Date: 2020-03-25 11:35:38
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-05-24 14:53:47
 */

export default {
  all: [
    'get',
    function(limitPerPage = 0, numOfPage = 5) {
      return `/wx/products/v2/series/all/paging?limitPerPage=${limitPerPage}&numOfPage=${numOfPage}`;
      return `/wx/products/series/all/paging?limitPerPage=${limitPerPage}&numOfPage=${numOfPage}`;
    },
  ],
  roles: ['get', '/wx/products/roles'],
  // roles: ['get', '/wx/products/series/'],
  series: [
    'get',
    function(roleId = 0) {
      return `/wx/products/v2/${roleId}/series`;
      return `/wx/products/${roleId}/series`;
    },
  ],
  childSeries: [
    'get',
    function(seriesId = 0) {
      return `/wx/products/v2/series/${seriesId}`;
    },
  ],
  childProducts: [
    'get',
    function(seriesId = 0) {
      // 根据产品系列ID，获取产品列表
      return `/wx/products/v2/series/${seriesId}/products`;
      return `/wx/products/series/${seriesId}/products`;
    },
  ],
  productsWithBuyFlag: [
    'get',
    function(seriesId = 0) {
      return `/wx/products/v2/series/${seriesId}/products-with-buy-flag`;
    },
  ],
};
