    private DasGroup parseGroup(XmlPullParser xpp) throws DataSourceException, XmlPullParserException, IOException {

        String id = null, label = null, type = null;

        Map<URL, String> links = new HashMap<URL, String>();

        List<String> notes = new ArrayList<String>();

        List<DasTarget> targets = new ArrayList<DasTarget>();

        for (int i = 0; i < xpp.getAttributeCount(); i++) {

            final String attName = xpp.getAttributeName(i);

            if (attName.equals(ATT_id)) id = xpp.getAttributeValue(i); else if (attName.equals(ATT_type)) type = xpp.getAttributeValue(i); else if (attName.equals(ATT_label)) label = xpp.getAttributeValue(i);

        }

        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_GROUP.equals(xpp.getName()))) {

            if (xpp.getEventType() == XmlPullParser.START_TAG) {

                final String tagName = xpp.getName();

                if (ELEMENT_LINK.equals(tagName)) {

                    parseLink(xpp, links);

                } else if (ELEMENT_NOTE.equals(tagName)) {

                    xpp.next();

                    notes.add(xpp.getText());

                } else if (ELEMENT_TARGET.equals(tagName)) {

                    targets.add(parseTarget(xpp));

                }

            }

        }

        DasGroup group = new DasGroup(id, label, type, notes, links, targets);

        return group;

    }
