🛠️ Configuración del entorno OSRM con Docker

Para que el sistema haga uso del motor de rutas OSRM (Open Source Routing Machine), debe configurar los contenedores locales en Docker siguiendo estos pasos:
1️⃣ Instalación de Docker

Debe asegurarse de que Docker esté instalado en su sistema operativo (Windows, Linux, macOS).

📍 Acción:
Ejecute el siguiente comando en una terminal o consola:

docker --version


Debería obtener un resultado como:

Docker version 24.0.2, build cb74dfc

❌ Si no tiene Docker instalado:

    Diríjase a: https://docs.docker.com/get-docker/

    Siga los pasos de instalación según su sistema operativo.

    Una vez instalado, asegúrese de que Docker esté ejecutándose.


2️⃣ Descargar los archivos .osm.pbf

Debe obtener un archivo con los datos de OpenStreetMap (OSM) correspondiente a la región deseada.

🔗 Ejemplo recomendado (Argentina):
Descargar desde Geofabrik:
https://download.geofabrik.de/south-america/argentina-latest.osm.pbf


3️⃣ Crear carpeta de trabajo y mover el archivo

Ejecute los siguientes comandos en su terminal:

mkdir ~/osrm-data
mv ~/Descargas/argentina-latest.osm.pbf ~/osrm-data/
cd ~/osrm-data


Esto crea una carpeta de trabajo (osrm-data) y ubica allí el archivo descargado.

4️⃣ Procesar y levantar el servidor OSRM

Con los datos en la carpeta correcta, ejecute los siguientes pasos para preparar y levantar el servicio de enrutamiento.
📍 Extracción del mapa con el perfil de vehículo:

docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/car.lua /data/argentina-latest.osm.pbf

Particionar y personalizar:
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/argentina-latest.osrm
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/argentina-latest.osrm

Iniciar el servidor OSRM con el algoritmo MLD (Multi-Level Dijkstra):
docker run -t -i -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/argentina-latest.osrm

Esto levanta un servidor HTTP local en el puerto 5000.

✅ Verificar que el servicio funciona



Puede hacer una prueba ejecutando el siguiente comando (usando coordenadas de ejemplo):

curl "http://127.0.0.1:5000/route/v1/driving/13.388860,52.517037;13.385983,52.496891?steps=true"
Si el servidor está corriendo correctamente, recibirá una respuesta en formato JSON con los pasos de la ruta calculada.



![WhatsApp Video 2026-06-10 at 19 44 26](https://github.com/user-attachments/assets/a9173a12-67fb-4a24-a23e-6f835175fa6d)

