# 微信爬虫
## 中间代理方式

​	proxy目录下通过自定义anyproxy（参见：https://github.com/alibaba/anyproxy）过滤配置，来抓取微信文章url、阅读数量、评论内容等。获取到url后将消息推送到rabbitmq，java消费端以url中的sn参数为依据，将获取到的内容保存到db中，图片&音频等素材下载到本地。

## 直接抓取

​	从微信客户端中复制公众号的历史文章url，放入到autoUrl.txt文件中，启动服务即可自动抓取，抓取到内容保存到db。