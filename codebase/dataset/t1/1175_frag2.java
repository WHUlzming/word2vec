    public void print(PrintWriter output, NumberFormat format, int width) {

        output.println();

        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                String s = format.format(A[i][j]);

                int padding = Math.max(1, width - s.length());

                for (int k = 0; k < padding; k++) output.print(' ');

                output.print(s);

            }

            output.println();

        }

        output.println();

    }
