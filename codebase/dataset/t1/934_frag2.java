    public static svm_model svm_train(svm_problem prob, svm_parameter param) {

        svm_model model = new svm_model();

        model.param = param;

        if (param.svm_type == svm_parameter.ONE_CLASS || param.svm_type == svm_parameter.EPSILON_SVR || param.svm_type == svm_parameter.NU_SVR) {

            model.nr_class = 2;

            model.label = null;

            model.nSV = null;

            model.probA = null;

            model.probB = null;

            model.sv_coef = new double[1][];

            if (param.probability == 1 && (param.svm_type == svm_parameter.EPSILON_SVR || param.svm_type == svm_parameter.NU_SVR)) {

                model.probA = new double[1];

                model.probA[0] = svm_svr_probability(prob, param);

            }

            decision_function f = svm_train_one(prob, param, 0, 0);

            model.rho = new double[1];

            model.rho[0] = f.rho;

            int nSV = 0;

            int i;

            for (i = 0; i < prob.l; i++) if (Math.abs(f.alpha[i]) > 0) ++nSV;

            model.l = nSV;

            model.SV = new svm_node[nSV][];

            model.sv_coef[0] = new double[nSV];

            int j = 0;

            for (i = 0; i < prob.l; i++) if (Math.abs(f.alpha[i]) > 0) {

                model.SV[j] = prob.x[i];

                model.sv_coef[0][j] = f.alpha[i];

                ++j;

            }

        } else {

            int l = prob.l;

            int[] tmp_nr_class = new int[1];

            int[][] tmp_label = new int[1][];

            int[][] tmp_start = new int[1][];

            int[][] tmp_count = new int[1][];

            int[] perm = new int[l];

            svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start, tmp_count, perm);

            int nr_class = tmp_nr_class[0];

            int[] label = tmp_label[0];

            int[] start = tmp_start[0];

            int[] count = tmp_count[0];

            svm_node[][] x = new svm_node[l][];

            int i;

            for (i = 0; i < l; i++) x[i] = prob.x[perm[i]];

            double[] weighted_C = new double[nr_class];

            for (i = 0; i < nr_class; i++) weighted_C[i] = param.C;

            for (i = 0; i < param.nr_weight; i++) {

                int j;

                for (j = 0; j < nr_class; j++) if (param.weight_label[i] == label[j]) break;

                if (j == nr_class) System.err.print("warning: class label " + param.weight_label[i] + " specified in weight is not found\n"); else weighted_C[j] *= param.weight[i];

            }

            boolean[] nonzero = new boolean[l];

            for (i = 0; i < l; i++) nonzero[i] = false;

            decision_function[] f = new decision_function[nr_class * (nr_class - 1) / 2];

            double[] probA = null, probB = null;

            if (param.probability == 1) {

                probA = new double[nr_class * (nr_class - 1) / 2];

                probB = new double[nr_class * (nr_class - 1) / 2];

            }

            int p = 0;

            for (i = 0; i < nr_class; i++) for (int j = i + 1; j < nr_class; j++) {

                svm_problem sub_prob = new svm_problem();

                int si = start[i], sj = start[j];

                int ci = count[i], cj = count[j];

                sub_prob.l = ci + cj;

                sub_prob.x = new svm_node[sub_prob.l][];

                sub_prob.y = new double[sub_prob.l];

                int k;

                for (k = 0; k < ci; k++) {

                    sub_prob.x[k] = x[si + k];

                    sub_prob.y[k] = +1;

                }

                for (k = 0; k < cj; k++) {

                    sub_prob.x[ci + k] = x[sj + k];

                    sub_prob.y[ci + k] = -1;

                }

                if (param.probability == 1) {

                    double[] probAB = new double[2];

                    svm_binary_svc_probability(sub_prob, param, weighted_C[i], weighted_C[j], probAB);

                    probA[p] = probAB[0];

                    probB[p] = probAB[1];

                }

                f[p] = svm_train_one(sub_prob, param, weighted_C[i], weighted_C[j]);

                for (k = 0; k < ci; k++) if (!nonzero[si + k] && Math.abs(f[p].alpha[k]) > 0) nonzero[si + k] = true;

                for (k = 0; k < cj; k++) if (!nonzero[sj + k] && Math.abs(f[p].alpha[ci + k]) > 0) nonzero[sj + k] = true;

                ++p;

            }

            model.nr_class = nr_class;

            model.label = new int[nr_class];

            for (i = 0; i < nr_class; i++) model.label[i] = label[i];

            model.rho = new double[nr_class * (nr_class - 1) / 2];

            for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) model.rho[i] = f[i].rho;

            if (param.probability == 1) {

                model.probA = new double[nr_class * (nr_class - 1) / 2];

                model.probB = new double[nr_class * (nr_class - 1) / 2];

                for (i = 0; i < nr_class * (nr_class - 1) / 2; i++) {

                    model.probA[i] = probA[i];

                    model.probB[i] = probB[i];

                }

            } else {

                model.probA = null;

                model.probB = null;

            }

            int nnz = 0;

            int[] nz_count = new int[nr_class];

            model.nSV = new int[nr_class];

            for (i = 0; i < nr_class; i++) {

                int nSV = 0;

                for (int j = 0; j < count[i]; j++) if (nonzero[start[i] + j]) {

                    ++nSV;

                    ++nnz;

                }

                model.nSV[i] = nSV;

                nz_count[i] = nSV;

            }

            System.out.print("Total nSV = " + nnz + "\n");

            model.l = nnz;

            model.SV = new svm_node[nnz][];

            p = 0;

            for (i = 0; i < l; i++) if (nonzero[i]) model.SV[p++] = x[i];

            int[] nz_start = new int[nr_class];

            nz_start[0] = 0;

            for (i = 1; i < nr_class; i++) nz_start[i] = nz_start[i - 1] + nz_count[i - 1];

            model.sv_coef = new double[nr_class - 1][];

            for (i = 0; i < nr_class - 1; i++) model.sv_coef[i] = new double[nnz];

            p = 0;

            for (i = 0; i < nr_class; i++) for (int j = i + 1; j < nr_class; j++) {

                int si = start[i];

                int sj = start[j];

                int ci = count[i];

                int cj = count[j];

                int q = nz_start[i];

                int k;

                for (k = 0; k < ci; k++) if (nonzero[si + k]) model.sv_coef[j - 1][q++] = f[p].alpha[k];

                q = nz_start[j];

                for (k = 0; k < cj; k++) if (nonzero[sj + k]) model.sv_coef[i][q++] = f[p].alpha[ci + k];

                ++p;

            }

        }

        return model;

    }
