            if ("id".equals(pColumn)) return new Long(fTotal.getId());

            if ("type".equals(pColumn)) return new Long(fTotal.getType());

            if ("principal".equals(pColumn)) return fTotal.getPrincipal();
