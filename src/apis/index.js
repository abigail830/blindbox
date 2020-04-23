/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-04-03 18:24:34
 */
import cfg from '../config';
import util from '../common/util';
import store from '../store';
import Fly from 'flyio/dist/npm/wx';
import qs from 'qs';
import appApi from './util';

export const fly = new Fly();
const newFly = new Fly();
const files = require.context('./modules', false, /\.js$/);
let apis = {},
  defModule = {},
  getTokenStatsu = false;
files.keys().forEach(key => {
  if (key === './index.js') {
    defModule = files(key).default;
    return;
  }
  apis[key.replace(/(\.\/|\.js)/g, '')] = files(key).default;
});

// if (typeof Promise.prototype['finally'] === 'undefined')
Promise.prototype['finally'] = function(callback) {
  let P = this.constructor;
  return this.then(
    value => P.resolve(callback(value)),
    err => P.resolve(callback(err))
  );
};

// if (typeof Promise.prototype['done'] === 'undefined')
Promise.prototype['done'] = function(onResolved, onRejected) {
  this.then(onResolved, onRejected).catch(function(err) {
    setTimeout(() => {
      throw err;
    }, 0);
  });
};

const log = (...msg) => {
  if (process.env.NODE_ENV === 'development') {
    console.log('%c API ', 'background:#f5f5dc;color:#bada55', ...msg);
  }
};
const noJSON = data => {
  if (typeof data === 'string') {
    util.$log('NoJSON');
    return { code: 200, msg: 'toJson', data: data };
  }
  return null;
};
const api = async (
  name,
  data = {},
  opt = {},
  success,
  fail = () => {},
  complete = () => {}
) => {
  const nameType = typeof name;
  let api = '',
    urlData = [];
  if (nameType === 'function') {
    let tmp = name() || ['', ''];
    name = tmp[0];
    urlData = tmp.slice(1);
  }
  if (typeof name === 'string') {
    const moduleArr = name.split('.');
    const module = apis[moduleArr[0]] || '';
    if (!!module) {
      api = moduleArr[1] ? module[moduleArr[1]] : '';
    } else {
      api = defModule[name];
    }
  } else {
    api = name;
  }
  if (!api) {
    log('Api Non existent: ' + name);
    return;
  }
  const apiType = typeof api;

  if (apiType === 'string') {
    return api;
  }

  let [type, url] = api,
    res;
  if (typeof url === 'function') {
    url = url(...urlData);
  }

  if (typeof data === 'string' || typeof data === 'number') {
    url = url + data;
    data = null;
  }
  if (typeof success === 'function') {
    res = await fly
      .request(
        url,
        data,
        Object.assign(
          {
            method: type
          },
          opt
        )
      )
      .then(e => {
        log(type.toUpperCase(), url, data, e);
        return success(e);
      })
      .catch(e => {
        log('catch', e);
        log(type.toUpperCase() + '(catch)', url, data, e);
        return fail(e);
      })
      .finally(complete);
  } else {
    res = fly
      .request(
        url,
        data,
        Object.assign(
          {
            method: type
          },
          opt
        )
      )
      .then(e => {
        log(type.toUpperCase(), url, data, e);
        return e;
      });
  }

  return res;
};

fly.config.baseURL = cfg.apiHost;
fly.config.timeout = cfg.apiTimeout;
fly.config.headers = {};

fly.interceptors.request.use(
  request => {
    let token = store.getters.token;
    if (token) {
      request.headers['Authorization'] = token;
    }
    request.headers['platform'] = process.env.VUE_APP_PLATFORM;
    request.headers['x_requested_with'] = 'xmlhttprequest';

    return request;
  },
  err => {
    return Promise.reject(err);
  }
);

fly.interceptors.response.use(
  async response => {
    let data = response.data;
    const no = noJSON(data);
    let code = response.status;
    if (no) {
      // return no;
      data = { msg: data };
    }
    if (data['code'] === 401) {
      log('NoLogin', getTokenStatsu);
      if (!getTokenStatsu) {
        // apis["user"] && apis["user"]["getToken"]
        getTokenStatsu = true;
        setTimeout(_ => {
          getTokenStatsu = false;
        }, 20000);
        newFly.config = fly.config;
        // #ifdef H5
        log('不能预获取 Token');
        return data;
        // #endif

        return (
          (await getToken(_ => {
            return fly.request(response.request).then(e => {
              return e;
            });
          })) || data
        );
      }
      return Promise.reject('NoLogin');
    }
    return data;
  },
  async err => {
    let code = err.status;
    let response = err.response;
    let url = '';
    if (util.$is.object(err['request'])) {
      url = err['request'].url;
    }
    log('status:', code, url, response);
    let data = response ? response.data : '';
    if (code === 401 || code === 403) {
      log('NoLogin', getTokenStatsu, code);
      if (!getTokenStatsu) {
        getTokenStatsu = true;
        setTimeout(_ => {
          getTokenStatsu = false;
        }, 20000);
        newFly.config = fly.config;
        // #ifdef H5
        log('不能预获取 Token');
        return data;
        // #endif
        const newRes = await getToken(e => {
          // if (process.env.NODE_ENV === 'development') {
          //   if (err.request.url === '/wx/users/by-token') {
          //     err.request.url = '/wx/users/by-tokenTest';
          //   }
          // }
          return fly.request(err.request).then(e => {
            log('retry', e);
            return e;
          });
        });
        return newRes;
      }
      return Promise.reject('NoLogin');
    } else if (code === 400) {
      util.$log('客服端错误', code, data.errorMsg);
      return Promise.reject(data.errorMsg);
    } else if (!!data.errorMsg) {
      return Promise.reject(data.errorMsg);
    } else if (code >= 500) {
      util.$alert('系统繁忙');
      return Promise.reject('系统繁忙');
    }

    let message = err.message;
    if (typeof message === 'undefined') {
      message = '';
    } else {
      message = message.trim();
    }

    if (message === 'request:ok') {
      const status = err.status;
      let data = err.response.data;
      const no = noJSON(data);
      if (no) {
        data = { code: 200, msg: 'toJson', data: data };
      }
      return Promise.resolve(data);
    } else if (message === 'request:fail') {
      util.$alert('网络繁忙');
    }
    return Promise.reject(message);
  }
);

export const getToken = async (fn = _ => {}, errFn = e => {}) => {
  fly.lock();
  let code = await appApi.getLoginCode();
  newFly.config = fly.config;
  try {
    let url = '';
    // #ifdef H5
    url = apis['user']['getWebToken'][1];
    // #endif
    // #ifndef H5
    url = apis['user']['getToken'][1];
    // #endif
    const e = await newFly.post(url, qs.stringify({ code: code }), {
      headers: {
        'X-WX-Code': code
      }
    });
    const data = e && e.data ? e.data : {};
    log('Login', data);
    if (data['token']) {
      store.commit('SET_TOKEN', data['token']);
      fly.unlock();
      return fn(data);
    } else {
      fly.unlock();
    }

    return null;
  } catch (e) {
    fly.unlock();
    let errRes = errFn(e);
    return errRes !== undefined ? errRes : e;
  }
};

export const keys = Object.keys(apis);
export const ajax = api;
export const all = newFly.all;
export default Object.assign({}, defModule, keys);
