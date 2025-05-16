/**
 * Fournit une couche d’abstraction pour les opérations sur le système de fichiers,
 * permettant une manipulation uniforme des fichiers quel que soit le protocole
 * ou l’environnement sous-jacent.
 *
 * <p>
 * Le package {@code filesystem} définit une interface générique {@link filesystem.FileHandler}
 * pour les opérations courantes : copie, suppression, lecture de date de modification
 * et vérification d’existence de fichiers. Cette abstraction permet de séparer
 * la logique de synchronisation du détail d’accès au système de fichiers.
 * </p>
 *
 * <p>
 * Une première implémentation {@link filesystem.LocalFileHandler} s’appuie sur l’API
 * {@code java.nio.file} pour effectuer les opérations sur un système de fichiers local.
 * À terme, d’autres variantes (FTP, WebDAV, cloud, etc.) pourront être introduites
 * sans modifier le reste de l’application.
 * </p>
 *
 * <p>
 * Le package utilise également le patron de conception <strong>Factory Method</strong>
 * à travers {@link filesystem.FileHandlerFactory}, permettant de produire dynamiquement
 * des gestionnaires de fichiers adaptés au contexte.
 * </p>
 *
 * <h2>Composants</h2>
 * <ul>
 *     <li>{@link filesystem.FileHandler} : interface générique pour toutes les implémentations.</li>
 *     <li>{@link filesystem.LocalFileHandler} : implémentation locale utilisant {@code java.nio.file}.</li>
 *     <li>{@link filesystem.FileHandlerFactory} : fabrique statique permettant l’instanciation centralisée.</li>
 * </ul>
 *
 * @since JDK 17
 */
package filesystem;