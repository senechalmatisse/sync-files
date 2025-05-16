/**
 * Contient les points d’entrée des différentes applications de la suite de synchronisation.
 *
 * <p>
 * Ce package regroupe les classes clientes exécutables du projet, chacune représentant une fonctionnalité
 * distincte accessible via la ligne de commande. Chaque classe s’appuie sur les sous-systèmes
 * définis dans les autres packages, tout en appliquant des patrons de conception appropriés
 * à son domaine d’exécution.
 * </p>
 *
 * <h2>Applications principales</h2>
 * <ul>
 *     <li>{@link app.NewProfileApp} : création d’un profil de synchronisation en ligne de commande.
 *         Utilise le patron <strong>Builder</strong> pour construire un objet {@link model.Profile}
 *         et le patron <strong>Singleton</strong> pour le gérer via {@link manager.ProfileManager}.</li>
 *
 *     <li>{@link app.SyncApp} : lance une synchronisation bidirectionnelle des fichiers entre deux répertoires.
 *         Implémente une architecture modulaire basée sur les patrons <strong>Composite</strong>,
 *         <strong>Visitor</strong>, <strong>Chain of Responsibility</strong>, et <strong>Singleton</strong>.</li>
 *
 *     <li>{@link app.SyncStatApp} : affiche l’état d’un profil de synchronisation (chemins + registre).
 *         Repose sur une interface unifiée via le patron <strong>Façade</strong>
 *         (voir {@link stat.SyncStatFacade}).</li>
 * </ul>
 *
 * <h2>Objectifs</h2>
 * <p>
 * Le package {@code app} constitue la couche la plus externe du système, jouant le rôle de client.
 * Il ne contient aucune logique métier propre, mais orchestre les composants fournis
 * par les packages internes selon le principe de séparation des responsabilités.
 * </p>
 *
 * @see model.Profile
 * @see manager.ProfileManager
 * @see manager.RegistryManager
 * @see stat.SyncStatFacade
 * @since JDK 17
 */
package app;