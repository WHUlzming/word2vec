    public String toString() {

        String strMessage = printDigits(m_timestamp.getTime(), 12, true);

        for (int i = 0; i < m_data.size(); i++) {

            if (m_data.get(i) instanceof Character) {

                strMessage += " " + Character.getNumericValue(((Character) m_data.get(i)).charValue());

            } else {

                strMessage += " " + m_data.get(i);

            }

        }

        return strMessage;

    }
