
# Vendas

## Descrição

É uma aplicação desenvolvida em Java utilizando Swing para gerenciamento de vendas, cadastro de produtos e clientes. A aplicação se conecta a um banco de dados MySQL hospedado na nuvem.

## Funcionalidades

- Gerenciamento de **vendas**.
- **Cadastro de produtos** e **clientes**.

## Requisitos

- **Java 21** ou superior.
- **Maven** para gerenciamento de dependências e build.
- **MySQL** (configurado na nuvem, sem necessidade de configuração adicional).

## Como compilar

1. Clone este repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```

2. Navegue até a pasta do projeto:
   ```bash
   cd sales
   ```

3. Compile o projeto com Maven:
   ```bash
   mvn clean package
   ```

## Como executar

- Executando o arquivo JAR gerado:
  ```bash
  java -jar target/sales-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```


## Banco de Dados

A aplicação já está configurada para utilizar um banco de dados MySQL hospedado na nuvem. Não há necessidade de configuração adicional.

## Dependências

- **Maven**: Para gerenciar dependências e build.
- **Swing**: Para a criação da interface gráfica.
- **MySQL**: Banco de dados em nuvem.

## Autor

Desenvolvido por Rafael Laranjeiras.