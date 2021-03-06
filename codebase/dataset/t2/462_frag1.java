    public void testRunbenchLRUSpinlock(String inputFile, Integer threads, Integer capacity, Integer round, Double scanPercentage, Integer scanLength, int pageSize) throws IOException, InterruptedException {

        File file = new File(inputFile);

        if (!file.exists()) {

            Assert.fail("file not found: " + inputFile);

        }

        String filename = FileUtils.basename(inputFile);

        long[][] dist = readDistribution(file, threads, round);

        System.gc();

        runBenchmarkWithZipfDistribution1(false, new AtomicBackoffLock(), filename, threads, capacity, round, scanPercentage, scanLength, ReplacementAlgorithm.LRU, pageSize, dist);
