    public void test_validate_group_get_weeklyArtistCharts() {

        try {

            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            File schemaLocation = new File("tes.xsd");

            Schema schema = factory.newSchema(schemaLocation);

            Validator validator = schema.newValidator();

            URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=group.getweeklyartistchart&group=mnml&api_key=b25b959554ed76058ac220b7b2e0a026");

            InputStream inputStream = url.openStream();

            Source source = new StreamSource(inputStream);

            validator.validate(source);

        } catch (IOException ex) {

            Logger.getLogger(GetWeeklyartistchart_Test.class.getName()).log(Level.SEVERE, null, ex);

            assertFalse("File not found", true);

        } catch (SAXException ex) {

            Logger.getLogger(GetWeeklyartistchart_Test.class.getName()).log(Level.SEVERE, null, ex);

            assertFalse("Schema did not validate", true);

        }

        assertTrue(true);

    }
