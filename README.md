# Mini autorizador

A VR processa todos os dias diversas transações de Vale Refeição e Vale Alimentação, entre outras.
De forma breve, as transações saem das maquininhas de cartão e chegam até uma de nossas aplicações, conhecida como *autorizador*, que realiza uma série de verificações e análises. Essas também são conhecidas como *regras de autorização*. 

## Tecnologias Utilizadas

- [JAVA 17](https://www.java.com/pt-BR/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MongoDB](https://www.mongodb.com/)
- [Jacoco](https://www.jacoco.org/jacoco/trunk/index.html)
- [Snyk](https://snyk.io)
- [Redis](https://redis.io)
- [Sonar](https://www.sonarsource.com)
- [Docker](https://www.docker.com/)
- [Swagger](http://localhost:8080/swagger-ui/index.html#)

## Funcionalidades

- Criação de cartões
- Verificação de saldo
- Processamento de uma transação

## Rodar o projeto
Executar o clone do projeto
```
git clone https://github.com/rafaeloliveirati/miniautorizador.git
```

A partir da raiz do projeto executar o comando abaixo para rodar o docker:
```
cd miniautorizador/docker && docker-compose up -d
```

Abrir a IDE e executar a classe MiniAutorizadorApplication.java

Acesse a documentação do Swagger para testar os serviços:
```
http://localhost:8080/swagger-ui/index.html#
``` 

## Contratos dos serviços

### Criar novo cartão
```
Method: POST
URL: http://localhost:8080/cartoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senha": "1234"
}
```
#### Possíveis respostas:
```
Criação com sucesso:
   Status Code: 201
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
-----------------------------------------
Caso o cartão já exista:
   Status Code: 422
   Body (json):
   {
      "senha": "1234",
      "numeroCartao": "6549873025634501"
   } 
```

### Obter saldo do Cartão
```
Method: GET
URL: http://localhost:8080/cartoes/{numeroCartao} , onde {numeroCartao} é o número do cartão que se deseja consultar
```

#### Possíveis respostas:
```
Obtenção com sucesso:
   Status Code: 200
   Body: 495.15 
-----------------------------------------
Caso o cartão não exista:
   Status Code: 404 
   Sem Body
```

### Realizar uma Transação
```
Method: POST
URL: http://localhost:8080/transacoes
Body (json):
{
    "numeroCartao": "6549873025634501",
    "senhaCartao": "1234",
    "valor": 10.00
}
```

#### Possíveis respostas:
```
Transação realizada com sucesso:
   Status Code: 201
   Body: OK 
-----------------------------------------
Caso alguma regra de autorização tenha barrado a mesma:
   Status Code: 422 
   Body: SALDO_INSUFICIENTE|SENHA_INVALIDA|CARTAO_INEXISTENTE (dependendo da regra que impediu a autorização)
```