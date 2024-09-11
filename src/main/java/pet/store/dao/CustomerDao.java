package pet.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.Customer;

/*
 * Create a new DAO interface to manage CRUD operations on the customer table. 
 * It is used by the service method to find an existing Customer row.
 */
public interface CustomerDao extends JpaRepository<Customer, Long> {

}
