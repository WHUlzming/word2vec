    private org.omg.CORBA.portable.OutputStream _OB_att_get_MyChannel(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CosNotifyChannelAdmin.EventChannel _ob_r = MyChannel();

        org.omg.CORBA.portable.OutputStream out = handler.createReply();

        org.omg.CosNotifyChannelAdmin.EventChannelHelper.write(out, _ob_r);

        return out;

    }
