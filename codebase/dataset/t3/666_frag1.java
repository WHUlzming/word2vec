    public ThdProxyAcceptCb(TCPServerChannel svrChannel, ConnectionListener cb, SpecialRoutingExecutor svc2, BufferFactory bufFactory) {

        this.svrChannel = svrChannel;

        this.cb = cb;

        this.svc = svc2;

        this.bufFactory = bufFactory;

    }
