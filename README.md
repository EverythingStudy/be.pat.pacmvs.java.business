

## 病理图片项目-WEB后端


http://192.168.52.129:8280/RD/be.PathMedics.SaaS.java.business.git


防火墙开放以下端口
sudo firewall-cmd --zone=public --add-port=9998/tcp --permanent
sudo firewall-cmd --zone=public --add-port=82/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9001/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9002/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9003/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9999/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5000/tcp --permanent
sudo firewall-cmd --zone=public --add-port=80/tcp --permanent
sudo firewall-cmd --zone=public --add-port=8080/tcp --permanent
sudo firewall-cmd --zone=public --add-port=8848/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9848/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9849/tcp --permanent
sudo firewall-cmd --zone=public --add-port=6379/tcp --permanent
sudo firewall-cmd --zone=public --add-port=3306/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9100/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9200/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9201/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9202/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9203/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9300/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9000/tcp --permanent
sudo firewall-cmd --zone=public --add-port=6379/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9300/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9990/tcp --permanent
sudo firewall-cmd --zone=public --add-port=3306/tcp --permanent
sudo firewall-cmd --zone=public --add-port=3307/tcp --permanent
sudo firewall-cmd --zone=public --add-port=1514/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5000/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9002/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9003/tcp --permanent
sudo firewall-cmd --zone=public --add-port=15672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=4369/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5671/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=15671/tcp --permanent
sudo firewall-cmd --zone=public --add-port=15691/tcp --permanent
sudo firewall-cmd --zone=public --add-port=15692/tcp --permanent
sudo firewall-cmd --zone=public --add-port=25672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=15672/tcp --permanent
sudo firewall-cmd --zone=public --add-port=82 /tcp --permanent
sudo firewall-cmd --zone=public --add-port=9990/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9998/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9203/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9003/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9201/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9002/tcp --permanent
sudo firewall-cmd --zone=public --add-port=8080/tcp --permanent
sudo firewall-cmd --zone=public --add-port=9999/tcp --permanent
sudo firewall-cmd --zone=public --add-port=5000/tcp --permanent
sudo firewall-cmd --zone=public --add-port=8000/tcp --permanent
sudo firewall-cmd --zone=public --add-port=80/tcp --permanent
重启防火墙
service firewalld restart

查看开放端口
sudo firewall-cmd --permanent --list-port



## 更新日志
2022-07-13 16:04:29
后端系统拆分为system为固定和bussiness已经完成了。

新的bussiness项目 Gitlab:   http://192.168.52.129:8280/lbf-dev/staitech-anno.git   
jenkins:   http://172.31.2.63:7080/job/staitech-anno/
CICD自动构建已经配置完成。

网关：     192.168.52.108:8080/anno/xxx/xxxx

前端项目、图片、标注、审核 相关的接口需要把system改成anno，其它接口复测一下。

后端需要重新拉取新的项目，昨天下午2点之后提交的代码需要自己手动再重写一下，项目进行了重大重构，注意编码规范。

原项目中业务代码明天删除。

请多测试，多调试，有问题及时反馈。

不走网关端口：9998



docker启动rabbitmq
```shell

docker pull 192.168.52.112/library/rabbitmq:3-management
 
 
docker run -d --hostname localhost --name rabbitmq -p 15672:15672 -p 5672:5672  192.168.52.112/library/rabbitmq:3-management
```
