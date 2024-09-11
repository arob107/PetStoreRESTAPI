package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.Employee;

/*
 * Create a new DAO interface to manage CRUD operations on the employee table. 
 * It is used by the service method to find an existing Employee row.
 */
public interface EmployeeDao extends JpaRepository<Employee, Long> {

}
