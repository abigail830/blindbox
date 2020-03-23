/**
 * @Author: seekwe
 * @Date:   2020-03-09 12:03:36
 * @Last Modified by:   seekwe
 * @Last Modified time: 2020-03-09 22:06:07
 */

import { $to } from '@/common/util';
const fsm = uni.getFileSystemManager();
const FILE_BASE_NAME = 'tmp_base64src';

const base64src = function(base64data) {
  return new Promise((resolve, reject) => {
    const [, format, bodyData] =
      /data:image\/(\w+);base64,(.*)/.exec(base64data) || [];
    if (!format) {
      reject(new Error('ERROR_BASE64SRC_PARSE'));
    }
    const filePath = `${wx.env.USER_DATA_PATH}/${FILE_BASE_NAME}.${format}`;
    const buffer = wx.base64ToArrayBuffer(bodyData);
    fsm.writeFile({
      filePath,
      data: buffer,
      encoding: 'binary',
      success() {
        resolve(filePath);
      },
      fail() {
        reject(new Error('ERROR_BASE64SRC_WRITE'));
      }
    });
  });
};

const downImg = (url, header) => {
  if (!url) {
    console.error('图片url 不能为空');
    throw new Error('图片url 不能为空');
  }
  if (/wxfile\:\/\//.test(url)) {
    return cache[url] || url;
  }
  if (url.indexOf('data:image') === 0) {
    return base64src(url);
  }

  if (cache[url]) {
    return cache[url];
  }
  return new Promise(function(resolve, reject) {
    uni.downloadFile({
      url: url.replace('http:', 'https:'),
      header: header || {},
      success: res => {
        resolve(res.tempFilePath);
        cache[url] = res.tempFilePath;
      },
      fail: err => {
        reject(err);
      }
    });
  });
};

const cache = {};
let _ratio = 1;
class Sprite {
  constructor(config) {
    this.x = config.x;
    this.y = config.y;
    this.width = config.width;
    this.height = config.height;
    let radius = config.borderRadius || config.radius;
    if (typeof radius === 'string' && radius.indexOf('%') >= 0) {
      let r = radius.match(/([-]?\d{1,3})%/)[1];
      this.radius = this.width / (200 / r);
    } else {
      this.radius = $to.rpx2px(radius);
    }
  }
  Border() {}
}

class Image extends Sprite {
  constructor(config) {
    super(config);
    this.src = config.src;

    this.header = config.header || {};
  }

  async draw(ctx) {
    ctx.save();
    this.Border();
    let src = await downImg(this.src, this.header);
    if (this.radius > 0) {
      ctx.setGlobalAlpha(0);
      this.r = this.width / 2;
      ctx.beginPath();
      ctx.arc(this.x + this.r, this.y + this.r, this.r, 0, 2 * Math.PI);
      ctx.fill();
      ctx.clip();
      ctx.setGlobalAlpha(1);
    }
    ctx.drawImage(src, this.x, this.y, this.width, this.height);
    ctx.restore();
  }
}
class Rect extends Sprite {
  constructor(config) {
    super(config);
    this.src = config.src;
    this.background = config.background || '#fff';
  }

  async draw(ctx) {
    ctx.save();
    ctx.setFillStyle(this.background);
    if (this.radius > 0) {
      ctx.beginPath();
      ctx.arc(
        this.x + this.radius,
        this.y + this.radius,
        this.radius,
        Math.PI,
        Math.PI * 1.5
      );
      ctx.moveTo(this.x + this.radius, this.y);
      ctx.lineTo(this.x + this.width - this.radius, this.y);
      ctx.lineTo(this.x + this.width, this.y + this.radius);
      ctx.arc(
        this.x + this.width - this.radius,
        this.y + this.radius,
        this.radius,
        Math.PI * 1.5,
        Math.PI * 2
      );
      ctx.lineTo(this.x + this.width, this.y + this.height - this.radius);
      ctx.lineTo(this.x + this.width - this.radius, this.y + this.height);
      ctx.arc(
        this.x + this.width - this.radius,
        this.y + this.height - this.radius,
        this.radius,
        0,
        Math.PI * 0.5
      );
      ctx.lineTo(this.x + this.radius, this.y + this.height);
      ctx.lineTo(this.x, this.y + this.height - this.radius);
      ctx.arc(
        this.x + this.radius,
        this.y + this.height - this.radius,
        this.radius,
        Math.PI * 0.5,
        Math.PI
      );
      ctx.lineTo(this.x, this.y + this.radius);
      ctx.lineTo(this.x + this.radius, this.y);
      ctx.fill();
      ctx.closePath();
    } else {
      ctx.fillRect(this.x, this.y, this.width, this.height);
    }
    ctx.restore();
  }
}

class Text extends Sprite {
  constructor(config) {
    super(config);
    this.text = config.text;
    this.color = config.color;
    this.maxLine = config.maxLine;
    this.lineHeight = config.lineHeight || 1.2;
    this.fontSize = $to.rpx2px(config.fontSize || 24);
  }

