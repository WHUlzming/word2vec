    private String checkForWebkbErrors(String doc) {

        String extractedErrorStr = doc;

        int startPatternInd = doc.indexOf(webkbErorrStartPattern);

        int endPatternInd = doc.indexOf(webkbErrorEndPattern);

        if (endPatternInd != -1) {

            extractedErrorStr = extractedErrorStr.substring(0, endPatternInd);

        }

        if (startPatternInd != -1) {

            extractedErrorStr = extractedErrorStr.substring(webkbErorrStartPattern.length());

        }

        return extractedErrorStr;

    }
