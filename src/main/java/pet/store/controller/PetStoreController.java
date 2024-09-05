package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.service.PetStoreService;

/*
 *  In this step you will create the pet store controller class in a new package. 
 *  This will allow Spring to map HTTP requests to specific methods. The URI for 
 *  every request that is mapped to the controller must start with "/pet_store". 
 *  You can control the class-level mapping by specifying "/pet_store" as the 
 *  value inside the @RequestMapping annotation.
 *  
 *  This class should have the following class level annotations:
 *  i. @RestController – This tells Spring that this class is a REST controller. 
 *  As such it expects and returns JSON in the request/response bodies. The 
 *  default response status code is 200 (OK) if you don't specify something 
 *  different. And finally, this annotation tells Spring to map HTTP requests 
 *  to class methods. The annotation is in the org.springframework.web.bind.annotation 
 *  package.
 *  
 *  ii. @RequestMapping("/pet_store") – This tells Spring that the URI for every HTTP 
 *  request that is mapped to a method in this controller class must start with 
 *  "/pet_store". This annotation is in the org.springframework.web.bind.annotation 
 *  package.
 *  
 *  iii. @Slf4j – This is a Lombok annotation that creates an SLF4J logger. It adds 
 *  the logger as an instance variable named log. Use it like this:
 *  log.info("This is a log line"):
 *  This annotation is in the lombok.extern.slf4j package.
 */

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);	
	}
	
	/*
	 * Add a public method to update the pet store data. This method should:
	 * Have an @PutMapping annotation to map PUT requests to this method. The 
	 * annotation should specify that a pet store ID is present in the HTTP request URI.
	 * Return a PetStoreData object.
	 * The method parameters are the pet store ID and the pet store data. Don't forget
	 * the appropriate annotations to read the store ID from the request URI and the store
	 * data from the request body. 
	 * Set the pet store ID in the pet store data from the ID parameter.
	 * Log the method call.
	 * Call the savePetStore method in the service class.
	 */
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
}
