            int exponent = init_exp + i;

            int N = (int) Math.pow(2, exponent);

            sizes[i] = N;

            System.out.println("Complex forward FFT 3D (input 3D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);

            DoubleFft3D fft3 = new DoubleFft3D(N, N, N);

            x = new double[N][N][2 * N];
