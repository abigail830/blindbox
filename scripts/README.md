# Server setup

## Setup python 2 & 3
``` bash
dnf install python3
dnf install python2
cd /usr/bin
ln -s python2.7 python
```

## Install Java8
``` bash
yum install java-1.8.0-openjdk-headless java-1.8.0-openjdk java-1.8.0-openjdk-devel
```

## Install Nginx

Config under /etc/nginx/conf.d

``` bash
yum install nginx
systemctl start nginx.service
systemctl enable nginx.service
```

## Firewall

``` bash
systemctl start firewalld
systemctl enable firewalld
firewall-cmd --permanent --zone=public --add-service=https --add-service=http
firewall-cmd --zone=public --permanent --add-port=8080/tcp
firewall-cmd --reload 
```

## Docker

``` bash
wget https://download.docker.com/linux/centos/7/x86_64/stable/Packages/docker-ce-18.03.1.ce-1.el7.centos.x86_64.rpm
yum install docker-ce-18.03.1.ce-1.el7.centos.x86_64.rpm
systemctl enable docker
systemctl start docker
```

