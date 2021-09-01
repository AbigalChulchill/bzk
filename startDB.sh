docker run                                  \
  -d                                        \
  -p 5432:5432                          \
  -e POSTGRES_USER="root"               \
  -e POSTGRES_PASSWORD="root"       \
  postgres
  
  
 docker run \
  -d \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME="root" \
  -e MONGO_INITDB_ROOT_PASSWORD="root" \
  -e MONGO_INITDB_DATABASE="bzklog" \
  -it --memory="512m" \
  mongo:3.4.2
  