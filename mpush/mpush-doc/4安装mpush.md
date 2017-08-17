一、Linux安装Mpush

> \[root@localhost app\]\# tar xf mpush-release-0.0.5.tar.gz
> 
> \[root@localhost app\]\# ln -s mpush-0.0.5 mpush
> 
> 编辑Mpush配置文件\(注意，只需要修改mpush.conf配置文件即可，不需要修改reference.conf\)，修改默认提供的Redis、Zookeeper服务器地址和端口信息\(因为我redis和zookeeper直接部署在本机，所以不用修改\)。
> 
> \[root@localhost app\]\#vim conf\/mpush.conf
> 
> 
> 
> 给脚本执行权限
> 
> \[root@localhost mpush\]\# chmod +x bin\/\*.sh
> 
> \[root@localhost mpush\]\# bin\/mp.sh start

![](/assets/mpush01.png)

查看启动情况和端口监听情况

> \[root@localhost mpush\]\#netstat -tplan \| grep java

![](/assets/mpush02.png)

可以看到，主要端口\(3000、3001、3002\)已经启动

查看日志\(如果有错误，会在日志中提现\)

![](/assets/mpush03.png)

二、Windows安装Mpush

1.解压mpush-release-0.0.5.tar.gz

![](/assets/mpush04.png)

2.编辑Mpush配置文件\(注意，只需要修改mpush.conf配置文件即可，不需要修改reference.conf\)，修改默认提供的Redis、Zookeeper服务器地址和端口信息\(因为我redis和zookeeper直接部署在本机，所以不用修改\)。



3.使用cmd进入到bin目录，启动Mpush\(使用如下脚本java -Dmp.conf=D:\mpush\mpush-0.0.5\conf\mpush.conf -jar bootstrap.jar\)

![](/assets/mpush05.png)

![](/assets/mpush06.png)







