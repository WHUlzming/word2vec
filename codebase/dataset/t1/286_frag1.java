    private ModuleInfoLine[] getActiveModulesInformations(Integer userId) throws Exception {

        Logs.logMethodName();

        Call call = (Call) SoapTestCase.service.createCall();

        call.setTargetEndpointAddress(getEndPoint() + "Module");

        call.setOperationName("getActiveModulesInformations");

        call.setOperationStyle("rpc");

        call.addParameter("userId", XMLType.XSD_INT, ParameterMode.IN);

        QName qn = new QName("urn:com.entelience.soap.soapModule", "ModuleInfoLine");

        call.registerTypeMapping(com.entelience.objects.module.ModuleInfoLine.class, qn, new BeanSerializerFactory(com.entelience.objects.module.ModuleInfoLine.class, qn), new BeanDeserializerFactory(com.entelience.objects.module.ModuleInfoLine.class, qn));

        call.setReturnType(XMLType.SOAP_ARRAY);

        return (ModuleInfoLine[]) call.invoke(new Object[] { userId });

    }
