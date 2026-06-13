daemon off;
user nginx;
pid /var/run/nginx.pid;
worker_processes 1;
pcre_jit on;
error_log /proc/1/fd/1 error;

events {
    worker_connections 512;
}

http {
    access_log off;
    client_max_body_size 4G;
    default_type application/octet-stream;
    keepalive_timeout 65;
    sendfile off;
    server_tokens off;
    tcp_nodelay on;
    tcp_nopush on;

    map $http_upgrade $connection_upgrade {
        default upgrade;
        '' close;
    }

    resolver 127.0.0.11 ipv6=off;

    server {
        listen 8099 default_server;
        root /dev/null;
        server_name _;
        absolute_redirect off;
        port_in_redirect off;

        location / {
            allow 172.30.32.2;
            deny all;

            set $target "{{ .server }}";

            proxy_pass $target;
            proxy_http_version 1.1;
            proxy_ignore_client_abort off;
            proxy_read_timeout 86400s;
            proxy_redirect {{ .server }}/ $http_x_ingress_path/;
            proxy_redirect {{ .server }} $http_x_ingress_path;
            proxy_redirect / $http_x_ingress_path/;
            proxy_send_timeout 86400s;
            proxy_max_temp_file_size 0;
            proxy_no_cache 1;
            proxy_cache_bypass 1;

            proxy_hide_header X-Frame-Options;
            proxy_hide_header Frame-Options;
            proxy_hide_header Content-Security-Policy;
            proxy_hide_header Content-Security-Policy-Report-Only;
            proxy_hide_header Cross-Origin-Opener-Policy;
            proxy_hide_header Cross-Origin-Embedder-Policy;
            proxy_hide_header Cross-Origin-Resource-Policy;

            proxy_cookie_domain ~.* $host;
            proxy_cookie_path / /;

            sub_filter_once off;
            sub_filter_types text/css application/javascript application/json;
            sub_filter '{{ .server }}/' '$http_x_ingress_path/';
            sub_filter '{{ .server }}' '$http_x_ingress_path';
            sub_filter 'href="/' 'href="$http_x_ingress_path/';
            sub_filter 'src="/' 'src="$http_x_ingress_path/';
            sub_filter 'action="/' 'action="$http_x_ingress_path/';
            sub_filter 'url(/' 'url($http_x_ingress_path/';

            add_header Cache-Control "no-store, no-cache, must-revalidate, proxy-revalidate, max-age=0";
            add_header Pragma "no-cache";
            add_header Expires 0;

            proxy_set_header Accept-Encoding "";
            proxy_set_header Connection $connection_upgrade;
            proxy_set_header Host $proxy_host;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Host $http_host;
            proxy_set_header X-Forwarded-Prefix $http_x_ingress_path;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-External-Path $http_x_ingress_path;
            proxy_set_header X-Ingress-Path $http_x_ingress_path;
            proxy_set_header X-NginX-Proxy true;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header GROCY-API-KEY "{{ .grocy_api_key }}";
        }
    }
}
