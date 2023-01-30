# Springboot Introduction

## O que é?
Introdução ao springboot.

## Tecnologias / Frameworks
- Java 11
- Maven 3.8
- Springboot 2.7.0
- Swagger
- Hibernate
- H2

## Anotações
- Toda classe controller, deve ter um @Controller, porém, será necessário acrescentar um @ResponseBody para cada @RequestMapping, caso contrário, o springboot entenderá que o retorno dos métodos da classe será uma interface web e não um corpo json por exemplo. 
 
- Para evitar esse problema, pode-se substituir o @Controller por @RestController, dessa forma, não é necessário utilizar o @ResponseBody, pois o mesmo ficará implicito no código.


- Não é uma boa prática retornar entidades JPA nos métodos dos controllers, sendo mais indicado retornar classes que seguem o padrão DTO (Data Transfer Object);

- Para não precisar reiniciar manualmente o servidor a cada alteração feita no código, basta utilizar o módulo Spring Boot DevTools;

- Para transformar as classes em entidades da JPA, deve-se utilizar a anotação @Entity.
- Para identificar a chave primaria usar a anotação @Id e para autoincrementar usar @GeneratedValue(strategy = GenerationType.IDENTITY)

- Para que o Spring Boot popule automaticamente o banco de dados da aplicação, devemos criar o arquivo src/main/resources/data.sql;

- O arquivo application.properties contém as configurações de datasources, JPA, e outras configurações do springboot.

- No application.properties a propriedade 'spring.jpa.defer-datasource-initialization=true' serve para que o data.sql seja executada após a criação das tabelas do banco.
  
- Para criar consultas que filtram por atributos da entidade, devemos seguir o padrão de nomenclatura de métodos do Spring, como por exemplo findByCursoNome;

- Para criar manualmente a consulta com JPQL, devemos utilizar a anotação @Query;

- @RequestBody serve para explicitar que o parametro de um método vai ser passado através do corpo da requisição.
  
- Para receber dados enviados no corpo da requisição, a boa prática é criar uma classe que também siga o padrão DTO (Data Transfer Object);

- Para montar uma resposta a ser devolvida ao cliente da API, devemos utilizar a classe ResponseEntity do Spring;
  
- A partir da versão 2.3.0 do Spring Boot o Bean Validation (@NotNull @NotEmpty @Size) não vem mais incluído automaticamente no projeto, sendo necessário adicionar essa dependência no arquivo pom.xml:
```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

```

- Para o Spring disparar as validações do Bean Validation e devolver um erro 400, caso alguma informação enviada pelo cliente esteja inválida, devemos utilizar a anotação @Valid;
  
- Para interceptar as exceptions que forem lançadas nos métodos das classes controller, devemos criar uma classe anotada com @RestControllerAdvice;
  
- Para tratar os erros de validação do Bean Validation e personalizar o JSON, que será devolvido ao cliente da API, com as mensagens de erro, devemos criar um método na classe @RestControllerAdvice e anotá-lo com @ExceptionHandler e @ResponseStatus.

- Nas últimas versões do Spring Boot os métodos getOne() e getById(), das interfaces repository, foram marcados como deprecated, sendo que agora devemos utilizar o novo método getReferenceById().
  
  >Lista de deprecated: https://docs.spring.io/spring-data/jpa/docs/current/api/deprecated-list.html

- O springboot 2.7.0 utiliza um conversor (jackson) que binda objetos para json, porém, ao utiliza-lo com lombok, é necessário sentar a anotação @JsonAutoDetect(fieldVisibility = Visibility.ANY) para que o jackson enxergue os métodos (getters e setters).
  
- Para receber parâmetros dinâmicos no path da URL, devemos utilizar a anotação @PathVariable;
  
- Para fazer o controle transacional automático, devemos utilizar a anotação @Transactional nos métodos do controller, ou seja, dessa forma é feito commit no banco após a execução sem erros/exceptions e a conexão com o banco é fechada.
- Para criar paginação e ordenação de modo configurável deve-se utilizar a classe Pageable como no exemplo abaixo.
```java
Pageable paginacao = PageRequest.of(pagina, qtd, Direction.ASC, ordenacao);
```
- É possivel utilizar a paginação e ordenação de forma automática, sem a necessidade de utilizar a configuração do Pageable. Para isso, é necessário adicionar a notação @EnableSpringDataWebSupport na classe principal da aplicação, a mesma que contém a anotação @SpringBootApplication. Para consumir o endpoint com a paginação e ordenação deve utilizar o modelo abaixo:
```json
{{HOST}}/{{URL_API}}?page=1&size=2&sort=titulo,desc&&sort=id,asc
```
- É possível configurar o parametro default da ordenação e paginação nos parâmetros do método em questão.
```java
@PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 2)
```

- É possível utilizar provedor de cache em memória para testes da aplicação ou setar um provedor para produção. Para isso, é necessário baixar a dependência e adicionar a anotação @EnableCaching na classe principal da aplicação. E adicionar a anotação @Cacheable(value = "findAll") no método onde queira guardar o retorno em cache.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

- Para reiniciar o cache caso haja inserção ou update no banco, basta utilizar a anotação @CacheEvict(value = {"findAll", "detailsById"}, allEntries = true) nos métodos que devem disparar o reset do cache e passar um value com os apelidos dos caches.
- Utilizar cache apenas em entidades que são pouco alteradas.
```
Link para soluções do WebSecurityConfigurerAdapter deprecated

https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
```
- Para configurar a autenticação stateless no Spring Security, devemos utilizar o método sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

- Para disparar manualmente o processo de autenticação no Spring Security, devemos utilizar a classe AuthenticationManager;

- Para poder injetar o AuthenticationManager no controller, devemos criar um método anotado com @Bean, na classe SecurityConfigurations, que retorna uma chamada ao método super.authenticationManager();

- Para criar o token JWT, devemos utilizar a classe Jwts;
O token tem um período de expiração, que pode ser definida no arquivo application.properties;

- Para injetar uma propriedade do arquivo application.properties, devemos utilizar a anotação @Value.

- Para enviar o token JWT na requisição, é necessário adicionar o cabeçalho Authorization, passando como valor Bearer token;
  
- Para criar um filtro no Spring, devemos criar uma classe que herda da classe OncePerRequestFilter;
  
- Para recuperar o token JWT da requisição no filter, devemos chamar o método request.getHeader("Authorization");
  
- Para habilitar o filtro no Spring Security, devemos chamar o método and().addFilterBefore(new AutenticacaoViaTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  
- Para indicar ao Spring Security que o cliente está autenticado, devemos utilizar a classe SecurityContextHolder, chamando o método SecurityContextHolder.getContext().setAuthentication(authentication).

- Na classe TokenService foi necessário colocar a anotação @Service. Na classe SecurityConfigurations foi necessário adicionar o método abaixo:
```java
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
```
## Dúvidas
- DTO (Data Transfer Object) e o VO (Value Object) são a mesma coisa?
- Diferença entre métodos Put e Patch? Put atualiza e sobreescreve tudo, já o Patch atualiza apenas uma parte?