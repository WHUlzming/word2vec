    public RESTProxyCreator(JClassType resourceInterface, JClassType responseReaderInterface, JPackage restInterfacePackage, JPackage jsoPackage) {

        this.resourceInterface = resourceInterface;

        this.responseReaderInterface = responseReaderInterface;

        this.restInterfacePackage = restInterfacePackage;

        this.jsoPackage = jsoPackage;

    }
