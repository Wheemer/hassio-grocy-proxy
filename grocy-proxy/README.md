# Grocy Proxy

This add-on creates a Home Assistant ingress/sidebar entry for a Grocy server running somewhere else.

It does not run Grocy. It proxies the Grocy web UI through Home Assistant so the left-menu item works locally and through Home Assistant remote access.

## Options

```yaml
server: "http://192.168.1.50:9283"
grocy_api_key: ""
```

- `server`: URL for the external Grocy web UI. Do not use a trailing slash.
- `grocy_api_key`: optional Grocy API key to forward as `GROCY-API-KEY`.

After starting the add-on, open **Grocy** from the Home Assistant sidebar.
