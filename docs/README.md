# (주)디자인아이엠 iCMS-1.0.0

## 1. IPv4
Server Arguments > -Djava.net.preferIPv4Stack=true

## 2. Mod
dev : Server Arguments > -Dglobals.properties.mode=dev  
prod : Server Arguments > -Dglobals.properties.mode=prod

## 3. URL
WebInterceptor.java > preHandle > url 설정  
search : README.3

## 4. Server config
server.xml > Connector tag
property 추가 maxSwallowSize="-1"
