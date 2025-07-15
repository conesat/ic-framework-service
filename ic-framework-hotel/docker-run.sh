docker run -d \
-v /apps/ic-hotel:/app \
-p 8080:8080 \
--network app-network \
--name hotel \
ic-hotel
