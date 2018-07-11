    protected void setURL(URL arg0, String arg1, String arg2, int arg3, String arg4, String arg5, String arg6, String arg7, String arg8) {

        URLStreamHandler handler = factory.findAuthorizedURLStreamHandler(protocol);

        if (handler != null) {

            try {

                handlerField.set(arg0, handler);

                setURLMethod.invoke(handler, new Object[] { arg0, arg1, arg2, new Integer(arg3), arg4, arg5, arg6, arg7, arg8 });

                handlerField.set(arg0, this);

                return;

            } catch (Exception e) {

                factory.adaptor.getFrameworkLog().log(new FrameworkLogEntry(MultiplexingURLStreamHandler.class.getName(), "setURL", FrameworkLogEntry.ERROR, e, null));

                throw new RuntimeException(e.getMessage());

            }

        }

        throw new IllegalStateException();

    }
