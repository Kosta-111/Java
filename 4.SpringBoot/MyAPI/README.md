mvn -v

mvn clean package

mvn spring-boot:run

http://localhost:8082/swagger-ui/index.html

java -jar target/categoriesApi.jar

mvn clean verify

mvn test

mvn dependency:resolve

mvn clean

java -jar target/categoriesApi.jar --server.port=8082

```
docker build -t java-app . 
docker images --all
docker run -it --rm -p 5086:8082 --name java-app-container java-app
docker run -d --restart=always --name java-app-container -p 5086:8082 java-app
docker run -d --restart=always -v d:/volumes/spring/uploading:/app/images --name java-app-container -p 5086:8082 kosta111/java-app
docker run -d --restart=always -v /volumes/spring/uploading:/app/images --name java-app-container -p 5086:8082 kosta111/java-app
docker ps -a
docker stop java-app-container
docker rm java-app-container

docker images --all
docker rmi java-app

docker login
docker tag java-app:latest kosta111/java-app:latest
docker push kosta111/java-app:latest

docker pull kosta111/java-app:latest
docker ps -a
docker run -d --restart=always --name java-app-container -p 5086:8082 kosta111/java-app


docker pull kosta111/java-app:latest
docker images --all
docker ps -a
docker stop java-app-container
docker rm java-app-container
docker run -d --restart=always --name java-app-container -p 5086:8082 kosta111/java-app

---------------/etc/nginx/sites-available/--------------------------

server {
    server_name   slush.itstep.click *.slush.itstep.click;
    location / {
       proxy_pass         http://localhost:5086;
       proxy_http_version 1.1;
       proxy_set_header   Upgrade $http_upgrade;
       proxy_set_header   Connection keep-alive;
       proxy_set_header   Host $host;
       proxy_cache_bypass $http_upgrade;
       proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header   X-Forwarded-Proto $scheme;
    }
}

sudo nginx -t
sudo systemctl restart nginx
sudo systemctl status nginx
```