  async draw(ctx) {
    ctx.save();
    ctx.fillStyle = this.color;
    ctx.setFontSize(this.fontSize);
    let multiLine = false;

    if (/\n/.test(this.text)) {
      multiLine = true;
    }
    if (this.width || multiLine) {
      let len = Math.floor(this.width / this.fontSize);
      let row = null;
      let arr = [];
      if (multiLine) {
        arr = this.text.split('\n');
        row = arr.length;
      } else {
        row = Math.ceil(this.width / len);
      }
      //.ctx.measureText(text).width

      if (row === 1) {
        ctx.fillText(this.text, this.x, this.y + this.fontSize);
      } else {
        if (!multiLine) {
          for (let i = 0; i < row; i++) {
            if (this.maxLine > 0 && i >= this.maxLine) {
              break;
            }
            arr.push(this.text.substr(i * len, len));
          }
        }
        arr.forEach((str, i) =>
          ctx.fillText(
            str,
            this.x,
            this.y + this.fontSize * (i + 1) * this.lineHeight
          )
        );
      }
    } else {
      ctx.fillText(this.text, this.x, this.y + this.fontSize);
    }

    ctx.restore();
  }
}

class Poster {
  constructor(
    { width, height, scale, canvasId, pixelRatio = 1, backgroundColor },
    vue
  ) {
    this.width = width;
    this.height = height;
    this.scale = scale || 1;
    this.canvasId = canvasId;
    this.vue = vue;
    this.ctx = uni.createCanvasContext(canvasId, vue);
    this.setBackgroundColor(backgroundColor);
    _ratio = pixelRatio;
  }
  setHeight(height) {
    this.height = height;
  }
  setWidth(width) {
    this.width = width;
  }
  setBackgroundColor(backgroundColor) {
    if (!backgroundColor) return;
    this.ctx.save();
    this.ctx.setFillStyle(backgroundColor);
    this.ctx.fillRect(0, 0, this.width, this.height);
    this.ctx.restore();
  }
  async draw(steps) {
    let ctx = this.ctx;
    ctx.scale(this.scale, this.scale);
    let omTree = [];
    const typeMap = {
      img: Image,
      text: Text,
      rect: Rect
    };
    console.log('draw');

    for (let i = 0, len = steps.length; i < len; i++) {
      let sprite = steps[i] || {};

      sprite.width = $to.rpx2px(sprite.width || 0);
      sprite.height = $to.rpx2px(sprite.height || 0);
      if (
        sprite.draw &&
        Object.prototype.toString.call(sprite.draw) === '[object Function]'
      ) {
        sprite.type = 'custom';
        omTree.push(sprite);
        continue;
      }
      if (!sprite.type || !typeMap[sprite.type]) {
        console.warn('sprite err', sprite);
        continue;
      }

      if (i > 0 && /^\+\d+$/.test(sprite.x)) {
        if (!steps[i - 1].x && steps[i - 1].x !== 0) {
          console.warn(i + '节点的上一个节点没有x坐标值');
          sprite.x = $to.rpx2px(sprite.x.slice(1));
        } else {
          let lastX = steps[i - 1].x;
          if (!!sprite.positionX) {
            lastX = lastX + steps[i - 1].width;
          }
          sprite.x = lastX + $to.rpx2px(sprite.x.slice(1));
        }
      } else {
        sprite.x = $to.rpx2px(sprite.x || 0);
      }

      if (i > 0 && /^\+\d+$/.test(sprite.y)) {
        if (!steps[i - 1].y && steps[i - 1].y !== 0) {
          console.warn(i + '节点的上一个节点没有y坐标值');
          sprite.y = $to.rpx2px(sprite.y.slice(1));
        } else {
          let lastY = steps[i - 1].y;
          if (!!sprite.positionY) {
            lastY = lastY + steps[i - 1].height;
          }
          sprite.y = lastY + $to.rpx2px(sprite.y.slice(1));
        }
      } else {
        sprite.y = $to.rpx2px(sprite.y || 0);
      }

      omTree.push(new typeMap[sprite.type](sprite));
    }

    for (let om of omTree) {
      try {
        await om.draw(ctx);
      } catch (err) {
        console.error(err);
      }
    }

    return new Promise((resolve, reject) => {
      ctx.draw(false, () => {
        uni.canvasToTempFilePath(
          {
            x: 0,
            y: 0,
            width: this.width * this.scale,
            height: this.height * this.scale,
            canvasId: this.canvasId,
            quality: 0.8,
            complete: e => {
              console.log(e);
            },
            success: res => {
              resolve(res.tempFilePath);
            },
            fail: err => {
              reject(err);
            }
          },
          this.vue
        );
      });
    });
  }
  clear() {
    this.ctx.scale(1, 1);
    this.ctx.clearRect(0, 0, this.width, this.height);
    this.ctx.draw();
  }
}

export default Poster;
