### 概述
基于MQTT协议的推送套件

mqtt-push-sdk 为开发SDK 

mqtt-push-server为推送中间件

服务端和客户端设计在低流量网络不稳定的设备上。

自带有心跳检测和消息重发。支持消息服务质量QOS。

消息订阅支持父子主题，也就是说我给/root主题发布一个消息
订阅了/root以及/root的子级都会收到这个消息。

![输入图片说明](https://git.oschina.net/uploads/images/2017/0524/182721_de8e903d_75292.png "在这里输入图片标题")

