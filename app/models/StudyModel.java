package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.TypedQuery;

import play.data.validation.ValidationError;
import play.db.jpa.JPA;

@Entity
public class StudyModel {

	@Id
	@GeneratedValue
	private Long id;

	private String title;

	@Lob
	private String description;

	private Timestamp date;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "StudyMemberMap", joinColumns = { @JoinColumn(name = "member_email", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "study_id", referencedColumnName = "email") })
	private Set<UserModel> memberList = new HashSet<UserModel>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderColumn(name = "componentList_order")
	@JoinColumn(name = "study_id")
	private List<ComponentModel> componentList = new ArrayList<ComponentModel>();

	public StudyModel() {
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDate(Timestamp timestamp) {
		this.date = timestamp;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setMemberList(Set<UserModel> memberList) {
		this.memberList = memberList;
	}

	public Set<UserModel> getMemberList() {
		return memberList;
	}

	public void addMember(UserModel user) {
		memberList.add(user);
	}

	public void removeMember(UserModel user) {
		memberList.remove(user);
	}

	public boolean hasMember(UserModel user) {
		return memberList.contains(user);
	}

	public void setComponentList(List<ComponentModel> componentList) {
		this.componentList = componentList;
	}

	public List<ComponentModel> getComponentList() {
		return this.componentList;
	}

	public void addComponent(ComponentModel component) {
		componentList.add(component);
	}

	public void removeComponent(ComponentModel component) {
		componentList.remove(component);
	}

	public boolean hasComponent(ComponentModel component) {
		return componentList.contains(component);
	}

	public ComponentModel getFirstComponent() {
		if (componentList.size() > 0) {
			return componentList.get(0);
		}
		return null;
	}

	public ComponentModel getNextComponent(ComponentModel component) {
		int index = componentList.indexOf(component);
		if (index < componentList.size() - 1) {
			return componentList.get(index + 1);
		}
		return null;
	}

	public void componentOrderMinusOne(ComponentModel component) {
		int index = componentList.indexOf(component);
		if (index > 0) {
			ComponentModel prevComponent = componentList.get(index - 1);
			componentOrderSwap(component, prevComponent);
		}
	}

	public void componentOrderPlusOne(ComponentModel component) {
		int index = componentList.indexOf(component);
		if (index < (componentList.size() - 1)) {
			ComponentModel nextComponent = componentList.get(index + 1);
			componentOrderSwap(component, nextComponent);
		}
	}

	public void componentOrderSwap(ComponentModel component1,
			ComponentModel component2) {
		int index1 = componentList.indexOf(component1);
		int index2 = componentList.indexOf(component2);
		ComponentModel.changeComponentOrder(component1, index2);
		ComponentModel.changeComponentOrder(component2, index1);
	}

	public void update(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public List<ValidationError> validate() {
		List<ValidationError> errorList = new ArrayList<ValidationError>();
		if (this.title == null || this.title.isEmpty()) {
			errorList.add(new ValidationError("title", "Missing title"));
		}
		return errorList.isEmpty() ? null : errorList;
	}

	@Override
	public String toString() {
		return id + " " + title;
	}
	
	public static StudyModel findById(Long id) {
		return JPA.em().find(StudyModel.class, id);
	}

	public static List<StudyModel> findAll() {
		TypedQuery<StudyModel> query = JPA.em().createQuery(
				"SELECT e FROM StudyModel e", StudyModel.class);
		return query.getResultList();
	}

	public void persist() {
		JPA.em().persist(this);
	}

	public void merge() {
		JPA.em().merge(this);
	}

	public void remove() {
		JPA.em().remove(this);
	}

	public void refresh() {
		JPA.em().refresh(this);
	}

}