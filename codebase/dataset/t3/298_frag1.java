    private org.omg.CORBA.portable.OutputStream _OB_op_pull_structured_events(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            int _ob_a0 = in.read_long();

            org.omg.CosNotification.StructuredEvent[] _ob_r = pull_structured_events(_ob_a0);

            out = handler.createReply();

            org.omg.CosNotification.EventBatchHelper.write(out, _ob_r);

        } catch (org.omg.CosEventComm.Disconnected _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosEventComm.DisconnectedHelper.write(out, _ob_ex);

        }

        return out;

    }
