# 盲盒小程序

## 开发

```bash
# 先安装依赖
npm install
# or 考虑到国内环境可以直接使用淘宝源
npm install --registry=https://registry.npm.taobao.org


# 启动项目，然后使用微信开发者工具 导入项目 -> dist/dev/mp-weixin
npm run serve
```

## 发布

```bash
# 编译生产版本然后使用微信开发者工具 导入项目 -> dist/build/mp-weixin ，再上传发布
npm run build
```
