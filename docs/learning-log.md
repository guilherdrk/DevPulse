Anotações

- Os dados de acesso estão no arquivo só por ser ambiente local de estudo. Na fase de deploy vamos tirar isso do código, usando variáveis de ambiente.

1. Por que o @WebMvcTeste passou sem banco, mas o @SpringBootTest não?

- O @WebMvcTeste não sobe o banco, pois ele é só um teste de controller.
- O @SpringBootTest sobe a aplicação inteira, pois ele é um teste de integração.

2. Qual a diferença entre class file version 65 e 61?

- 61 = Java 17
- 65 = Java 21

ddl-auto: troque para validate. Com update, o Hibernate cria e altera tabelas por conta próproa, e isso briga com o Flyway. A regra é: o Flyway manda no schema, o Hibernate só confere.
