    public ChoiceGroup getLstConservazione() {

        if (lstConservazione == null) {

            lstConservazione = new ChoiceGroup("Conservazione", Choice.POPUP);

            lstConservazione.setSelectedFlags(new boolean[] {});

        }

        return lstConservazione;

    }
