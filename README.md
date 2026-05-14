--------------------------------------------------------------------------------
# Projet CPO : Simulateur CPU et Assembleur en Java

Projet réalisé dans le cadre du cours de Conception et Programmation Objet (L3 IRT / STRI1A) encadré par M. Patrice Torguet à l'Université de Toulouse.

## Description
Ce projet consiste à développer un simulateur de processeur accompagné de son assembleur. L'application est capable de lire un programme textuel (instructions assembleur), de le traduire en codes numériques (Opcodes), de le charger dans une mémoire RAM simulée de 64 Ko, puis de l'exécuter via un processeur virtuel gérant 16 registres et une ALU.

## État d'avancement (Dépôt Partiel 2)
Le noyau fonctionnel du simulateur est terminé à 100%. Les étapes 1 à 5 du cahier des charges sont entièrement implémentées et testées :
- **Étape 1** : Gestion de la mémoire, des registres et instructions de transfert (`load`, `store`, `break`).
- **Étape 2** : Assembleur (découpage du texte et traduction en octets avec gestion des exceptions `try/catch`).
- **Étape 3** : Unité Arithmétique et Logique (ALU) pour les calculs (dont la division avec récupération du reste).
- **Étape 4** : Structures de contrôle et branchements mémoire (`jump`, `beq`, `bne`).
- **Étape 5** : Utilisation de registres d'index pour les tableaux et directives `data` / `string` (encodage UTF-8).

## Outils et Architecture
- **Langage** : Java
- **Gestionnaire de projet** : Maven
- **Framework de test** : JUnit 4
- **Versionnement** : Git (Dépôt : github.com/Weishen-Rif/SimulateurCPU/)

Le code source est organisé de manière modulaire (séparation de la logique dans les paquetages `fr.stri.projet.cpu`, `fr.stri.projet.memoire`, `fr.stri.projet.assembleur`).

## Compilation et Exécution
Le projet utilise l'architecture standard Maven (`src/main` pour les sources, `src/test` pour les tests).

**Pour compiler le projet :**
```bash
mvn clean compile
```

**Pour exécuter le programme :**

Vous pouvez lancer le projet directement depuis votre IDE (IntelliJ IDEA, NetBeans, Eclipse) en exécutant la méthode main située dans la classe Main.java (src/main/java/fr/stri/projet/Main.java).

## Tests Unitaires

La validation du code est assurée par des tests unitaires automatisés sous JUnit, reposant sur l'utilisation de fixtures (@Before) et de la métaphore de l'oracle (assertEquals).

Nous avons isolé les tests pour ProcesseurTest, AssembleurTest et ALUTest.

**Pour lancer la suite de tests :**

```bash
mvn test
```

## Perspectives (Pour le Dépôt Final du 24 mai)

Maintenant que le simulateur de base est fonctionnel, nous prévoyons d'implémenter Plusieurs extensions :

- Création d'une Interface Homme-Machine (IHM).
- Persistance des données de la mémoire (Base de données H2 ou Firebase).
- Interface de contrôle via un Bot Discord.

Auteurs :
Ayoub BOUDOUHI (STRI1A),
Ayyub BOUTAHIR (L3 IRT) et
Robin RIGAL (L3 IRT)

***
