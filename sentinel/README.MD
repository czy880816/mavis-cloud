## sentinel 控制台监控
1. dashboard启动命令
java -Dserver.port=8080 
-Dcsp.sentinel.dashboard.server=localhost:8080 
-Dcsp.sentinel.api.port=8719
-Dproject.name=sentinel-dashboard 
-jar sentinel-dashboard-1.4.0.jar
2. 客户端
指定dashboard地址以及http服务器的端口
-Dcsp.sentinel.dashboard.server=localhost:8080 
-Dcsp.sentinel.api.port=8081

