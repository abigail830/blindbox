/*
 * @Author: seekwe
 * @Date: 2019-11-08 14:06:18
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-07-13 14:17:18
 */

import Store from '@/store';
let app;
export const __setMp = (mpObj) => {
  app = mpObj;
};
export const $alert = (text, duration, success = null, opt = {}) => {
  let defOpt = {
    title: text,
    duration: duration || 3000,
    icon: success === true ? 'success' : success === false ? 'loading' : 'none',
  };
  if (typeof success === 'string') {
    defOpt.image = success;
  }
  uni.showToast(Object.assign(defOpt, opt));
};

const $loading = (text = '加载中', mask = true) => {
  uni.showLoading({
    title: text,
    mask: mask,
  });
  return uni.hideLoading;
};

const $go = async function (page, goType = false) {
  let path = '';
  if (page.indexOf('.') === 0) {
    let tmp = this.$mp.page.route.split('/');
    tmp[tmp.length - 1] = page.slice(2);
    path = '/' + tmp.join('/');
  } else if (page.indexOf('/') === 0) {
    path = page;
  } else {
    path = '/pages/' + page;
  }
  let url = {
    url: path,
  };
  let res;
  if (goType === true) {
    res = await uni.redirectTo(url);
  } else if (goType === false) {
    res = await uni.navigateTo(url);
  } else if (typeof goType === 'string' && typeof uni[goType] === 'function') {
    res = await uni[goType](url);
  } else {
    res = await uni.reLaunch(url);
  }
  return res;
};
export const $back = function (delta = 1) {
  if (getCurrentPages().length <= 1) {
    this.$go('index/home', 'reLaunch');
    return;
  }
  uni.navigateBack({ delta: delta });
};
export const $is = {
  object(arg) {
    return typeof arg === 'object' && arg !== null;
  },
  func(arg) {
    return typeof arg === 'function';
  },
  phone(phone) {
    return /^0?1\d{10}$/.test(phone);
  },
};

function $log(...msg) {
  if (process.env.NODE_ENV === 'development') {
    console.log('%c LOG ', 'background:#aaa;color:#bada55', ...msg);
  }
}

export const $app = {
  async getSetting() {
    return await new Promise((resolve, reject) => {
      uni.getSetting({
        success(res) {
          resolve(res);
        },
        fail(err) {
          reject(err);
        },
      });
    });
  },
};

export const $systems = (() => {
  let s = uni.getSystemInfoSync();

  if (typeof s.safeArea === 'undefined') {
    s.safeArea = {
      width: s.windowWidth,
      height: s.windowHeight,
      bottom: s.windowHeight,
    };
  }

  s.screenScale = s.windowHeight / s.windowWidth;

  switch (true) {
    case s.screenScale > 1.9:
      s.size = 'large';
      break;
    case s.screenScale < 1.6:
      s.size = 'small';
      break;
    default:
      s.size = 'ordinary';
  }
  s.isFull = s.statusBarHeight > 20;

  return s;
})();

export const $to = {
  px2rpx(v) {
    return +(v / ($systems.windowWidth / 750)).toFixed(2);
  },
  rpx2px(v) {
    return +(v * ($systems.windowWidth / 750)).toFixed(2);
  },
  to(v) {
    let unit, value;
    let vType = typeof v;
    if (vType === 'number') {
      return [v, ''];
    } else if (!!v && vType === 'string') {
      let reg = /^[0-9]+([.]{1}[0-9]+){0,1}(rpx|px)$/g;
      const results = reg.exec(v);
      if (!results) {
        if (v == +v) {
          return [v, ''];
        }
        console.error(`to px: ${v} is illegal`);
        return [0, ''];
      }
      unit = results[2];
      value = parseFloat(v);
      return [value, unit];
    }
    return [0, ''];
  },
  rpx(v, zoom = 1) {
    let [value, unit] = this.to(v);
    if (value === 0) {
      return 0;
    } else if (typeof value === 'number' && unit === '') {
      return value * zoom;
    }
    let res = 0;
    if (unit === 'rpx') {
      res = value;
    } else if (unit === 'px') {
      res = this.px2rpx(value);
    }
    return res * zoom;
  },
  px(v, zoom = 1) {
    let [value, unit] = this.to(v);
    if (value === 0) {
      return 0;
    } else if (typeof value === 'number' && unit === '') {
      return value * zoom;
    }

    let res = 0;
    if (unit === 'rpx') {
      res = this.rpx2px(value);
    } else if (unit === 'px') {
      res = value;
    }
    return res * zoom;
  },
};
export const $awaitWrap = (promise) => {
  return promise.then((data) => [data, null]).catch((err) => [null, err]);
};

export const $shake = (
  stopFn = (stop) => { },
  intervalTime = 500,
  music = ''
) => {
  let audioCtx,
    stop,
    play,
    global = {
      state: false,
      lastTime: 0,
      lastX: 0,
      lastY: 0,
      lastZ: 0,
      shakeSpeed: 70,
    };
  let soundEffects = Store.state.system.soundEffects
  if (music) {
    if (soundEffects) {
      Store.commit('system/changeSoundEffects', false);
    }
    audioCtx = uni.createInnerAudioContext();
    audioCtx.src = music;
    audioCtx.onPlay((_) => { });
    audioCtx.onEnded((_) => {
      if (soundEffects) {
        Store.commit('system/changeSoundEffects', true);
      }
    });
    audioCtx.onError((res) => {
      $log('audioCtx error:', res.errMsg, res.errCode);
    });
  }
  const fn = function (acceleration) {
    if (global.state) {
      return;
    }
    let nowTime = new Date().getTime();
    if (nowTime - global.lastTime > 100) {
      let diffTime = nowTime - global.lastTime;
      global.lastTime = nowTime;
      let x = acceleration.x;
      let y = acceleration.y;
      let z = acceleration.z;
      let speed =
        (Math.abs(x + y + z - global.lastX - global.lastY - global.lastZ) /
          diffTime) *
        10000;

      if (speed > global.shakeSpeed) {
        play();
      }
      global.lastX = x;
      global.lastY = y;
      global.lastZ = z;
    }
  };
  play = () => {
    global.state = true;
    setTimeout((_) => {
      global.state = false;
    }, intervalTime);
    if (audioCtx) {
      audioCtx.play();
    }
    $is.func(stop) && stopFn(stop);
  };
  stop = (_) => {
    if (audioCtx) {
      audioCtx.destroy();
      audioCtx = null;
    }
    wx.offAccelerometerChange(fn);
  };
  wx.onAccelerometerChange(fn);

  console.log('开始', global);
  return [stop, play];
};
export const $util = {
  throttle: function (fn, gapTime = 1500) {
    let _lastTime = null;
    return function () {
      let _nowTime = +new Date();
      if (_nowTime - _lastTime > gapTime || !_lastTime) {
        fn.apply(this, arguments);
        _lastTime = _nowTime;
      }
    };
  },
};
export default {
  $util,
  $go,
  $back,
  $alert,
  $loading,
  $log,
  $is,
  $to,
  $app,
  $awaitWrap,
  $systems,
  $PLATFORM: process.env.VUE_APP_PLATFORM,
};

export const shuffle = (arr) => {
  for (let i = arr.length - 1; i >= 0; i--) {
    let rIndex = Math.floor(Math.random() * (i + 1));
    let temp = arr[rIndex];
    arr[rIndex] = arr[i];
    arr[i] = temp;
  }
  return arr;
};
