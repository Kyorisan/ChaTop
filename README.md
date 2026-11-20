# ChaTop - Portail de location

Application full-stack de mise en relation entre locataires et propriétaires :

- **Front-end** : Angular 20 (à la racine du dépôt, fourni et non modifié)
- **Back-end** : API REST Spring Boot 4.1 / Java 25 (dossier `back/`)
- **Base de données** : MySQL 8
- **Sécurité** : Spring Security avec authentification par token JWT
- **Documentation** : OpenAPI 3 / Swagger UI (springdoc-openapi)

## Structure du dépôt

```
ChaTop/
├── back/ API Spring Boot
│   ├── src/main/java/com/chatop/api/
│   │   ├── config/  Swagger, ressources statiques, propriétés applicatives
│   │   ├── controller/  Points d'entrée HTTP
│   │   ├── dto/  Objets de requête et de réponse
│   │   ├── entity/  Entités JPA (User, Rental, Message)
│   │   ├── exception/  Exceptions métier et gestion centralisée des erreurs
│   │   ├── repository/  Interfaces Spring Data JPA
│   │   ├── security/  Configuration Spring Security et JWT
│   │   └── service/  Logique métier
│   └── src/main/resources/application.properties
├── ressources/
│   ├── mockoon/rental-oc.json  Environnement Mockoon (maquette de l'API)
│   └── sql/script.sql  Schéma de la base de données)
└── src/  Application Angular
```

## Prérequis

- Java 25 (JDK)
- MySQL 8
- Node.js 24 et npm
- Git

Maven n'est pas nécessaire : le projet embarque le wrapper ``mvnw`.

## 1. Cloner le projet

```bash
git clone https://github.com/Kyorisan/ChaTop.git
cd ChaTop
```

## 2. Configurer la base de données

Connectez-vous à MySQL en tant qu'administrateur (`mysql -u root -p`), puis créez la base et un utilisateur dédié :

```sql
CREATE DATABASE chatop_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'chatop_user'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON chatop_db.* TO 'chatop_user'@'localhost';
FLUSH PRIVILEGES;
```

Créez ensuite les tables à partir du script fourni :

```sql
USE chatop_db;
SOURCE chemin/vers/ChaTop/ressources/sql/script.sql;
```

Le script crée les tables `USERS`, `RENTALS` et `MESSAGES` ainsi que leurs clés étrangères.

## 3. Configurer les variables d'environnement

Aucun identifiant n'est écrit dans le code : `application.properties` lit les valeurs suivantes dans les variables d'environnement.

| Variable      | Description                                           | Exemple                                  |
|---------------|-------------------------------------------------------|------------------------------------------|
| `DB_URL`      | URL JDBC de la base                                   | `jdbc:mysql://localhost:3306/chatop_db`  |
| `DB_USERNAME` | Utilisateur MySQL                                     | `chatop_user`                            |
| `DB_PASSWORD` | Mot de passe MySQL                                    | `votre_mot_de_passe`                     |
| `JWT_SECRET`  | Clé de signature des tokens JWT (32 caractères min.)  | chaîne aléatoire                         |

**Windows (PowerShell)** :

```powershell
[Environment]::SetEnvironmentVariable("DB_URL", "jdbc:mysql://localhost:3306/chatop_db", "User")
[Environment]::SetEnvironmentVariable("DB_USERNAME", "chatop_user", "User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "votre_mot_de_passe", "User")

$bytes = New-Object byte[] 48
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Environment]::SetEnvironmentVariable("JWT_SECRET", [Convert]::ToBase64String($bytes), "User")
```

Fermez puis rouvrez le terminal (et l'IDE) pour que les variables soient prises en compte.

**Linux / macOS** :

```bash
export DB_URL=jdbc:mysql://localhost:3306/chatop_db
export DB_USERNAME=chatop_user
export DB_PASSWORD=votre_mot_de_passe
export JWT_SECRET=$(openssl rand -base64 48)
```

## 4. Lancer le back-end

```bash
cd back
./mvnw spring-boot:run # Windows : .\mvnw.cmd spring-boot:run
```

L'API démarre sur **http://localhost:3001**. Les images envoyées lors de la création d'une location sont stockées dans `back/uploads/` (dossier ignoré par Git) et servies sur `http://localhost:3001/uploads/...`.

## 5. Lancer le front-end

Dans un second terminal, à la racine du dépôt :

```bash
npm install --no-package-lock
npm run start
```

L'application est disponible sur **http://localhost:4200**. Le proxy Angular (`src/proxy.config.json`) redirige les appels `/api/**` vers le back-end sur le port 3001.

> `--no-package-lock` : le `package-lock.json` fourni référence un registre npm privé inaccessible. Cette option installe les dépendances depuis le registre public sans modifier le fichier.

## Documentation de l'API (Swagger)

Une fois le back-end lancé :

- **Swagger UI** : http://localhost:3001/swagger-ui.html
- **Spécification OpenAPI (JSON)** : http://localhost:3001/v3/api-docs

Pour tester les routes protégées depuis Swagger UI :

1. Appeler `POST /api/auth/register` ou `POST /api/auth/login` et copier le `token` renvoyé.
2. Cliquer sur **Authorize** et coller le token (sans le préfixe `Bearer`).
3. Les appels suivants envoient automatiquement le header `Authorization: Bearer <token>`.

## Routes de l'API

Toutes les routes sont préfixées par `/api` et protégées par JWT, sauf `register`, `login` et la documentation Swagger.

| Méthode | Route                | Description                                    | Succès |
|---------|----------------------|------------------------------------------------|--------|
| POST    | `/api/auth/register` | Créer un compte et obtenir un token            | 200    |
| POST    | `/api/auth/login`    | Se connecter et obtenir un token               | 200    |
| GET     | `/api/auth/me`       | Utilisateur connecté                           | 200    |
| GET     | `/api/rentals`       | Lister les locations                           | 200    |
| GET     | `/api/rentals/{id}`  | Détail d'une location                          | 200    |
| POST    | `/api/rentals`       | Créer une location (multipart, avec image)     | 201    |
| PUT     | `/api/rentals/{id}`  | Modifier une location (propriétaire seulement) | 200    |
| GET     | `/api/user/{id}`     | Détail d'un utilisateur                        | 200    |
| POST    | `/api/messages`      | Envoyer un message au propriétaire             | 201    |

Codes d'erreur : `400` (données invalides), `401` (token absent ou invalide, identifiants incorrects), `403` (action réservée au propriétaire), `404` (ressource introuvable), `500` (erreur serveur). Les erreurs renvoient un corps JSON `{"message": "..."}`.

## Mockoon (optionnel)

L'environnement `ressources/mockoon/rental-oc.json` simule l'API et a servi de référence pour le contrat. Pour l'utiliser à la place du back-end : installer [Mockoon](https://mockoon.com/download/), ouvrir l'environnement (**File > Open environment**) et lancer le serveur. Mockoon et le back-end utilisent tous deux le port 3001 : un seul des deux doit tourner à la fois.
