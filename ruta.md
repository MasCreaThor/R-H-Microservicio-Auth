auth-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hotel/
│   │   │           └── auth_service/
│   │   │               ├── config/                    # Configuraciones
│   │   │               │   ├── MongoConfig.java       # Configuración de MongoDB
│   │   │               │   └── DBInitializer.java     # Inicializador de datos
│   │   │               │
│   │   │               ├── controllers/               # Controladores REST
│   │   │               │   ├── AuthController.java    # Controlador de autenticación
│   │   │               │   ├── UserController.java    # Controlador de usuarios
│   │   │               │   └── PeopleController.java  # Controlador de datos personales
│   │   │               │
│   │   │               ├── models/                    # Modelos de datos
│   │   │               │   ├── dto/                   # Data Transfer Objects
│   │   │               │   │   ├── JwtResponse.java   # (Dentro de auth-dtos.java)
│   │   │               │   │   ├── LoginRequest.java  # (Dentro de auth-dtos.java)
│   │   │               │   │   ├── RegisterRequest.java # (Dentro de auth-dtos.java)
│   │   │               │   │   ├── PeopleRequest.java # (Dentro de auth-dtos.java)
│   │   │               │   │   ├── TokenRefreshRequest.java # (Dentro de auth-dtos.java)
│   │   │               │   │   ├── TokenRefreshResponse.java # (Dentro de auth-dtos.java)
│   │   │               │   │   └── MessageResponse.java
│   │   │               │   │
│   │   │               │   └── entity/                # Entidades de MongoDB
│   │   │               │       ├── User.java          # (Ya existe)
│   │   │               │       ├── Role.java          # (Ya existe)
│   │   │               │       ├── People.java        # (Ya existe)
│   │   │               │       └── RefreshToken.java  # (Ya existe)
│   │   │               │
│   │   │               ├── repositories/              # Repositorios de MongoDB
│   │   │               │   ├── UserRepository.java    # (Ya existe)
│   │   │               │   ├── RoleRepository.java    # (Ya existe)
│   │   │               │   ├── PeopleRepository.java  # (Ya existe)
│   │   │               │   └── RefreshTokenRepository.java # Nuevo repositorio
│   │   │               │
│   │   │               ├── security/                  # Configuración de seguridad
│   │   │               │   ├── WebSecurityConfig.java # Configuración de seguridad
│   │   │               │   │
│   │   │               │   ├── jwt/                   # Clases relacionadas con JWT
│   │   │               │   │   ├── JwtUtils.java      # Utilidades para JWT
│   │   │               │   │   ├── AuthTokenFilter.java # Filtro JWT
│   │   │               │   │   ├── AuthEntryPointJwt.java # Punto de entrada para errores
│   │   │               │   │   └── UserDetailsImpl.java # Implementación UserDetails
│   │   │               │   │
│   │   │               │   └── services/              # Servicios de seguridad
│   │   │               │       └── UserDetailsServiceImpl.java # Servicio UserDetails
│   │   │               │
│   │   │               └── service/                   # Servicios de la aplicación
│   │   │                   └── RefreshTokenService.java # Servicio de refresh token
│   │   │
│   │   └── resources/
│   │       └── application.properties    # (Ya existe) Propiedades de la aplicación
│   │
│   └── test/
└── pom.xml  # (Ya existe) Archivo de configuración Maven