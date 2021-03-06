    private void loadModelFiles(String modelName) throws FileNotFoundException, IOException, ZipException {

        logger.config("Loading Sphinx3 acoustic model: " + modelName);

        logger.config("    Path      : " + location);

        logger.config("    modellName: " + model);

        logger.config("    dataDir   : " + dataDir);

        if (binary) {

            meansPool = loadDensityFileBinary(dataDir + "means", -Float.MAX_VALUE);

            variancePool = loadDensityFileBinary(dataDir + "variances", varianceFloor);

            mixtureWeightsPool = loadMixtureWeightsBinary(dataDir + "mixture_weights", mixtureWeightFloor);

            matrixPool = loadTransitionMatricesBinary(dataDir + "transition_matrices");

        } else {

            meansPool = loadDensityFileAscii(dataDir + "means.ascii", -Float.MAX_VALUE);

            variancePool = loadDensityFileAscii(dataDir + "variances.ascii", varianceFloor);

            mixtureWeightsPool = loadMixtureWeightsAscii(dataDir + "mixture_weights.ascii", mixtureWeightFloor);

            matrixPool = loadTransitionMatricesAscii(dataDir + "transition_matrices.ascii");

        }

        senonePool = createSenonePool(distFloor, varianceFloor);

        InputStream modelStream = getClass().getResourceAsStream(model);

        if (modelStream == null) {

            throw new IOException("can't find model " + model);

        }

        loadHMMPool(useCDUnits, modelStream, location + File.separator + model);

    }
