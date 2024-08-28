package pet.store.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Employee {

	// add @ID and @GeneratedValue annotations to primary key(s)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeId;

	private String employeeFirstName;
	private String employeeLastName;
	private String employeePhoneNumber;
	private String employeeJobTitle;

	/*
	 * Add @EqualsAndHashCode.Exclude and @ToString.Exclude to all of the recursive
	 * relationship variables. This will prevent recursion from occurring when the
	 * .toString(), .equals(), or .hashCode() methods are called.
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pet_store_id")
	private PetStore petStore;
}
