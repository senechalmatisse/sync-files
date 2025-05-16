/**
 * Fournit les composants nécessaires à la construction d’objets {@link model.Profile}
 * via le patron de conception <strong>Builder</strong>.
 *
 * <p>
 * Le package {@code builder} encapsule l’implémentation du patron Builder dans un contexte
 * de configuration de profils de synchronisation. Il permet de séparer la logique
 * de construction d’un objet complexe (profil) de sa représentation interne,
 * tout en assurant la cohérence des données fournies.
 * </p>
 *
 * <p>
 * Ce patron est particulièrement adapté ici car un profil est une entité dont
 * l’instanciation nécessite plusieurs étapes : initialisation, définition du nom,
 * des chemins A et B. Le builder permet également de valider la complétude
 * de ces données avant la création de l’objet final.
 * </p>
 *
 * <h2>Composants principaux</h2>
 * <ul>
 *     <li>{@link builder.ProfileBuilder} : interface définissant les étapes de construction d’un {@link model.Profile}.</li>
 *     <li>{@link builder.ConcreteProfileBuilder} : implémentation concrète fournissant une construction standard, avec validations intégrées.</li>
 *     <li>{@link builder.ProfileDirector} : directeur responsable d’orchestrer la construction en invoquant les étapes du {@code Builder} dans le bon ordre.</li>
 * </ul>
 *
 * <p>
 * Ce package est principalement utilisé dans l’application cliente
 * {@code NewProfileApp}, qui agit comme client du patron Builder.
 * L’application utilise uniquement l’interface du {@code Director},
 * garantissant une encapsulation totale du processus de construction.
 * </p>
 *
 * <p>
 * Cette organisation renforce la lisibilité, favorise la réutilisabilité
 * et respecte pleinement les principes de modularité et de responsabilité unique.
 *
 * @see model.Profile
 * @since JDK 17
 */
package builder;