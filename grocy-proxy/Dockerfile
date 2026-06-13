ARG BUILD_FROM
FROM $BUILD_FROM

RUN apk add --no-cache nginx

COPY nginx.conf.gtpl /
COPY entrypoint.sh /

RUN chmod a+x /entrypoint.sh

ARG BUILD_ARCH
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_VERSION

LABEL \
  io.hass.name="${BUILD_NAME}" \
  io.hass.description="${BUILD_DESCRIPTION}" \
  io.hass.arch="${BUILD_ARCH}" \
  io.hass.type="addon" \
  io.hass.version=${BUILD_VERSION}

ENTRYPOINT ["/entrypoint.sh"]
