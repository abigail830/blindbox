/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:15:06
 */
export default {
  getToken: ['post', '/wx/users/login'],
  getWebToken: ['post', ''],
  getUserInfo: ['post', ''],
  login: ['post', '/wx/users/auth'],
  update: ['post', '/wx/users/auth'],
  info: ['get', '/wx/users/by-token']
};
