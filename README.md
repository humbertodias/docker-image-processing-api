# Java-web-jhlabs

REST API for jhlabs filters generation.

Supported filters by [jhlabs](http://www.jhlabs.com/ip/filters/):
* POINTILLIZE
* BLUR
* WATER
* RIPPLE
* SHADOW


# Run

```
make run
```

# Browser

http://localhost:8080

![](doc/form.png)


![Filter](doc/output.png)


# Using terminal

```
curl "http://localhost:8080/filter" \
-F file=@doc/form.png \
-F name=pointillize \
-F output=png > output.png
```

or with Docker

```
make docker-build
make docker-run
```