    private org.omg.CORBA.portable.OutputStream _OB_op_pull(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            org.omg.CORBA.Any _ob_r = pull();

            out = handler.createReply();

            out.write_any(_ob_r);

        } catch (org.omg.CosEventComm.Disconnected _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosEventComm.DisconnectedHelper.write(out, _ob_ex);

        }

        return out;

    }
