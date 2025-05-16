/**
 * Fournit une façade simplifiée pour consulter l’état d’un profil de synchronisation.
 *
 * <p>
 * Le package {@code stat} contient une classe principale, {@link stat.SyncStatFacade},
 * qui implémente le patron de conception <strong>Façade</strong>. Il encapsule l'accès
 * combiné aux gestionnaires de profil et de registre ({@link manager.ProfileManager},
 * {@link manager.RegistryManager}) afin de centraliser l'affichage des informations
 * liées à un profil de synchronisation.
 * </p>
 *
 * <p>
 * Ce module est principalement utilisé dans le cadre d’un outil de diagnostic
 * ou de contrôle, pour observer l’état actuel d’un profil :
 * </p>
 * <ul>
 *     <li>Nom du profil et chemins des dossiers synchronisés ;</li>
 *     <li>Entrées du registre avec date de dernière modification ;</li>
 *     <li>Informations lisibles destinées à la console.</li>
 * </ul>
 *
 * <p>
 * Cette façade est pensée comme un composant statique et non instanciable,
 * facilitant son utilisation dans des contextes tels que des interfaces CLI ou des tests.
 * </p>
 *
 * @see stat.SyncStatFacade
 * @see manager.ProfileManager
 * @see manager.RegistryManager
 * @since JDK 17
 */
package stat;