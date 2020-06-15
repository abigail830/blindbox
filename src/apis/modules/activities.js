/*
 * @Author: seekwe
 * @Date: 2020-03-29 12:35:23
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-05-25 12:36:22
 */
export default {
  all: ['get', '/wx/activities/all'],
  get: [
    'get',
    function(activityId = 0) {
      return `/wx/activities/id/${activityId}`;
    },
  ],
  acceptNotify: [
    'get',
    function(activityId = 0) {
      return `/wx/activities/id/${activityId}/accept-notify`;
    },
  ],
  notice: [
    'put',
    function(activityId = 0, redirectPage = '') {
      return `/wx/activities/id/${activityId}/accept-notify?redirectPage=${redirectPage}`;
    },
  ],
};
