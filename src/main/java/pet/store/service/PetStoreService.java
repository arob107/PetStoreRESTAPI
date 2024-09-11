package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	/*
	 * Add a PetStoreDao object named petStoreDao as a private instance variable.
	 * Annotate the instance variable with @Autowired so that Spring can inject the
	 * DAO object into the variable.
	 */
	@Autowired
	private PetStoreDao petStoreDao;

	/*
	 * Inject the EmployeeDao object into the pet store service class using
	 * the @Autowired annotation.
	 */
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CustomerDao customerDao;

	/*
	 * This method will save a pet store. It first instantiates a petStoreId, then
	 * checks to see if the pet store already exists using findOrCreatePetStore
	 * method. This method will create a pet store if it doesn't exist. This method
	 * will also copy pet store fields using a method. Matching fields are copied
	 * from the PetStoreData object to the PetStore object. The customers and
	 * employees fields are not copied in this method.
	 */
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();

		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);

		PetStore dbPetStore = petStoreDao.save(petStore);
		return new PetStoreData(dbPetStore);
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;

		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}

		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " does not exist."));
	}

	/* @formatter:off
	 * Create a method named findEmployeeById(). It should take the pet store ID and the employee ID as parameters.
	 * Use the employeeDAO method findById() to return the Employee object. If the employee isn't found, 
	 * throw a new NoSuchElementException().
	 * 
	 * If the pet store ID in the Employee object's PetStore variable does not match the given pet store ID, 
	 * throw a new IllegalArgumentException.
	 * 
	 * If everything is OK, the method should return the Employee object. 
	 * @formatter:on
	 */
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		return employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " does not exist."));
	}

	/*
	 * Create a new method findOrCreateEmployee().
	 * 
	 * a. This method should take an employee ID as a parameter (this will be null
	 * if the employee is being created), as well as the pet store ID. It will
	 * return an Employee object if successful.
	 * 
	 * b. If the employee ID is null, it should return a new Employee object.
	 * 
	 * c. If the employee ID is not null, it should call the method,
	 * findEmployeeById().
	 */
	private Employee findOrCreateEmployee(Long employeeId, Long petStoreId) {
		Employee employee;

		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(petStoreId, employeeId);
		}

		return employee;
	}

	/*
	 * Create a new method copyEmployeeFields().
	 * 
	 * a. The method should take an Employee as a parameter and a PetStoreEmployee
	 * as a parameter.
	 * 
	 * b. Copy all matching PetStoreEmployee fields to the Employee object.
	 */
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhoneNumber(petStoreEmployee.getEmployeePhoneNumber());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	/*
	 * Add a new method named saveEmployee(). This method should take a pet store ID
	 * and a PetStoreEmployee object as parameters. It must return a
	 * PetStoreEmployee object.
	 */
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		// Call findPetStoreById() to find the pet store object.
		PetStore petStore = findPetStoreById(petStoreId);

		Long employeeId = petStoreEmployee.getEmployeeId();

		// Call findOrCreateEmployee() to retrieve an existing employee or to create a
		// new one
		Employee employee = findOrCreateEmployee(employeeId, petStoreId);

		/*
		 * Call copyEmployeeFields() to copy the data in the pet store employee
		 * parameter (which ultimately came from the JSON in the HTTP POST request
		 * payload) to the Employee object.
		 */
		copyEmployeeFields(employee, petStoreEmployee);

		// Set the PetStore object in the Employee object.
		employee.setPetStore(petStore);

		// Add the Employee object into the Set of Employee objects in the PetStore
		// object.
		petStore.getEmployees().add(employee);

		// Save the employee by calling the save() method in the employee DAO.
		Employee dbEmployee = employeeDao.save(employee);

		// Convert the Employee object returned by the save method to a PetStoreEmployee
		// object and return it.
		return new PetStoreEmployee(dbEmployee);
	} // end of saveEmployee

	/*
	 * Note: that customer and pet store have a many-to-many relationship. This
	 * means that a Customer object has a List of PetStore objects. This means that,
	 * in the method findCustomerById(), you will need to loop through the list of
	 * PetStore objects looking for the pet store with the given pet store ID. If
	 * not found, throw an IllegalArgumentException.
	 */
	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));

		boolean found = false;

		// loop through list of PetStore objects in customer
		for (PetStore petStore : customer.getPetStores()) {
			// if the given pet store ID is found, break the loop
			if (petStore.getPetStoreId() == petStoreId) {
				found = true;
				break;
			}
		}
		// if the given pet store ID isn't found, through an IllegalArgumentException
		if (!found) {
			throw new IllegalArgumentException(
					"The customer with ID=" + customerId + " is not a member of the pet store with ID=" + petStoreId);
		}

		return customer;
	} // end of findCustomerById

	private Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
		Customer customer;

		if (Objects.isNull(customerId)) {
			customer = new Customer();
		} else {
			customer = findCustomerById(petStoreId, customerId);
		}

		return customer;
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}

	/*
	 * Add PetStore Customer In this section, you will add a customer to an existing
	 * pet store.
	 * 
	 * Follow the instructions in the Add store employee section, above using the
	 * pseudocode instructions modified to use customer instead of employee.
	 * 
	 * Modify the instructions to add the customer.
	 * 
	 * Note: that the controller and service methods should use a PetStoreCustomer
	 * DTO instead of PetStoreEmployee. (You have already created PetStoreCustomer
	 * in the pet.store.controller.model package.)
	 * 
	 * Make sure that the request used to add a customer is an HTTP POST request
	 * sent to
	 * 
	 * http://localhost:8080/pet_store/{ID}/customer
	 * 
	 * where {ID} is the primary key value of an existing pet store record. You can
	 * find sample JSON to add a customer in the student resources.
	 */
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		// Call findPetStoreById() to find the pet store object.
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(customerId, petStoreId);

		copyCustomerFields(customer, petStoreCustomer);

		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);

		Customer dbCustomer = customerDao.save(customer);

		return new PetStoreCustomer(dbCustomer);
	} // end of saveCustomer

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {

		// Call the findAll() method in the pet store DAO. Convert the List of
		// PetStore objects to a List of PetStoreData objects.
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();

		// Remove all customer and employee objects in each PetStoreData object.
		for (PetStore petStore : petStores) {
			PetStoreData petStoreData = new PetStoreData(petStore);
			
			// comment these lines out to see the customers and employees at each store
			petStoreData.getCustomers().clear();
			petStoreData.getEmployees().clear();
			
			result.add(petStoreData);
		}
		
		return result;
	} // end of retrieveAllPetStores

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

	

}
