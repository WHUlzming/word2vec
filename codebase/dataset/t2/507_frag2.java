    private org.omg.CORBA.portable.OutputStream _OB_att_get_priority_filter(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CosNotifyFilter.MappingFilter _ob_r = priority_filter();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        org.omg.CosNotifyFilter.MappingFilterHelper.write(out, _ob_r);

        return out;

    }
