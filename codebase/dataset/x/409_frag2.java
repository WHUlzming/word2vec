    private org.omg.CORBA.portable.OutputStream _OB_att_get_link_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Link _ob_r = link_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        LinkHelper.write(out, _ob_r);

        return out;

    }
