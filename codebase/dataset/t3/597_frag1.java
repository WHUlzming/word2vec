    public Object clone() {

        PaceMatrix X = new PaceMatrix(m, n);

        double[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = A[i][j];

            }

        }

        return (Object) X;

    }
