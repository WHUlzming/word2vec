        {

            fp.writeBytes("rho");

            for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++) fp.writeBytes(" " + model.rho[i]);

            fp.writeBytes("\n");

        }

        if (model.label != null) {

            fp.writeBytes("label");

            for (int i = 0; i < nr_class; i++) fp.writeBytes(" " + model.label[i]);

            fp.writeBytes("\n");

        }

        if (model.probA != null) {
