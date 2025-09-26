# franchise-handler-ce API

## Requisitos
- Java 17
- Maven
- Docker (opcional)

## Ejecutar local (sin Docker)
1. Levantar Mongo local en 27017
2. mvn clean package
3. java -jar target/franchise-handler-ce-0.0.1-SNAPSHOT.jar 

## Ejecutar con Docker Compose
1. mvn package -DskipTests
2. docker-compose up --build

## Endpoints principales

La aplicación escucha en: **http://localhost:8080**

## Franquicias
- **POST** `/api/franchises` → crea una franquicia  
- **PATCH** `/api/franchises/{franchiseId}` → actualiza el nombre de una franquicia  
- **GET** `/api/franchises` → lista todas las franquicias  
- **GET** `/api/franchises/{franchiseId}` → obtiene una franquicia por ID  

## Sucursales (Branches)
- **POST** `/api/franchises/{franchiseId}/branches` → crea una sucursal  
- **PATCH** `/api/franchises/{franchiseId}/branches/{branchId}` → actualiza el nombre de una sucursal  
- **GET** `/api/franchises/{franchiseId}/branches` → lista sucursales de una franquicia  

## Productos
- **POST** `/api/franchises/{franchiseId}/branches/{branchId}/products` → crea un producto  
- **PATCH** `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` → actualiza el nombre de un producto  
- **PATCH** `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` → actualiza el stock de un producto  
- **DELETE** `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` → elimina un producto  
- **GET** `/api/franchises/{franchiseId}/branches/{branchId}/products` → lista productos de una sucursal  

## Reportes
- **GET** `/api/franchises/{franchiseId}/top-products` → obtiene el producto con mayor stock en cada sucursal


## Puedes encontrar el archivo de postman en el repositorio

## Ejecutar test:
En la carpeta del proyecto:
- **Windows** `mvnw.cmd test`
- **Linux/Mac** `./mvnw test`