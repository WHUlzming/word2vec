    private StringBuffer getStringValue(OMNode node, StringBuffer buffer) {

        if (isText(node)) {

            buffer.append(((OMText) node).getText());

        } else if (node instanceof OMElement) {

            Iterator children = ((OMElement) node).getChildren();

            while (children.hasNext()) {

                getStringValue((OMNode) children.next(), buffer);

            }

        }

        return buffer;

    }
