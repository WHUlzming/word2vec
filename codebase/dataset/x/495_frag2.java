            for (int i = 0; i < nr_class; i++) fp.writeBytes(" " + model.label[i]);

            fp.writeBytes("\n");

        }

        if (model.probA != null) {

            fp.writeBytes("probA");

            for (int i = 0; i < nr_class * (nr_class - 1) / 2; i++) fp.writeBytes(" " + model.probA[i]);
