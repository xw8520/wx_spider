var fs = require('fs');
var rabbitmq = require('./rabbitmq');

rabbitmq.init();

module.exports = {
    replaceServerResDataAsync: function (req, res, serverResData, callback) {
        if (req.url.indexOf('mp.weixin.qq.com/s?') > -1) {
            rabbitmq.publish(req.url, 0, serverResData.toString());
        }
        if (req.url.indexOf("mp.weixin.qq.com/mp/getappmsgext?__biz") > -1) {
            rabbitmq.publish(req.url, 1, serverResData.toString());
        }
        if (req.url.indexOf("mp.weixin.qq.com/mp/appmsg_comment") > -1) {
            rabbitmq.publish(req.url, 2, serverResData.toString());
        }

        callback(serverResData);
    }
}