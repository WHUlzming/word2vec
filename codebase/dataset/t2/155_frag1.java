        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(showCountryList(urlData));

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {
