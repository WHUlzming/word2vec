    public static void exampleParam(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException {

        TransformerFactory tfactory = TransformerFactory.newInstance();

        Templates templates = tfactory.newTemplates(new StreamSource(xslID));

        Transformer transformer1 = templates.newTransformer();

        Transformer transformer2 = templates.newTransformer();

        transformer1.setParameter("a-param", "hello to you!");

        transformer1.transform(new StreamSource(sourceID), new StreamResult(new OutputStreamWriter(System.out)));

        System.out.println("\n=========");

        transformer2.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer2.transform(new StreamSource(sourceID), new StreamResult(new OutputStreamWriter(System.out)));

    }
