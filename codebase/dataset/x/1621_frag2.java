    private org.omg.CORBA.portable.OutputStream _OB_op_get_supplieradmin(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {

        org.omg.CORBA.portable.OutputStream out = null;

        try {

            int _ob_a0 = AdminIDHelper.read(in);

            SupplierAdmin _ob_r = get_supplieradmin(_ob_a0);

            out = handler.createReply();

            SupplierAdminHelper.write(out, _ob_r);

        } catch (AdminNotFound _ob_ex) {

            out = handler.createExceptionReply();

            AdminNotFoundHelper.write(out, _ob_ex);

        }

        return out;

    }
