package com.haulmont.sample.petclinic.web.owner.owner;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.sample.petclinic.entity.owner.Owner;
import com.haulmont.sample.petclinic.entity.pet.PetType;
import javax.inject.Inject;

@UiController("petclinic_Owner.browse")
@UiDescriptor("owner-browse.xml")
@LookupComponent("ownersTable")
@LoadDataBeforeShow
public class OwnerBrowse extends StandardLookup<Owner> {

  @Inject
  protected Dialogs dialogs;
  @Inject
  protected Messages messages;
  @Inject
  protected Metadata metadata;
  @Inject
  protected MessageBundle messageBundle;
  @Inject
  protected DataManager dataManager;
  @Inject
  protected GroupTable<Owner> ownersTable;
  @Inject
  protected Notifications notifications;

  @Subscribe("ownersTable.petsOfType")
  protected void onOwnersTablePetsOfType(ActionPerformedEvent event) {
    Owner owner = ownersTable.getSingleSelected();
    dialogs.createInputDialog(this)
        .withParameter(
            InputParameter
                .entityParameter("petType", PetType.class)
                .withRequired(true)
                .withCaption(messages.getTools().getEntityCaption(metadata.getClass(PetType.class)))

        )
        .withCaption(messageBundle.formatMessage("petsOfTypeTitle", owner.getName()))
        .withCloseListener(inputDialogCloseEvent -> {
          if (inputDialogCloseEvent.getCloseAction().equals(InputDialog.INPUT_DIALOG_OK_ACTION)) {
            PetType petType = inputDialogCloseEvent.getValue("petType");

            long amount = owner.petsOfType(petType);

            notifications.create(NotificationType.TRAY)
                .withCaption(messageBundle.formatMessage("petsOfTypeAmount", amount))
                .show();
          }
            }
        )
        .show();
  }


}