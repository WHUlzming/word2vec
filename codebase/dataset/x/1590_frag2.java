import javax.persistence.Table;

import mase.team.TeamMember;

import mase.util.WikipageService;

import org.apache.log4j.Category;

import org.apache.log4j.Level;

import org.hibernate.annotations.IndexColumn;



/**

 * WikiPage: MASE_EJB3

 * Package: mase.planninggame

 * Class: WikiPage.java

 * @author xueling shu

 *

 * TODO: <Describe Class>

 */

@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "WikiPage")

@Table(name = "WikiPage")

public class WikiPage implements Serializable {
