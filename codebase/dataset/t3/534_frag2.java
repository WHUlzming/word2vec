            } else if (cmd.startsWith("probB")) {

                int n = model.nr_class * (model.nr_class - 1) / 2;

                model.probB = new double[n];

                StringTokenizer st = new StringTokenizer(arg);

                for (int i = 0; i < n; i++) model.probB[i] = atof(st.nextToken());

            } else if (cmd.startsWith("nr_sv")) {

                int n = model.nr_class;

                model.nSV = new int[n];

                StringTokenizer st = new StringTokenizer(arg);

                for (int i = 0; i < n; i++) model.nSV[i] = atoi(st.nextToken());

            } else if (cmd.startsWith("SV")) {
