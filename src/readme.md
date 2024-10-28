```shell
ps -ef | grep courseware-0.0.1.jar
```
```shell
cd /tools/courseware
```
```shell
nohup java -jar courseware-0.0.1.jar > courseware.log 2>&1 &
```
121.40.154.188:10001/courseware/test.html
```dockerfile
docker stop mynginx
```
```dockerfile
docker remove mynginx
```
```dockerfile
docker run -d -p 9001:80 --name mynginx nginx
```
```dockerfile
docker cp /tools/nginx/dist/. mynginx:/usr/share/nginx/html/
```
```dockerfile
docker restart mynginx
```
121.40.154.188:9001
```dockerfile
docker stop vue
```
```dockerfile
docker remove vue
```
```dockerfile
docker run -d -p 9002:80 --name vue nginx
```
```dockerfile
docker cp /tools/nginx/dist/. vue:/usr/share/nginx/html/
```
```dockerfile
docker restart vue
```
121.40.154.188:9002
http://121.40.154.188:8080/draw/drawingboard/index.html
```dockerfile
docker run -d -p 80:80 --name vue nginx
docker cp /tools/nginx/dist/. vue:/usr/share/nginx/html/
docker restart vue
121.40.154.188:80
docker exec -it vue /bin/bash
server {
    listen 80;
    server_name 121.40.154.188;
    return 301 https://$host$request_uri;
}
echo 'server {
    listen 443 ssl;
    ssl on;
    ssl_certificate /etc/nginx/ssl/certificate.crt;
    ssl_certificate_key /etc/nginx/ssl/private.key;
    
    
    ssl_session_timeout 5m;
    ssl_protocols TLSv1.2 TLSv1.3;  
    ssl_ciphers 'ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-CHACHA20-POLY1305:kEDH+AESGCM:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256';  
    ssl_prefer_server_ciphers on;

    server_name 121.40.154.188;
    access_log /var/log/nginx/nginx.vhost.access.log;
    error_log /var/log/nginx/nginx.vhost.error.log;
    
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    location /.well-known/pki-validation/ {
        alias /usr/share/nginx/html/.well-known/pki-validation/;
    }

    location /courseware/ {
        proxy_pass http://121.40.154.188:10001;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        add_header Access-Control-Allow-Origin *;
        add_header Access-Control-Allow-Methods "GET, POST, OPTIONS";
        add_header Access-Control-Allow-Headers "Origin, Content-Type, Accept, Authorization";
    }
}' > /etc/nginx/conf.d/default.conf

```
```dockerfile
    echo 'server {
    listen 80;
    server_name 121.40.154.188;

    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }

    location /courseware/ {
        proxy_pass http://121.40.154.188:10001;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        add_header Access-Control-Allow-Origin *;
        add_header Access-Control-Allow-Methods "GET, POST, OPTIONS";
        add_header Access-Control-Allow-Headers "Origin, Content-Type, Accept, Authorization";
    }
}' > /etc/nginx/conf.d/default.conf
exit
docker restart mynginx
```
```dockerfile
echo 'server {
    listen 80;

    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }
}' > /etc/nginx/conf.d/default.conf
```
https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=aNXakJk2XQvxugcCFkEBqWMn&client_secret=QK2eZUGIqTIuJsTIriMUQXL9Smm7P1O2
```shell
sudo chmod -R 775 /tools/courseware/model
sudo chmod -R 664 /tools/courseware/model/*
```
```shell
sudo docker cp /tools/C7F00ECA16FB92116054BF4BB2620E2F.txt vue:/usr/share/nginx/html/.well-known/pki-validation/

```
### 验证域名
```shell
docker exec -it vue /bin/bash
mkdir -p /usr/share/nginx/html/.well-known/pki-validation/
docker cp /tools/C7F00ECA16FB92116054BF4BB2620E2F.txt vue:/usr/share/nginx/html/.well-known/pki-validation/
docker exec vue nginx -s reload
```
### 装证书
```shell
docker cp /tools/certificate.crt vue:/etc/nginx/ssl/certificate.crt
docker cp /tools/ca_bundle.crt vue:/etc/nginx/ssl/ca_bundle.crt
docker cp /tools/private.key vue:/etc/nginx/ssl/private.key

```
<Context docBase="/tools/courseware/model/" path="courseware" debug="0" reloadable="true" />