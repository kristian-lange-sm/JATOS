package models.common;

import java.io.File;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import utils.common.JsonUtils;

/**
 * Domain model / entity of a component. It's used by JPA and JSON marshaling.
 * The corresponding UI model is {@link models.gui.ComponentProperties}.
 * 
 * @author Kristian Lange
 */
@Entity
@Table(name = "Component")
public class Component {

	/**
	 * Version of this model used for serialisation (e.g. JSON marshaling)
	 */
	public static final int SERIAL_VERSION = 1;
	public static final String COMPONENT = "component";

	@Id
	@GeneratedValue
	@JsonView(JsonUtils.JsonForPublix.class)
	private Long id;

	/**
	 * Universally, (world-wide) unique ID. Used for import/export between
	 * different JATOS instances. A study can have only one component with the
	 * same UUID, although it is allowed to have other studies that have this
	 * component with this UUID.
	 */
	@Column(unique = false, nullable = false)
	@JsonView({ JsonUtils.JsonForPublix.class, JsonUtils.JsonForIO.class })
	private String uuid;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id", insertable = false, updatable = false, nullable = false)
	private Study study;

	@JsonView({ JsonUtils.JsonForPublix.class, JsonUtils.JsonForIO.class })
	private String title;

	/**
	 * Timestamp of the creation or the last update of this component
	 */
	@JsonIgnore
	private Timestamp date;

	/**
	 * Local path to component's HTML file in the study assets' dir. File
	 * separators are persisted as '/'.
	 */
	@JsonView({ JsonUtils.JsonForPublix.class, JsonUtils.JsonForIO.class })
	@JoinColumn(name = "viewUrl")
	private String htmlFilePath;

	@JsonView({ JsonUtils.JsonForPublix.class, JsonUtils.JsonForIO.class })
	private boolean reloadable = false;

	/**
	 * An inactive component can't be used within a study - it generates an
	 * error message if one try. Further it's skipped if one uses
	 * startNextComponent from the public API.
	 */
	@JsonView(JsonUtils.JsonForIO.class)
	private boolean active = true;

	/**
	 * User comments, reminders, something to share with others. They have no
	 * further meaning.
	 */
	@Lob
	@JsonView({ JsonUtils.JsonForIO.class })
	private String comments;

	/**
	 * Data in JSON format: every component run of this Component gets access to
	 * them. They can be changed in the GUI but not via jatos.js. Can be used
	 * for initial data and configuration.
	 */
	@JsonView({ JsonUtils.JsonForPublix.class, JsonUtils.JsonForIO.class })
	@Lob
	private String jsonData;

	public Component() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Study getStudy() {
		return this.study;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = htmlFilePath;
	}

	public String getHtmlFilePath() {
		if (htmlFilePath != null) {
			return this.htmlFilePath.replace('/', File.separatorChar);
		} else {
			return null;
		}
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return this.comments;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = JsonUtils.asStringForDB(jsonData);
	}

	public boolean isReloadable() {
		return reloadable;
	}

	public void setReloadable(boolean reloadable) {
		this.reloadable = reloadable;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return String.valueOf(id) + " " + title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Component)) {
			return false;
		}
		Component other = (Component) obj;
		if (id == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}

}
