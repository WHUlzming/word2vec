    public String toString() {

        if (m_classifiers == null) {

            return "ND: No model built yet.";

        }

        StringBuffer text = new StringBuffer();

        text.append("ND\n\n");

        m_ndtree.toString(text, new int[1], 0);

        return text.toString();

    }
