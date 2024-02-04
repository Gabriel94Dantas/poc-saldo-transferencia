# Desafio Saldo e Transferência

## Stack tecnológica

- Java 17
- Spring Boot 3.1.3
- Postgres
- Redis
- Kafka
- Zookeeper

## Organização do Projeto

O projeto foi organizado da seguinte maneira

### Pasta .devcontainer

Essa pasta contém as configurações necessárias para utilização do Codespace, ferramento de cloud code do Github.

### Pasta docs

Essa pasta tem o desenho de arquitetura tanto em imagem, quanto em xml e ainda possui um documento contendo as justificativas da escolha da stack tecnológica frente o desafio proposto.

### src/main/java/.../notificacaoBacen

Essa pasta específica do projeto tem por objetivo simular a construção de um microsserviço que é responsável por ler os eventos gerados pelo projeto principal e encaminhá-los para o BACEN e enviar os eventos tratados para um outro tópico.

### src/main/java/.../transferenciaStatus

Essa pasta específica do projeto tem por objetivo simular a construção de um microsserviço que é responsável por ler os eventos gerados pela notificação e executar a atualização do status na base de dados

## Como executar

O projeto está construído para ser completamente funcional no Docker para evitar problemas em montar o ambiente local.

É necessário ter na máquina o maven e o jdk 17, além do Docker e do docker-compose.

Basta seguir os passos abaixo para executar o projeto na raiz do projeto

``` mvn clean install ```

``` docker-compose up -d --build ```

No docker-compose tem as informações de senhas e usuários e uma ferramenta chamada adminer onde pode ser feita a visualização dos dados no Postgres.

O projeto estará sendo executado na porta 8080 e possui o swagger para facilitar a interação no seguinte path:

``` http://localhost:8080/swagger-ui/index.html ```

O projeto está separado em dois web controllers, um para saldo e um outro para transferencias.

No controller Saldo temos um endpoint chamado massaTeste, ao executar esse endpoint uma massa de teste será carregado na base de dados e irá inicializar as threads tanto do notificaBacen, quanto do transferenciaStatus.

Temos ainda um endpoint pra consultar Saldo e um para executar transferências.

## Testes Unitários

WIP

Não foram executados por conta do tempo que eu tinha disponível para executar o desafio, mas será feito a posteriori.

## Conclusão

O desafio foi uma importante etapa para construção de um portfólio pessoal e como uma prova de conceito de como EDA (Event Driven Architecture ou Arquitetura Orientada a Eventos) pode trazer consigo duas das três características de um software distribuído abordada no teorema de CAP (Consistency, Availability e Partial Tolerance) que são Disponibilidade e Tolerância Parcial.

Além disso, importante dizer que o código foi escrito seguindo parte do conceito SOLID e aplicando principalmente o Singleton design pattern e do ponto de vista arquitetural tentou-se seguir o conceito de microsserviço e utilizando tecnologias que fossem opensource e multi cloud.