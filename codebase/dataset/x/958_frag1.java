    public static int[][] getColumnsCopy(int[][] M, int... J) {

        int[][] array = new int[M.length][J.length];

        for (int i = 0; i < M.length; i++) {

            for (int j = 0; j < J.length; j++) {

                array[i][j] = M[i][J[j]];

            }

        }

        return array;

    }
