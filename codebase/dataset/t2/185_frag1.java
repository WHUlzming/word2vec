        try {

            TInfoLevel_QueryMap query = new TInfoLevel_QueryMap();

            List ls = query.findType(sf.getInfo_type());

            request.setAttribute("infoType", ls);

        } catch (HibernateException e) {

            logger.error("doEdit(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);

            e.printStackTrace();

        }

        if (null != review && "client".equals(from.trim())) {
