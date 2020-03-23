/**
 * @Author: seekwe
 * @Date:   2019-11-11 15:04:12
 * @Last Modified by:   seekwe
 * @Last Modified time: 2019-12-19 18:24:59
 */

module.exports = {
  parser: require('postcss-comment'),
  plugins: [
    require('postcss-import'),
    require('autoprefixer')({
      remove: process.env.UNI_PLATFORM !== 'h5'
    }),
    require('@dcloudio/vue-cli-plugin-uni/packages/postcss')
  ]
};
