/*
 * @Author: seekwe
 * @Date: 2020-03-25 11:35:38
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-02 23:46:19
 */

export default {
  all: [
    'get',
    function(limitPerPage = 0, numOfPage = 5) {
      return `/wx/products/series/all/paging?limitPerPage=${limitPerPage}&numOfPage=${numOfPage}`;
    }
  ],
  roles: ['get', '/wx/products/roles'],
  // roles: ['get', '/wx/products/series/'],
  series: [
    'get',
    function(roleId = 0) {
      return `/wx/products/${roleId}/series`;
    }
  ],
  childSeries: [
    'get',
    function(seriesId = 0) {
      return `/wx/products/series/${seriesId}`;
    }
  ],
  childProducts: [
    'get',
    function(seriesId = 0) {
      // 根据产品系列ID，获取产品列表
      return `/wx/products/series/${seriesId}/products`;
    }
  ]
};
