    private org.omg.CORBA.portable.OutputStream _OB_op_subscription_change(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            org.omg.CosNotification.EventType[] _ob_a0 = org.omg.CosNotification.EventTypeSeqHelper.read(in);

            org.omg.CosNotification.EventType[] _ob_a1 = org.omg.CosNotification.EventTypeSeqHelper.read(in);

            subscription_change(_ob_a0, _ob_a1);

            out = handler.createReply();

        } catch (org.omg.CosNotifyComm.InvalidEventType _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosNotifyComm.InvalidEventTypeHelper.write(out, _ob_ex);

        }

        return out;

    }
