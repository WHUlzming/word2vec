    private org.omg.CORBA.portable.OutputStream _OB_op_obtain_typed_push_supplier(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            String _ob_a0 = org.omg.CosTypedEventChannelAdmin.KeyHelper.read(in);

            org.omg.CosEventChannelAdmin.ProxyPushSupplier _ob_r = obtain_typed_push_supplier(_ob_a0);

            out = handler.createReply();

            org.omg.CosEventChannelAdmin.ProxyPushSupplierHelper.write(out, _ob_r);

        } catch (org.omg.CosTypedEventChannelAdmin.NoSuchImplementation _ob_ex) {

            out = handler.createExceptionReply();

            org.omg.CosTypedEventChannelAdmin.NoSuchImplementationHelper.write(out, _ob_ex);

        }

        return out;

    }
