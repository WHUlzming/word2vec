        if ("exchangeRate".equals(pColumn)) return new Double(vo.getExchangeRate());

        if ("creationDate".equals(pColumn)) return vo.getCreationDate();

        if ("dueDate".equals(pColumn)) return vo.getDueDate();
