    public void testGeneralPunctuationCategory() {

        String[] s = { ",", "!", "\"", "#", "%", "&", "'", "(", ")", "-", ".", "/" };

        String regexp = "\\p{P}";

        for (int i = 0; i < s.length; i++) {

            Pattern pattern = Pattern.compile(regexp);

            Matcher matcher = pattern.matcher(s[i]);

            assertTrue(matcher.find());

        }

    }
