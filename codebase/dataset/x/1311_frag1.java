        Collection<Node> getSuiteChildren(Node elem) {

            ArrayList<Node> suites = new ArrayList<Node>();

            NodeList children = elem.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {

                Node child = children.item(i);

                if (child.getNodeName().equals(DescriptionGenerator.TAG_SUITE)) {

                    suites.add(child);

                }

            }

            return suites;

        }
