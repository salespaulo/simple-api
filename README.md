# simple-api
A simple Api in Scala using Finch and Quill :-]

## Enjoy :D
```
$> git clone https://github.com/salespaulo/simple-api.git
$> cd simmple-api
$> sbt
```

## Running
### Block mode
```
$> sbt clean package docker
$> docker-compose up --build
```
### Non-block mode
```
$> sbt clean package docker
$> docker-compose up --build -d
```

