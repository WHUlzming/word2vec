        double[][] C = X.getArray();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                C[i][j] = B.A[i][j] / A[i][j];

            }

        }

        return X;

    }
