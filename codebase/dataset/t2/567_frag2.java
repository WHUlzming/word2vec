    public static void main(String rArgs[]) {

        GenericIteratorWriter codeWriter = new GenericIteratorWriter();

        try {

            codeWriter.writer__initialize();

            codeWriter.writer__handleArgs(rArgs);

            codeWriter.writer__write();

        } catch (Exception e) {

            codeWriter.writer__handleException(writer__UITEXT_ExceptionIn + writer__UITEXT_Method + writer__UITEXT_Main, e);

        }

    }