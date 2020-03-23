/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-17 10:19:21
 */

export const userInfo = state => state.user;
// 如果获取过用户信息那么就不会再进行sessionKey的过期验证了
export const authState = state => state.user.authState;
// 与后端通信 Token
export const token = state => state.token;
// 用户是否被禁止
export const banState = state => state.user.banState;
