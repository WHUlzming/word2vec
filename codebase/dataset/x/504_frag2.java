        if ("remarks".equals(pColumn)) return vo.getRemarks();

        if ("price".equals(pColumn)) return vo.getPrice();

        if ("discount".equals(pColumn)) return vo.getDiscount();

        if ("discountPercent".equals(pColumn)) return new Double(vo.getDiscountPercent());
