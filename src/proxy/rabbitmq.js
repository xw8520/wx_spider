var amqp = require('amqplib/callback_api');
var circularJson = require('circular-json');
var config = require('./config');
var newsChannel = null;

module.exports = {
    init: function () {
        amqp.connect(config.mq, function (err, conn) {
            conn.createChannel(function (err, ch) {
                newsChannel = ch;
                ch.assertQueue(config.newsQueue, {durable: false});
            });
        });
    },
    publish: function (url, type, body) {
        if (newsChannel != null) {
            console.log(type)
            newsChannel.sendToQueue(config.newsQueue,
                new Buffer(circularJson.stringify({
                    url: url,
                    type: type,
                    body: body
                }))
            );
        }
    }
}