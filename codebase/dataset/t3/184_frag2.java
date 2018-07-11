    private static void solve_c_svc(svm_problem prob, svm_parameter param, double[] alpha, Solver.SolutionInfo si, double Cp, double Cn) {

        int l = prob.l;

        double[] minus_ones = new double[l];

        byte[] y = new byte[l];

        int i;

        for (i = 0; i < l; i++) {

            alpha[i] = 0;

            minus_ones[i] = -1;

            if (prob.y[i] > 0) y[i] = +1; else y[i] = -1;

        }

        Solver s = new Solver();

        s.Solve(l, new SVC_Q(prob, param, y), minus_ones, y, alpha, Cp, Cn, param.eps, si, param.shrinking);

        double sum_alpha = 0;

        for (i = 0; i < l; i++) sum_alpha += alpha[i];

        if (Cp == Cn) System.out.print("nu = " + sum_alpha / (Cp * prob.l) + "\n");

        for (i = 0; i < l; i++) alpha[i] *= y[i];

    }
