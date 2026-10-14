Anotações

- Os dados de acesso estão no arquivo só por ser ambiente local de estudo. Na fase de deploy vamos tirar isso do código, usando variáveis de ambiente.

1. Por que o @WebMvcTeste passou sem banco, mas o @SpringBootTest não?

- O @WebMvcTeste não sobe o banco, pois ele é só um teste de controller.
- O @SpringBootTest sobe a aplicação inteira, pois ele é um teste de integração.

2. Qual a diferença entre class file version 65 e 61?

- 61 = Java 17
- 65 = Java 21

ddl-auto: troque para validate. Com update, o Hibernate cria e altera tabelas por conta próproa, e isso briga com o Flyway. A regra é: o Flyway manda no schema, o Hibernate só confere.


O que muda entre git config --local, --global e --system? Qual escolher quando se usa duas contas no mesmo computador?

--system: afeta todos os usuários do computador
--global: afeta apenas o usuário atual
--local: afeta apenas o repositório atual

- O --local é o mais recomendado, pois ele é o mais seguro e o que menos causa problemas.


1. O que é um arquivo .gitignore? Por que ele é importante?

- O .gitignore é um arquivo que diz para o Git quais arquivos e diretórios devem ser ignorados pelo sistema.

- Ele é importante para que o Git não rastreie arquivos desnecessários, como arquivos de configuração, arquivos de cache, arquivos de log, etc.


Um ALTER ... TYPE de timestamp para timestamptz converte os dados que já existem. Em que fuso o Postgres vai assumir que os valores antigos estavam? (Dica: pesquise timezone do servidor.) Por que isso importa em produção, com uma tabela cheia de dados?

- O timezone do servidor é UTC. Se a tabela estiver cheia de dados, o ALTER ... TYPE vai converter os dados para TIMESTAMP WITH TIME ZONE, assumindo que os valores antigos estavam em UTC. 

Pesquise o que o @Transactional(readOnly = true) faz num método só de leitura. Tente responder no learning-log.

- O @Transactional(readOnly = true) faz com que o método seja executado em uma transação somente leitura, o que significa que o Hibernate não vai fazer o cache dos dados


perguntas: 

1. Por que 201 e não 200? Para que serve o header Location?
    - O status 201 Created indica que um novo recurso foi criado com sucesso. 
    - O header Location indica a URL do recurso criado.

2. Por que validar no backend se o front já valida?
    - O front-end pode ser contornado e o back é a ultima barreira de proteção

3. O que acontece se o JSON enviado for malformado (uma virgula faltando?) qual exceção o Spring lança? (Teste com curl e leia a resposta. Fica como desafio: tratar essa também. Dica, crie uma classe "CustomExceptionHandler" e anote com @RestControllerAdvice, depois anote o método com @ExceptionHandler e retorne um ProblemDetail)

    - O Spring lança uma exceção do tipo MethodArgumentNotValidException

4. Um Instant.now() direto no service dificulta o teste. Como você resolveria? (Dica: o Clock que vimos no Dia 1.)
  - Injetar uma instância de Clock e usar o método now() diretamente. Desse jeito conseguimos mockar o Clock e testar o método create() com um horário fixo.
  