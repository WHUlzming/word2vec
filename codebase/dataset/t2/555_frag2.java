    private org.omg.CORBA.portable.OutputStream _OB_att_get_lookup_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Lookup _ob_r = lookup_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        LookupHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_att_get_proxy_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Proxy _ob_r = proxy_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        ProxyHelper.write(out, _ob_r);

        return out;

    }



    private org.omg.CORBA.portable.OutputStream _OB_att_get_register_if(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        Register _ob_r = register_if();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        RegisterHelper.write(out, _ob_r);

        return out;

    }

}
