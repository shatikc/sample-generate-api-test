
package com.example.petstore.controller;


import com.example.petstore.api.PetApi;
import com.example.petstore.model.Category;
import com.example.petstore.model.ModelApiResponse;
import com.example.petstore.model.Pet;
import com.example.petstore.model.Tag;
import com.google.common.base.Enums;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

@RestController
public class PetController implements PetApi {
    HashMap<Long, Pet> petsById;
    public PetController() {
        Category category = new Category() {{
            setId(1L);
            setName("Dog");
        }};
        List<Tag> tags = new ArrayList<>();
        petsById = new HashMap<>();
        for (int i = 0; i < 5; i++) {
             int finalI = i;
             Pet pet = new Pet() {{
                setId((long) finalI);
                setName("test" + finalI);
                setCategory(category);
                setTags(tags);
                setStatus(StatusEnum.PENDING);
            }};
            petsById.put((long) finalI,pet);
        }
    }

    @Override
    public ResponseEntity<Pet> addPet(@Valid Pet pet) {
        petsById.put(pet.getId(), pet);
        return new ResponseEntity<>(petsById.get(pet.getId()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deletePet(Long petId, String apiKey) {
        petsById.remove(petId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<List<Pet>>> findPetsByStatus(@Valid String status) {
       try {
           Pet.StatusEnum.fromValue(status);
       } catch (Exception exception) {
           return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
       }
        ArrayList<Pet> result = new ArrayList<>();
        for (Pet p: petsById.values()) {
            if (p.getStatus().toString().equals(status)) {
                result.add(p);
            }
        };
        ArrayList<List<Pet>>returnList = new ArrayList<>();
        returnList.add(result);
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<List<Pet>>> findPetsByTags(@Valid List<String> tags) {
        return null;
    }

    @Override
    public ResponseEntity<Pet> getPetById(Long petId) {
        if (petsById.get(petId) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Pet>(petsById.get(petId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Pet> updatePet(@Valid Pet pet) {
        if (petsById.get(pet.getId()) == null) {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        petsById.put(pet.getId(), pet);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updatePetWithForm(Long petId, @Valid String name, @Valid String status) {
        return null;
    }

    @Override
    public ResponseEntity<ModelApiResponse> uploadFile(Long petId, @Valid String additionalMetadata, @Valid Resource body) {
        return null;
    }


}

