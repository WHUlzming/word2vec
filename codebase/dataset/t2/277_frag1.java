    public PNMatrix plus(PNMatrix B) {

        checkMatrixDimensions(B);

        PNMatrix X = new PNMatrix(m, n);

        int[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = A[i][j] + B.A[i][j];

            }

        }

        return X;

    }