    public boolean validateDirectiveSection() throws Exception {

        String testRoot = TEST_DirectiveSection;

        boolean test = true;

        sReferencePS.clear();

        sReferencePS.set("list0", EMPTY);

        sReferencePS.set("list1", "[0]");

        sReferencePS.set("list2", "[0][1]");

        sReferencePS.set(FOO, BAR);

        sTestPS.load(new File(getTestFolder(), PREFIX_RESULT + testRoot + DOT + TXT));

        test = sTestPS.contains(sReferencePS) && test;

        if (!test) {

            displayPropertySets(sTestPS, sReferencePS);

        }

        return test;

    }
