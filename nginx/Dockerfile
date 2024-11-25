FROM nginx:latest
RUN apt-get update && apt-get install -y gettext
COPY nginx.template.conf /etc/nginx/nginx.conf