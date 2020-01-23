package com.haulmont.sample.petclinic.entity.owner;

import static org.assertj.core.api.Assertions.assertThat;

import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.PetclinicData;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerTest {

  PetclinicData data = new PetclinicData();
  private PetType electric;
  private Owner owner;
  private PetType fire;

  @BeforeEach
  public void initTestData() {
    electric = data.electricType();
    fire = data.fireType();

    owner = new Owner();
  }

  @Test
  public void givenOnePetWithElectricPet_whenAskingForElectricType_oneIsReturned() {

    // given:
    Pet electricPet = data.petWithType(electric);

    owner.pets = Arrays.asList(electricPet);

    // expect:
    assertThat(owner.petsOfType(electric))
        .isEqualTo(1);
  }

  @Test
  public void givenOnePetWithElectricPet_whenAskingForFireType_oneIsReturned() {

    // given:
    Pet electricPet = data.petWithType(electric);

    owner.pets = Arrays.asList(electricPet);

    // expect:
    assertThat(owner.petsOfType(fire))
        .isEqualTo(0);
  }


  @Test
  public void givenTwoElectricPetsAndOneFirePet_whenAskingForElectricType_twoIsReturned() {

    // given:
    Pet electricPet1 = data.petWithType(electric);
    Pet electricPet2 = data.petWithType(electric);
    Pet firePet1 = data.petWithType(fire);

    owner.pets = Arrays.asList(electricPet1, firePet1, electricPet2);

    // expect:
    assertThat(owner.petsOfType(electric))
        .isEqualTo(2);
  }


  @Test
  public void whenAskingForNull_zeroIsReturned() {

    // expect:
    assertThat(owner.petsOfType(null))
        .isEqualTo(0);
  }

}