package com.haulmont.sample.petclinic.web.visit.visit;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.pet.Pet;
import com.haulmont.sample.petclinic.entity.visit.Visit;
import javax.inject.Inject;

@UiController("petclinic_Visit.browse")
@UiDescriptor("visit-browse.xml")
@LookupComponent("visitsTable")
@LoadDataBeforeShow
public class VisitBrowse extends StandardLookup<Visit> {

  @Inject
  protected Dialogs dialogs;
  @Inject
  protected MessageBundle messageBundle;
  @Inject
  protected ScreenBuilders screenBuilders;
  @Inject
  protected GroupTable<Visit> visitsTable;
  @Inject
  protected Messages messages;
  @Inject
  protected MetadataTools metadataTools;
  @Inject
  protected Metadata metadata;
  @Inject
  protected DataManager dataManager;

  @Subscribe("visitsTable.createRegularCheckup")
  protected void onVisitsTableCreateRegularCheckup(ActionPerformedEvent event) {

    String petAttribute = "pet";

    dialogs.createInputDialog(this)
        .withCaption(messageBundle.getMessage("createRegularCheckup"))
        .withParameter(
            InputParameter
                .entityParameter(petAttribute, Pet.class)
                .withCaption(visitAttributeCaption(petAttribute))
                .withRequired(true)
        )
        .withCloseListener(inputDialogCloseEvent -> {
          if (inputDialogCloseEvent.getCloseAction().equals(InputDialog.INPUT_DIALOG_OK_ACTION)) {
            Pet pet = inputDialogCloseEvent.getValue(petAttribute);
            createRegularCheckupEditorFor(pet);
          }
        })
    .show();

  }

  private void createRegularCheckupEditorFor(Pet pet) {
    screenBuilders.editor(visitsTable)
        .withScreenClass(VisitCreateRegularCheckup.class)
        .newEntity()
        .withInitializer(visit ->
            visit.setPet(
                dataManager.reload(pet, "pet-with-owner-and-type")
            )
        )
        .show();
  }

  private String visitAttributeCaption(String propertyName) {
    return messages
        .getTools()
        .getPropertyCaption(
          metadata.getClass(Visit.class),
            propertyName
        );
  }


}