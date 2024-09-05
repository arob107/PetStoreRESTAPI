package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
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
	 * This method will save a pet store. It first instantiates a petStoreId, then
	 * checks to see if the pet store already exists using findOrCreatePetStore method. 
	 * This method will create a pet store if it doesn't exist.
	 * This method will also copy pet store fields using a method. Matching fields are 
	 * copied from the PetStoreData object to the PetStore object. The customers and
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
}
