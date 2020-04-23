/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-11 15:54:51
 */
export default {
  getToken: ['post', '/wx/users/login'],
  getWebToken: ['post', ''],
  getUserInfo: ['post', ''],
  login: ['post', '/wx/users/auth'],
  update: ['post', '/wx/users/auth'],
  info: ['get', '/wx/users/by-token'],
  shareActivity: [
    'post',
    function(activityId = 0) {
      return `/wx/users/share-activity/${activityId}`;
    }
  ]
};
