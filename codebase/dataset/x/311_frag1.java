        ByteArrayOutputStream buff = new ByteArrayOutputStream();

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        Source source = new DOMSource(doc);

        Result result = new StreamResult(buff);

        transformer.transform(source, result);

        if (output != null) {

            output.append(buff.toString());

        } else {

            FileWriter out = new FileWriter(this.getProperty("path.data") + File.separator + "MatchList.xml");

            out.write(buff.toString());

            out.close();

            System.out.println("MatchList.xml saved.");

        }

    }



    public HashMap<String, EpgMatchList> getMatchLists() {

        return epgMatchLists;

    }



    private void loadEpgAutoList() {

        epgMatchList = new Vector<EpgMatch>();

        try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new File(this.getProperty("path.data") + File.separator + "EpgAutoAdd.xml"));

            NodeList items = doc.getElementsByTagName("item");

            for (int x = 0; x < items.getLength(); x++) {
