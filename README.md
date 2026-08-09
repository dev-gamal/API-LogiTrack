# LogiTrack API

LogiTrack API est une application backend développée en **Spring Boot** (Java) destinée à la gestion logistique, incluant le suivi des commandes, la gestion des produits et des clients. 
L'application intègre un système d'authentification sécurisé basé sur JWT et documente ses endpoints de manière interactive via Swagger.

## 🚀 Fonctionnalités Principales

* **Sécurité & Authentification :** Inscription, connexion et sécurisation des routes via JWT (JSON Web Tokens). Gestion fine des utilisateurs et des rôles.
* **Gestion des Clients :** Création, lecture, mise à jour et suppression (CRUD) des profils clients.
* **Gestion des Produits :** Maintien du catalogue des produits disponibles.
* **Gestion des Commandes :** Création et suivi des commandes (`Order`), gestion des lignes de commande détaillées (`OrderLine`), et mise à jour du statut des commandes (`OrderStatus`).

## 🛠️ Stack Technique

* **Langage :** Java
* **Framework :** Spring Boot
* **Sécurité :** Spring Security & JWT
* **Base de données :** JPA / Hibernate
* **Outil de build :** Maven
* **Documentation API :** Swagger / OpenAPI
* **Architecture :** Architecture en couches (Controllers, Services, Repositories, DTOs, Mappers)

## 📂 Architecture du Projet

Le projet respecte une architecture classique et modulaire Spring Boot :

```text
src/main/java/com/logitrack/
├── config/        # Configuration globale (ex: SwaggerConfig)
├── controllers/   # Contrôleurs REST (Auth, Client, Order, Product, User)
├── dto/           # Objets de Transfert de Données (Request / Response)
├── entities/      # Modèles de données JPA (Client, Order, Product, Role, User...)
├── exception/     # Gestion centralisée des exceptions (GlobalExceptionHandler)
├── mapper/        # Mappage entre les Entités et les DTOs
├── repositories/  # Interfaces Spring Data JPA pour l'accès aux données
├── security/      # Configuration de sécurité (JwtAuthenticationFilter, JwtUtils, SecurityConfig)
└── services/      # Logique métier de l'application
```

## ⚙️ Prérequis

* Java Development Kit (JDK)
* Maven (ou utilisez le wrapper `./mvnw` inclus dans le projet)
* Serveur MySQL 8.0

## 🏃‍♂️ Installation et Exécution

1. **Cloner le dépôt :**
   ```bash
   git clone <votre-repo-url>
   cd API-LogiTrack
   ```

2. **Configurer la base de données :**
   Ouvrez le fichier `src/main/resources/application.properties` et configurez vos accès au serveur MySQL 8.0 (URL de connexion, nom d'utilisateur, mot de passe).

3. **Compiler et Lancer l'application :**
   *Via Maven Wrapper inclus :*
   
   Sous Linux/Mac :
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Sous Windows :
   ```cmd
   mvnw.cmd spring-boot:run
   ```

## 📖 Documentation de l'API (Swagger)

Une fois l'application démarrée, l'interface Swagger UI pour explorer et tester les APIs est accessible via votre navigateur. L'URL typique par défaut est :
`http://localhost:8080/swagger-ui.html` ou `http://localhost:8080/swagger-ui/index.html` (selon votre configuration locale).

## 📊 Modélisation et Conception

Le dépôt inclut également des ressources visuelles à la racine du projet pour faciliter la compréhension du domaine :
* `LogiTrack classes.jpg` : Diagramme de classes UML.
* `LogiTrack use case.jpg` : Diagramme des cas d'utilisation.
