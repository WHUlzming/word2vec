    public boolean validateMethodSetFullFileName() throws Exception {

        String testRoot = TEST_MethodSetFullFileName;

        boolean test = true;

        sReferencePS.clear();

        sReferencePS.set(NAME_FileNameRoot, PREFIX_RESULT + testRoot + DOT + TXT);

        sReferencePS.set(NAME_FullFileName, PREFIX_RESULT + testRoot + DOT + TXT);

        sReferencePS.set(NAME_FileNamePrefix, EMPTY);

        sReferencePS.set(NAME_FileNameSuffix, EMPTY);

        sTestPS.load(new File(getTestFolder(), PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.equals(sReferencePS);

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
