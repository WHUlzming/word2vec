        String order_date;

        String paymentCode;

        String paymentName;

        String order_total;

        Double order_totalDouble = 0.0;

        String shipping_vatpercent;

        String shipping_vatname;

        String shipping_name;

        String shipping_gross;

        String commentDate;

        String comment;

        String commentText;

        int documentId;

        boolean noVat = true;

        String noVatName = "";

        NamedNodeMap attributes = orderNode.getAttributes();

        order_id = getAttributeAsString(attributes, "id");
