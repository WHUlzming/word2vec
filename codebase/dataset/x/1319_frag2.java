    public static String convertXMLToString(Document doc) throws XMLHelperException {

        try {

            Format format = Format.getPrettyFormat();

            format.setTextMode(Format.TextMode.NORMALIZE);

            XMLOutputter xmlOutputter = new XMLOutputter(format);

            return xmlOutputter.outputString(doc);

        } catch (Exception ioe) {

            ioe.printStackTrace();

            throw new XMLHelperException("Unable to write to the string", ioe);

        }

    }
