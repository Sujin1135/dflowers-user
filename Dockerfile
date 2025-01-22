FROM fluent/fluentd:v1.17-debian-1

USER root

# Elasticsearch 플러그인 설치
RUN gem install fluent-plugin-elasticsearch --no-document

# 필요한 경우 추가 플러그인 설치
# RUN gem install fluent-plugin-rewrite-tag-filter --no-document

# 권한 복구
USER fluent