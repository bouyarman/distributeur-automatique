# distributeur-automatique

Ce projet est une API backend pour un distributeur automatique, développée en Java avec Spring Boot et PostgreSQL. Elle gère les produits, le panier, et le solde d’un utilisateur. Le frontend React consomme cette API.

# fonctionnalités principales

Gestion du solde (insertion, extraction, réinitialisation) le solde reste fixe meme si on refresh la page il faut clicked rsur reset balance

Gestion des produits (liste, ajout au panier) aussi les produis de panier reste fixe meme si on restart la page parceque les produits sont stocker en db il faut cliocker sur empty panier pour le vider

Gestion du panier (ajout, suppression, modification de la quantité)

Calcul de la faisabilité d’achat selon le solde disponible

Calcul du rendu de monnaie (décomposition du solde en pièces à rendre)

Support CORS pour le frontend React (http://localhost:5173)

# Technologies utilisées

Java 21 / Spring Boot

PostgreSQL (pgAdmin 4 pour la visualisation des données)

React (frontend, projet séparé)

Maven

Axios (côté client HTTP)

| Méthode                  | URL                          | Description                                              |
| ------------------------ | ---------------------------- | -------------------------------------------------------- |
| **BALANCE CONTROLLER**   |                              |
| POST                     | `/api/insert`                | Insère une pièce dans le solde (body : montant)          |
| POST                     | `/api/extract`               | Retire une somme du solde (body : montant)               |
| GET                      | `/api/reset-balance`         | Réinitialise le solde à 0                                |
| GET                      | `/api/balance`               | Récupère le solde actuel                                 |
| GET                      | `/api/return-exchange`       | Retourne la monnaie rendue                               |
| POST                     | `/api/add-product-price`     | Ajoute un prix produit (body : productPrice + productId) |
| **CART ITEM CONTROLLER** |                              |
| POST                     | `/api/remove-all-cart-items` | Vide le panier                                           |
| POST                     | `/api/subtract-quantity`     | Diminue la quantité d’un produit (body : productId)      |
| GET                      | `/api/list-added-to-cart`    | Liste les produits ajoutés au panier                     |
| **PRODUCT CONTROLLER**   |                              |
| GET                      | `/api/products`              | Récupère la liste de tous les produits                   |
| POST                     | `/api/add-to-cart`           | Ajoute un produit au panier (body : productId)           |

# exemple d’utilisation

**Note :** Je n’ai pas utilisé Postman ni curl pour tester l’API.  
J’ai principalement utilisé l’interface React développée pour visualiser et interagir avec l’application.

-Ajouter un produit au panier (id=1)
curl -X POST -H "Content-Type: application/json" -d "1" http://localhost:8080/api/add-to-cart

-Récupérer la liste des produits dans le panier
curl http://localhost:8080/api/list-added-to-cart

-Insérer une pièce de 2 unités dans le solde
curl -X POST -H "Content-Type: application/json" -d "2" http://localhost:8080/api/insert

-Réinitialiser le solde
curl http://localhost:8080/api/reset-balance

# installation & lancement

------- BACKEND
-Cloner le dépôt
git clone https://github.com/ton-utilisateur/distributeur-automatique.git
cd distributeur-automatique
cd BACKEND

-Configurer la base de données PostgreSQL (modifier application.properties)
spring.datasource.url=jdbc:postgresql://localhost:5432/distibuteurAutomatique
spring.datasource.username=your_username
spring.datasource.password=your_password

-Construire et lancer l’application
mvn clean install
mvn spring-boot:run

**Important :** L’API sera accessible sur http://localhost:8080.

------- FRONTEND
cd ../FRONTEND

-Installer les dépendances React
npm install

-Lancer l’application React
npm run dev

**Important :** L’interface React sera accessible par défaut sur http://localhost:5173

# Notes importantes

Le frontend React est une application séparée, elle communique via HTTP avec cette API.

Le CORS est configuré pour autoriser http://localhost:5173 (port par défaut de Vite.js/React).

Les timestamps createdAt sont gérés via Hibernate (annotation @CreationTimestamp).

Les DTOs sont utilisés pour transférer les données entre frontend et backend.

Les pièces valides pour le solde sont 0.25, 0.5, 1, 2, 5, 10.

# Contribution

Les contributions sont les bienvenues, n’hésitez pas à ouvrir une issue ou proposer une pull request.
Si vous rencontrez des problèmes ou avez des questions, n’hésitez pas à me contacter à l’adresse suivante : bouyarmanhamza@gmail.com

# Licence

Licence MIT © Bouyarman Hamza
