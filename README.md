# 🗂️ FileSync – Outil Java de synchronisation de fichiers

## 📌 Présentation

**FileSync** est un outil léger de synchronisation de fichiers développé en Java. Inspiré d’outils tels que `rsync` ou `Unison`, il permet d’unifier le contenu de deux répertoires en comparant **les dates de dernière modification** des fichiers. Ce projet s’inscrit dans un cadre académique, avec une forte attention portée à l’**architecture logicielle**, la **modularité**, et l’usage raisonné des **patrons de conception**.

L’outil se compose de **trois applications** en ligne de commande :

- `NewProfileApp` : crée un profil de synchronisation entre deux répertoires.
- `SyncApp` : effectue la synchronisation bidirectionnelle.
- `SyncStatApp` : affiche les informations d’un profil et de son registre.

---

## ⚙️ Fonctionnalités

- 🔧 Création de profil de synchronisation (`new-profile`)
- 🔁 Synchronisation bidirectionnelle avec détection des suppressions et modifications
- ⚠️ Gestion des conflits avec interaction utilisateur
- 🧾 Fichier de registre lisible (format XML)
- 🧱 Architecture modulaire, extensible et testable

---

## 📁 Structure du projet

```text
src/
├── app/                 # Points d’entrée : NewProfileApp, SyncApp, SyncStatApp
├── builder/             # Construction des profils avec le patron Builder
├── factory/             # Fabrication des stratégies de persistance (Factory Method)
├── filesystem/          # Abstraction du système de fichiers (FileHandler)
├── manager/             # Gestionnaires globaux de profils et registres (Singleton)
├── model/               # Modèle métier : Profile, Registry, FileComponent
├── persistence/         # Interface Strategy + implémentation XML
├── stat/                # Façade d’affichage des informations de profil
├── sync/                # Chaîne de synchronisation (Chain of Responsibility)
├── transformer/         # Conversion XML ↔ objets Java
├── visitor/             # Visiteurs de l’arborescence de fichiers
└── resources/
    └── dtd/             # Fichiers DTD de validation XML
```

---

## 🚀 Exécution

### 🧰 Prérequis

- Java 17+
- Maven 3.9+

### 🛠️ Compilation
```console
mvn clean package
```

### 📂 Création d’un nouveau profil
```console
java -cp target/file-sync-1.0.jar app.NewProfileApp monProfil /chemin/vers/A /chemin/vers/B
```
Un fichier `monProfil.sync` est généré, contenant les chemins A et B.

### 🔁 Synchronisation des dossiers
```console
java -cp target/file-sync-1.0.jar app.SyncApp monProfil
```
En cas de conflit, l’utilisateur devra choisir la version à conserver (A ou B).

### 📊 Affichage de l’état
```console
java -cp target/file-sync-1.0.jar app.SyncStatApp monProfil
```
Affiche les chemins du profil et les fichiers enregistrés dans le registre.

## 📚 Format XML & Validation
`profile.sync` (profil)
```xml
<!DOCTYPE profile SYSTEM "profile.dtd">
<profile>
  <name>monProfil</name>
  <pathA>/chemin/vers/A</pathA>
  <pathB>/chemin/vers/B</pathB>
</profile>
```

`registry.sync` (registre)
```xml
<!DOCTYPE registry SYSTEM "registry.dtd">
<registry profile="monProfil">
  <entry>
    <path>fichier.txt</path>
    <timestamp>2025-04-23T17:00:00</timestamp>
  </entry>
</registry>
```

DTDs associées
```xml
<!-- profile.dtd -->
<!ELEMENT profile (name, pathA, pathB)>
<!ELEMENT name (#PCDATA)>
<!ELEMENT pathA (#PCDATA)>
<!ELEMENT pathB (#PCDATA)>

<!-- registry.dtd -->
<!ELEMENT registry (entry*)>
<!ATTLIST registry profile CDATA #REQUIRED>
<!ELEMENT entry (path, timestamp)>
<!ELEMENT path (#PCDATA)>
<!ELEMENT timestamp (#PCDATA)>
```

## 🧠 Architecture logicielle
- **Builder** – Construction des objets `Profile` avec validation.
- **Composite** – Modélisation unifiée fichiers/dossiers via `FileComponent`.
- **Visitor** – Application de traitements sans modifier les structures.
- **Chain of Responsibility** – Enchaînement des étapes de synchronisation (copie, suppression, etc.).
- **Strategy** – Persistance interchangeable (ex : XML, JSON…).
- **Factory Method** – Instanciation différée des stratégies.
- **Singleton** – Gestionnaires uniques injectés dynamiquement.
- **Façade** – Simplification de l’accès aux données pour l’affichage.

## ✅ Respect des principes SOLID
| Principe                        | Application                                                    |
| ------------------------------- | -------------------------------------------------------------- |
| SRP (Responsabilité unique)     | Chaque classe a un rôle précis                                 |
| OCP (Ouvert/fermé)              | Ajout de stratégies ou handlers sans modifier le code existant |
| LSP (Substitution de Liskov)    | Utilisation transparente des interfaces                        |
| ISP (Séparation des interfaces) | Interfaces ciblées (ex: `PersistenceStrategy<T>`)              |
| DIP (Inversion des dépendances) | Injection de dépendances par interfaces et factories           |

## 🎓 Contexte académique
- Université de Rouen Normandie
- Master 1 – Génie de l’Informatique Logicielle (GIL)
- Mini-projet d’Architecture Logicielle – Mai 2025
