        if (amh.getMessage("menuitem.encoding").equals(label)) {

            appletCharacterEncoding();

            return;

        }

        if (amh.getMessage("menuitem.edit").equals(label)) {

            appletEdit();

            return;

        }

        if (amh.getMessage("menuitem.props").equals(label)) {

            networkProperties();

            return;

        }

        if (amh.getMessage("menuitem.close").equals(label)) {

            appletClose();

            return;

        }

        if (factory.isStandalone() && amh.getMessage("menuitem.quit").equals(label)) {
