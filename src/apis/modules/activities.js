/*
 * @Author: seekwe
 * @Date: 2020-03-29 12:35:23
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-29 21:14:06
 */
export default {
  all: ['get', '/wx/activities/all'],
  get: [
    'get',
    function(activityId = 0) {
      return `/wx/activities/id/${activityId}`;
    }
  ],
  notice: [
    'put',
    function(activityId = 0, redirectPage = '') {
      return `/wx/activities/id/${activityId}/accept-notify?redirectPage=${redirectPage}`;
    }
  ]
};
