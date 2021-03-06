    private javax.swing.JComboBox getLocalDrivesComboBox() {

        updateDriveList = true;

        File[] roots = File.listRoots();

        String[] localDisks = new String[roots.length];

        for (int i = 0; i < roots.length; i++) {

            localDisks[i] = roots[i].toString();

        }

        if (localDrivesComboBox == null) {

            localDrivesComboBox = new javax.swing.JComboBox(localDisks);

            localDrivesComboBox.setName("LocalDisks");

            localDrivesComboBox.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));

            localDrivesComboBox.addActionListener(this);

        }

        updateDriveList = false;

        return localDrivesComboBox;

    }
