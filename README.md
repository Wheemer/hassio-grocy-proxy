<div align="center">

# Home Assistant Grocy Proxy

### Add an external Grocy server to the Home Assistant sidebar through ingress

[![Home Assistant Add-on](https://img.shields.io/badge/HOME%20ASSISTANT-ADD--ON-41BDF5?style=for-the-badge&logo=home-assistant&logoColor=white&labelColor=555555)](https://www.home-assistant.io/)
[![Latest release](https://img.shields.io/github/v/release/Wheemer/hassio-grocy-proxy?style=for-the-badge&logo=github&logoColor=white&label=RELEASE&labelColor=555555&color=22C55E)](https://github.com/Wheemer/hassio-grocy-proxy/releases/latest)
[![License](https://img.shields.io/github/license/Wheemer/hassio-grocy-proxy?style=for-the-badge&labelColor=555555&color=64748B)](LICENSE)

<p>
  <strong>Ingress proxy for Grocy:</strong><br>
  Runs inside Home Assistant, points at Grocy somewhere else, and adds a real sidebar item.
</p>

[![Donate with PayPal](https://img.shields.io/badge/PayPal-Support%20this%20work-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.me/wheemer)

</div>

This repository contains a Home Assistant add-on that works like the Zigbee2MQTT Proxy add-on. It does **not** run Grocy. It creates a Home Assistant ingress panel that proxies to an already-running Grocy server on another machine.

Use it when:

- Grocy runs on a NAS, Docker host, VM, or another server.
- The Grocy Home Assistant integration is already installed for entities and services.
- You want a **Grocy** item in the Home Assistant left menu.
- You want Grocy reachable through Home Assistant remote access without exposing Grocy separately.

## What It Does

- Adds a Home Assistant ingress/sidebar entry named **Grocy**.
- Proxies the Grocy web UI from an external server.
- Keeps redirects inside the Home Assistant ingress path.
- Rewrites Grocy asset URLs so CSS, JavaScript, icons, and manifests load through ingress.
- Supports optional forwarding of `GROCY-API-KEY`.

## Install

Add this repository to the Home Assistant add-on store:

```text
https://github.com/Wheemer/hassio-grocy-proxy
```

Then install **Grocy Proxy**.

## Configure

Set `server` to the URL where Home Assistant can reach your Grocy web UI.

```yaml
server: "http://192.168.1.50:9283"
grocy_api_key: ""
```

Do not add a trailing slash.

If you already use the Grocy Home Assistant integration, use the same host and port from that integration.

## Start

1. Save the add-on configuration.
2. Start **Grocy Proxy**.
3. Enable **Show in sidebar** if Home Assistant has not enabled it already.
4. Open **Grocy** from the Home Assistant left menu.

If you use a custom sidebar mod, allow/show the new **Grocy** entry there after installing.

## Options

| Option | Required | Description |
| --- | --- | --- |
| `server` | Yes | Local URL for the external Grocy web UI. Example: `http://192.168.1.50:9283`. |
| `grocy_api_key` | No | Optional Grocy API key forwarded as `GROCY-API-KEY`. The Grocy UI still uses Grocy's normal login/session behavior. |

## Why Not Use A Webpage Dashboard?

A normal Webpage dashboard can work if Grocy is directly reachable and embeds cleanly. Ingress is better when you want Grocy to behave like an add-on panel:

- Home Assistant owns the remote access path.
- You only expose Home Assistant, not Grocy.
- The sidebar entry is a real add-on ingress panel.
- Mixed HTTP/HTTPS and frame issues are easier to avoid.

## Troubleshooting

### Blank Screen

Restart the add-on once after changing the `server` option. Then hard-refresh Home Assistant.

This proxy rewrites Grocy redirects and asset links into the Home Assistant ingress path. If the Grocy page is still blank, check the add-on log for Nginx errors and confirm Home Assistant can reach the configured `server` URL.

### 502 Bad Gateway

Home Assistant cannot reach Grocy at the configured URL. Check:

- Grocy is running.
- The `server` URL is reachable from the Home Assistant host.
- The URL includes the correct port.
- There is no trailing slash.

### Login Loops

Clear the browser session for Home Assistant/Grocy and log in again through the sidebar panel. If Grocy sits behind another reverse proxy, make sure that proxy is not forcing an incompatible cookie domain or path.

## Development

The add-on is intentionally small:

- `grocy-proxy/config.yaml` defines the Home Assistant add-on metadata.
- `grocy-proxy/build.yaml` selects the Home Assistant base images.
- `grocy-proxy/nginx.conf.gtpl` renders the runtime Nginx reverse proxy config.
- `grocy-proxy/entrypoint.sh` renders and starts Nginx.

## Support

If this helped you, you can support the work here:

[https://www.paypal.me/wheemer](https://www.paypal.me/wheemer)
