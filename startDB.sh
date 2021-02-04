docker run                                  \
  -d                                        \
  -p 5432:5432                          \
  -e POSTGRES_USER="root"               \
  -e POSTGRES_PASSWORD="root"       \
  -v "$PWD/pgdata":/var/lib/postgresql/data \
  postgres