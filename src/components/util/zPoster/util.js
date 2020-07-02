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
      },
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
  if (/^\/[\w\d]?/.test(url)) {
    return url;
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
      success: (res) => {
        if (res.statusCode !== 200) {
          reject('素材下载失败');
          return;
        }
        resolve(res.tempFilePath);
        cache[url] = res.tempFilePath;
      },
      complete: () => {
        // console.log('complete');
      },
      fail: (err) => {
        console.error(url, err);
        reject(err);
      },
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
    try {
      let src = await downImg(this.src, this.header);

      this.Border();
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
    } catch (e) {
      console.log('下载失败');
    }

    ctx.restore();
  }
}
class Rect extends Sprite {
  constructor(config) {
    super(config);
    this.src = config.src;
    this.background = config.background || '#fff';
    // console.warn(JSON.stringify(config));
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
    this.border = config.border;
    this.ellipsis =
      typeof config.ellipsis === 'undefined' ? '...' : config.ellipsis;
    this.text = config.text;
    this.color = config.color;
    this.maxLine = config.maxLine;
    this.center = config.center;
    this.lineHeight = config.lineHeight || 1.2;
    this.fontSize = $to.rpx2px(config.fontSize || 24);
  }

  async draw(ctx) {
    console.time('poster Text');
    ctx.save();
    // if (this.border) {
    // todo dev
    // await new Rect(
    //   Object.assign(
    //     {
    //       height: 1,
    //       width: this.width,
    //       x: this.x,
    //       y: this.y,
    //       background: '#000',
    //     },
    //     {}
    //   )
    // ).draw(ctx);
    // }
    ctx.fillStyle = this.color;
    ctx.setFontSize(this.fontSize);
    let multiLine = false;

    // if (/\n/.test(this.text)) {
    //   multiLine = true;
    // }
    if (this.width || multiLine) {
      let textLen = this.text.length;
      let textArr = [];
      let tmpIndex = 0;
      let tmpWidth = 0;
      let textTmp = [];
      let textTmpWidth = [];

      let dWidth = ctx.measureText(this.ellipsis).width;
      for (let i = 0; i < textLen; i++) {
        let text = this.text[i];
        let w = ctx.measureText(text).width;
        tmpWidth = tmpWidth + w;

        if (tmpWidth > this.width) {
          tmpWidth = 0;
          tmpIndex++;
        }

        if (tmpIndex >= this.maxLine && this.ellipsis) {
          tmpIndex--;
          let reduceWidth = 0;
          while (reduceWidth < dWidth) {
            textTmp[tmpIndex].pop();
            let lastWidth = textTmpWidth[tmpIndex].pop();
            reduceWidth = reduceWidth + lastWidth;
          }
          textTmp[tmpIndex].push(this.ellipsis);
          break;
        }
        if (typeof textTmp[tmpIndex] === 'undefined') {
          textTmp[tmpIndex] = [];
          textTmpWidth[tmpIndex] = [];
        }
        textTmp[tmpIndex].push(text);
        textTmpWidth[tmpIndex].push(w);
        textArr[i] = [text, w];
      }

      // console.log(textArr);
      // console.log(this.text, textTmp);
      textTmp.forEach((str, i) => {
        let text = str.join('');
        if (!this.center) {
          ctx.fillText(
            text,
            this.x,
            this.y + this.fontSize * (i + 1) * this.lineHeight
          );
        } else {
          let width = ctx.measureText(text).width;
          let x = this.x + (this.width - width) / 2;
          ctx.fillText(
            text,
            x,
            this.y + this.fontSize * (i + 1) * this.lineHeight
          );
        }
      });
      // 文字
      // return;
      // let len = Math.floor(this.width / this.fontSize);
      // let row = null;
      // let arr = [];
      // if (multiLine) {
      //   arr = this.text.split('\n');
      //   row = arr.length;
      // } else {
      //   row = Math.ceil(this.width / len);
      // }
      // //.ctx.measureText(text).width

      // if (row === 1) {
      //   ctx.fillText(this.text, this.x, this.y + this.fontSize);
      // } else {
      //   if (!multiLine) {
      //     for (let i = 0; i < row; i++) {
      //       if (this.maxLine > 0 && i >= this.maxLine) {
      //         break;
      //       }
      //       arr.push(this.text.substr(i * len, len));
      //     }
      //   }
      //   let textLen = arr.length - 1;
      //   arr.forEach((str, i) => {
      //     if (this.width && textLen === i) {
      //       let textWidth = ctx.measureText(str).width;
      //       const unitTextWidth = +(textWidth / str.length).toFixed(2);
      //       let max = 1000;
      //       if (textWidth > this.width) {
      //         max = Math.ceil((textWidth - this.width) / unitTextWidth);
      //       }
      //       console.warn(
      //         '最后一行',
      //         max,
      //         this.width,
      //         textWidth,
      //         unitTextWidth,
      //         str
      //       );
      //     }
      //     ctx.fillText(
      //       str,
      //       this.x,
      //       this.y + this.fontSize * (i + 1) * this.lineHeight
      //     );
      //   });
      // }
    } else {
      ctx.fillText(this.text, this.x, this.y + this.fontSize);
    }

    ctx.restore();
    console.timeEnd('poster Text');
  }
}

