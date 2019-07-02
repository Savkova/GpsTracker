Configuration
================================

Server uses for storing data H2 database. You must create the database locally.

The file which stores all the settings for the server: <b>./bin/config.properties</b>.
It must contain the following required properties (values are for example):

server properties
-----------------
server_name=GPS Tracker Server

server_port=8080

server_ip=192.168.0.1

database properties
-------------------
database_url=jdbc:h2:./storage;FILE_LOCK=SOCKET

database_user=admin

database_password=adminpass

