    public void saveAttributeXML(Attributes a) throws IOException {

        try {

            Runtime r = Runtime.getRuntime();

            Document doc = new DocumentImpl();

            Element root_element = doc.createElement("ROOT");

            root_element.setAttribute("file_class_name", a.getClass().getName());

            root_element.setAttribute("file_version", Integer.toString(this.getVersion()));

            root_element.setAttribute("time_key_index", Integer.toString(a.getTimeKeyIndex()));

            root_element.setAttribute("name", a.getName());

            doc.appendChild(root_element);

            if (a.getPrecisionExtent() != null) {

                a.getPrecisionExtent().toXML(doc);

            }

            if (a.getUnitExtent() != null) {

                a.getUnitExtent().toXML(doc);

            }

            if (a.getNoteExtent() != null) {

                a.getNoteExtent().toXML(doc);

            }

            if (a.getLinkExtent() != null) {

                a.getLinkExtent().toXML(doc);

            }

            if (a.getSourceExtent() != null) {

                a.getSourceExtent().toXML(doc);

            }

            if (a.getGraphExtent() != null) {

                a.getGraphExtent().toXML(doc);

            }

            if (a.getDataLegendExtent() != null) {

                a.getDataLegendExtent().toXML(doc);

            }

            if (a.getReferenceExtent() != null) {

                a.getReferenceExtent().toXML(doc);

            }

            r.gc();

            saveXMLDocument(fileResourceAttributes.getAbsoluteFilename(), doc);

            r.gc();

        } catch (Exception e) {

            e.printStackTrace(System.out);

            throw new IOException("Error :: AttributesExternalizerMultiSource :: saveAttributeXML :: " + e.getClass() + " " + e.getMessage());

        }

    }
