    private org.omg.CORBA.portable.OutputStream _OB_op_obtain_notification_pull_consumer(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            org.omg.CosNotifyChannelAdmin.ClientType _ob_a0 = org.omg.CosNotifyChannelAdmin.ClientTypeHelper.read(in);

            org.omg.CORBA.IntHolder _ob_ah1 = new org.omg.CORBA.IntHolder();

            org.omg.CosNotifyChannelAdmin.ProxyConsumer _ob_r = obtain_notification_pull_consumer(_ob_a0, _ob_ah1);

            out = handler.createReply();

            org.omg.CosNotifyChannelAdmin.ProxyConsumerHelper.write(out, _ob_r);

            org.omg.CosNotifyChannelAdmin.ProxyIDHelper.write(out, _ob_ah1.value);

        } catch (org.omg.CosNotifyChannelAdmin.AdminLimitExceeded _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosNotifyChannelAdmin.AdminLimitExceededHelper.write(out, _ob_ex);

        }

        return out;

    }
