# 🔧 Arquitetura do Projeto

Este documento detalha a arquitetura técnica do projeto, incluindo padrões de design, estrutura de código e decisões técnicas.

## 📋 Índice

- [Visão Geral](#visao-geral)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Padrões de Design](#padroes-de-design)
- [Segurança](#seguranca)
- [Performance](#performance)
- [Monitoramento](#monitoramento)

## 🎯 Visão Geral

O projeto segue uma arquitetura em camadas com separação clara de responsabilidades:

```
Frontend (React/TS) ←→ API Gateway ←→ Microserviços (Spring Boot)
```

### Princípios Arquiteturais

1. **Separation of Concerns (SoC)**
   - UI separada da lógica de negócios
   - Dados separados da lógica de apresentação

2. **SOLID Principles**
   - Single Responsibility
   - Open/Closed
   - Liskov Substitution
   - Interface Segregation
   - Dependency Inversion

3. **Clean Architecture**
   - Independência de frameworks
   - Testabilidade
   - Independência de UI
   - Independência de banco de dados

## 🛠️ Tecnologias

### Frontend
- React 18+
- TypeScript 4.x
- Redux Toolkit
- Material-UI
- React Query

### Backend
- Spring Boot 3.x
- Java 17
- Spring Security
- Spring Data JPA
- PostgreSQL

### DevOps
- Docker
- Kubernetes
- Jenkins
- SonarQube
- ELK Stack

## 📁 Estrutura do Projeto

### Frontend

```
src/
├── assets/         # Imagens, fontes, etc.
├── components/     # Componentes reutilizáveis
├── hooks/         # Custom React hooks
├── pages/         # Componentes de página
├── services/      # Serviços de API
├── store/         # Redux store
├── types/         # TypeScript types
└── utils/         # Funções utilitárias
```

### Backend

```
src/main/java/
├── config/        # Configurações
├── controllers/   # REST Controllers
├── models/        # Entidades
├── repositories/  # Data Access
├── services/      # Lógica de negócios
└── utils/         # Classes utilitárias
```

## 🎨 Padrões de Design

### Frontend

1. **Componentes Funcionais**
   ```typescript
   const UserProfile: React.FC<UserProps> = ({ user }) => {
     return (
       <div>
         <h1>{user.name}</h1>
         <p>{user.email}</p>
       </div>
     );
   };
   ```

2. **Custom Hooks**
   ```typescript
   const useAuth = () => {
     const [user, setUser] = useState<User | null>(null);
     
     const login = async (credentials: Credentials) => {
       // Implementação
     };
     
     return { user, login };
   };
   ```

### Backend

1. **Repository Pattern**
   ```java
   @Repository
   public interface UserRepository extends JpaRepository<User, Long> {
       Optional<User> findByEmail(String email);
   }
   ```

2. **Service Layer**
   ```java
   @Service
   @Transactional
   public class UserService {
       private final UserRepository repository;
       
       public UserService(UserRepository repository) {
           this.repository = repository;
       }
       
       public User createUser(UserDTO dto) {
           // Implementação
       }
   }
   ```

## 🔒 Segurança

### Autenticação

1. **JWT Token**
   ```java
   @Component
   public class JwtTokenProvider {
       public String createToken(Authentication authentication) {
           // Implementação
       }
   }
   ```

2. **Spring Security Config**
   ```java
   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {
       @Bean
       public SecurityFilterChain filterChain(HttpSecurity http) {
           // Configuração
       }
   }
   ```

### Autorização

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public ResponseEntity<?> adminOnly() {
    // Endpoint restrito
}
```

## 🚀 Performance

### Frontend

1. **Code Splitting**
   ```typescript
   const Dashboard = lazy(() => import('./pages/Dashboard'));
   ```

2. **Memoização**
   ```typescript
   const MemoizedComponent = memo(({ data }) => {
     return <div>{data}</div>;
   });
   ```

### Backend

1. **Caching**
   ```java
   @Cacheable(value = "users", key = "#id")
   public User getUser(Long id) {
       return repository.findById(id).orElseThrow();
   }
   ```

2. **Paginação**
   ```java
   @GetMapping("/users")
   public Page<User> getUsers(Pageable pageable) {
       return repository.findAll(pageable);
   }
   ```

## 📊 Monitoramento

### Métricas

1. **Actuator Endpoints**
   ```java
   @Configuration
   public class ActuatorConfig {
       @Bean
       public MeterRegistry meterRegistry() {
           return new SimpleMeterRegistry();
       }
   }
   ```

2. **Custom Metrics**
   ```java
   @Component
   public class MetricsService {
       private final MeterRegistry registry;
       
       public void recordOperation(String name) {
           registry.counter("app.operation", "name", name).increment();
       }
   }
   ```

### Logging

```java
@Slf4j
@RestController
public class UserController {
    public ResponseEntity<?> createUser() {
        log.info("Creating new user");
        // Implementação
    }
}
```

## 🔄 CI/CD Pipeline

```yaml
pipeline:
  stages:
    - build
    - test
    - sonar
    - deploy

  steps:
    build:
      - npm install
      - npm run build
    
    test:
      - npm run test
      - mvn test
    
    sonar:
      - sonar-scanner
    
    deploy:
      - docker build
      - docker push
      - kubectl apply
```

## 📈 Escalabilidade

### Horizontal Scaling

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: app
        image: app:latest
```

### Load Balancing

```yaml
apiVersion: v1
kind: Service
metadata:
  name: app-service
spec:
  type: LoadBalancer
  ports:
    - port: 80
  selector:
    app: myapp
```