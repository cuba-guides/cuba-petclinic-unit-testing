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
  public void aPetWithMatchingPetType_isCounted() {

    // given:
    owner.pets = Arrays.asList(
        data.petWithType(electric)
    );

    // expect:
    assertThat(owner.petsOfType(electric))
        .isEqualTo(1);
  }

  @Test
  public void aPetWithNonMatchingPetType_isNotCounted() {

    // given:
    owner.pets = Arrays.asList(
        data.petWithType(electric)
    );

    // expect:
    assertThat(owner.petsOfType(fire))
        .isEqualTo(0);
  }


  @Test
  public void twoPetsMatch_andOneNot_twoIsReturned() {

    // given:
    owner.pets = Arrays.asList(
        data.petWithType(electric),
        data.petWithType(fire),
        data.petWithType(electric)
    );

    // expect:
    assertThat(owner.petsOfType(electric))
        .isEqualTo(2);
  }


  @Test
  public void petsWithoutType_areNotConsideredInTheCounting() {

    // given:
    Pet petWithoutType = data.petWithType(null);

    // and:
    owner.pets = Arrays.asList(
        data.petWithType(electric),
        petWithoutType
    );

    // expect:
    assertThat(owner.petsOfType(electric))
        .isEqualTo(1);
  }


  @Test
  public void whenAskingForNull_zeroIsReturned() {

    // expect:
    assertThat(owner.petsOfType(null))
        .isEqualTo(0);
  }

}