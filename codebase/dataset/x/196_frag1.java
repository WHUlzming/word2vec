            int exponent = init_exp + i;

            int N = (int) Math.pow(2, exponent);

            sizes[i] = N;

            System.out.println("Real forward FFT 3D (input 1D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);

            FloatFft3D fft3 = new FloatFft3D(N, N, N);

            x = new float[N * N * 2 * N];
