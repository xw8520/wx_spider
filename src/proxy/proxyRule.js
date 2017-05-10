var fs = require('fs');
var rabbitmq = require('./rabbitmq');

rabbitmq.init();

module.exports = {
    shouldInterceptHttpsReq :function(req){
        if(req.url.indexOf('mp.weixin.qq.com')>-1){
            return true;
        }else{
            return false;
        }
    },
    replaceServerResDataAsync: function (req, res, serverResData, callback) {
        if (req.url.indexOf('mp.weixin.qq.com/s?') > -1) {
            rabbitmq.publish(req.url, 0, serverResData.toString());
        }
        if (req.url.indexOf("mp.weixin.qq.com/mp/getappmsgext?__biz") > -1) {
            var refer = req.headers.referer;
            //console.log("refer："+refer)
            rabbitmq.publish(refer, 1, serverResData.toString());
        }
        if (req.url.indexOf("mp.weixin.qq.com/mp/appmsg_comment") > -1) {
            var refer = req.headers.referer;
            //console.log("refer："+refer)
            rabbitmq.publish(refer, 2, serverResData.toString());
        }

        callback(serverResData);
    }
}