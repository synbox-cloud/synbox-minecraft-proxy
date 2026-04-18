# SynboxProxy

`SynboxProxy` ist ein Velocity-Proxy-Plugin für die Synbox-Umgebung. Es bindet die Synbox API an den Proxy an, startet einen kleinen Webserver für Webhooks und Magic-Link-Anfragen und verarbeitet Server-Events für die Synbox-Integration.

## Maven-Dependency

```xml
<repositories>
    <repository>
        <id>synbox</id>
        <url>https://registryhub.synbox.gg/repository/synbox/</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>gg.synbox.minecraft.proxy</groupId>
    <artifactId>synboxproxy</artifactId>
    <version>1.0</version>
</dependency>
```

