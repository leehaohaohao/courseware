#!/bin/bash
# vue_nginx_deploy.sh
# 部署 Vue 项目到 Nginx 容器

set -e

CONTAINER_NAME="vue"
HOST_IP="121.40.154.188"
HOST_PORT="9002"
NGINX_HTML_DIR="/usr/share/nginx/html"
LOCAL_DIST_DIR="/tools/nginx/dist"

echo "停止并删除旧容器..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm -f $CONTAINER_NAME 2>/dev/null || true

echo "启动新 Nginx 容器..."
docker run -d -p $HOST_PORT:80 --name $CONTAINER_NAME nginx

echo "复制 Vue 构建文件到容器..."
docker cp $LOCAL_DIST_DIR/. $CONTAINER_NAME:$NGINX_HTML_DIR/

echo "写入 Nginx 配置..."
TMP_CONF=$(mktemp)
cat > $TMP_CONF <<EOF
server {
    listen 80;
    server_name $HOST_IP;

    location / {
        root $NGINX_HTML_DIR;
        index index.html index.htm;
        try_files \$uri \$uri/ /index.html;
    }

    location /courseware/ {
        proxy_pass http://$HOST_IP:10001;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        add_header Access-Control-Allow-Origin *;
        add_header Access-Control-Allow-Methods "GET, POST, OPTIONS";
        add_header Access-Control-Allow-Headers "Origin, Content-Type, Accept, Authorization";
    }
}
EOF

docker cp $TMP_CONF $CONTAINER_NAME:/etc/nginx/conf.d/default.conf
rm $TMP_CONF

echo "重启 Nginx 容器..."
docker restart $CONTAINER_NAME

echo "部署完成！访问 http://$HOST_IP:$HOST_PORT"
