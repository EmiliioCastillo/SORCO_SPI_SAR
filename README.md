🛠️ Para que el sistema haga uso del OSRM, primero usted debe configurar los contenedores locales en Docker.
1) Instalación de Docker

Debe confirmar que Docker esté instalado en su sistema operativo (Windows, Linux, macOS).

Acción:
Ejecute el siguiente comando en una terminal o consola:

docker --version

Esto debería devolver algo como:

Docker version 24.0.2, build cb74dfc

❌ Si no tiene Docker instalado:

    Diríjase a: https://docs.docker.com/get-docker/

    Siga los pasos de instalación para su sistema operativo.

    Una vez instalado, asegúrese de que Docker esté ejecutándose.

**2) Descargar los archivos .osm.pbf
**
Descargue el extracto de OpenStreetMap, por ejemplo, de Geofabrik 
https://download.geofabrik.de/south-america/argentina-latest.osm.pbf

3) Guardar el archivo en una carpeta especifica
Siga los comandos
mkdir ~/osrm-data
mv ~/Descargas/argentina-latest.osm.pbf ~/osrm-data/
cd ~/osrm-data

Dentro de la misma carpeta donde tenemos el archivo descargado, debemos:
Preprocesar el extracto con el perfil del vehículo e iniciar un servidor HTTP del motor de enrutamiento en el puerto 5000

Ejecutar:
 docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/car.lua /data/argentina-latest.osm.pbf

Debemos luego particionar y customizar la imagen:
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/argentina-latest.osrm
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/argentina-latest.osrm
Luego instalar el algoritmo MLD (Multi-Level Dijkstra)
docker run -t -i -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/argentina-latest.osrm

Por ultimo probar la instalación con coordenadas de prueba:
curl "http://127.0.0.1:5000/route/v1/driving/13.388860,52.517037;13.385983,52.496891?steps=true"