class Poster {
  constructor(c, vue) {
    let {
      width,
      height,
      scale,
      ctx,
      canvasId,
      header,
      pixelRatio = 1,
      backgroundColor,
    } = c;
    console.log(backgroundColor, c);
    this.config = c;
    this.width = width;
    this.height = height;
    this.scale = scale || 1;
    this.canvasId = canvasId;
    this.vue = vue;
    this.ctx = ctx;
    this.header = header;
    this.backgroundColor = backgroundColor;
    _ratio = pixelRatio;
  }
  setHeight(height) {
    this.height = height;
  }
  setWidth(width) {
    this.width = width;
  }
  async setBackground() {
    if (!this.config.backgroundColor && !this.config.backgroundImage) return;
    this.ctx.save();
    const config = {
      width: $to.rpx2px(this.width || 0),
      height: $to.rpx2px(this.height || 0),
    };
    if (this.backgroundColor) {
      this.ctx.setFillStyle(this.backgroundColor);
      this.ctx.fillRect(0, 0, config.width, config.height);
    }
    if (this.config.backgroundImage) {
      // try{
      let src = await downImg(this.config.backgroundImage, this.header);
      let [err, resInfo] = await uni.getImageInfo({ src: src });
      console.log('设置背景', this.config.backgroundImage, err, resInfo);

      let iHeight = 0;
      let sum = 0;
      if (!err && resInfo) {
        let iwProportion = $to.rpx2px(750) / resInfo.width;
        iHeight = resInfo.height * iwProportion;
      }
      if (iHeight >= config.height || !iHeight) {
        console.log('直接设置背景');
        config.height = iHeight;
        await new Image(
          Object.assign(config, {
            x: 0,
            y: 0,
            src: this.config.backgroundImage,
          })
        ).draw(this.ctx);
      } else {
        sum = Math.ceil(config.height / iHeight);
        config.height = iHeight;

        console.log('追加背景', sum, this.config.backgroundImage);
        for (let index = 0; index < sum; index++) {
          await new Image(
            Object.assign(config, {
              x: 0,
              y: config.height * index,
              src: this.config.backgroundImage,
            })
          ).draw(this.ctx);
        }
      }
      // }catch(e){}
    }

    this.ctx.restore();
  }

  async draw(steps) {
    let ctx = this.ctx;
    ctx.scale(this.scale, this.scale);
    let omTree = [];
    const typeMap = {
      img: Image,
      text: Text,
      rect: Rect,
    };

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

    await this.setBackground();
    for (let om of omTree) {
      try {
        await om.draw(ctx);
      } catch (err) {
        console.error(err);
      }
    }

    // ctx.draw(false);
    return new Promise((resolve, reject) => {
      // return;
      ctx.draw(false, async () => {
        setTimeout((_) => {
          uni.canvasToTempFilePath(
            {
              x: 0,
              y: 0,
              // width: this.width * this.scale,
              width: this.width,
              height: this.height,
              canvasId: this.canvasId,
              quality: 0.8,
              complete: (e) => {
                // console.log(e, this.canvasId, this.vue);
              },
              success: (res) => {
                resolve(res.tempFilePath);
              },
              fail: (err) => {
                reject(err);
              },
            },
            this.vue
          );
        }, 20);
        // if (!err) {
        //   resolve(res.tempFilePath);
        // }
        // console.log(err, res);
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
