
# GestBanda

GestBanda es un proyecto dedicado a la gestión de las bandas principalmente cofrades.

GestBanda permite llevar una gestión de los componentes, voces, instrumentos, trajes y eventos de una banda

Este es un proyecto desarollado en Flutter con Riverpod. El backend esta desarrollado en Java con Spring.


## Instalación

Para instalar este proyecto, previamente deberemos tener descargado [JDK 21](https://www.oracle.com/es/java/technologies/downloads/#jdk21-windows) y [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads).

Por defecto la aplicación está configurada para usar el puerto por defecto de PostgreSQL **(5432)** que el usuario de postgre sea **postgres** y la password **admin**.

Esta configuración se puede cambiar desde **resources/application.properties** en los values:

```bash
  spring.datasource.username=postgres
  spring.datasource.password=admin
```


Accederemos mediante nuestro IDE favorito a la BBDD y ejecutaremos el .sql encontrado en **resources/db.scripts/bbdd.sql**

Tras esto compilaremos e iniciaremos nuestra app.

## Autor

- [@ManuelJesusPascual](https://github.com/ManuelJesusPascual) Desarrollador principal del proyecto
## Contribuidor

- [@DevJoseEstrada](https://github.com/DevJoseEstrada): Arquitectura del proyecto, soporte a las dudas y buenas practicas.

