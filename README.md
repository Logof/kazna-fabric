# kazna-fabric

Для запуска потребуется hyperledger

1. Сделать клон репозитория hyperledger samples<br>
``git clone https://github.com/hyperledger/fabric-samples.git``

2. Сделать клон этого репозитория<br>
``git clone https://github.com/Logof/kazna-fabric.git``

3. Скопировать каталоги<br>
``cp -r <путь до репозитория>/kazna-fabric <путь до репозитория>/fabric-samples``

4. Запустить<br>
`` cd <путь до репозитория>/fabric-samples/fabkazna``<br>
`` bash ./startFabric.sh``<br>
Как результат будет запущена сеть hyperledger'а и rest сервер на 3000 порту, для работы с сетью и контрактом