        if ("id".equals(pColumn)) return new Long(vo.getId());

        if ("tstamp".equals(pColumn)) return vo.getTstamp();

        if ("listType".equals(pColumn)) return new Long(vo.getListType());
