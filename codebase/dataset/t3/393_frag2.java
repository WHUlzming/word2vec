        ServerHandler srvHdl = new ServerHandler();

        IServer server = new Server(srvHdl);

        server.start();

        INonBlockingConnection clientCon = new NonBlockingConnection("localhost", server.getLocalPort());

        QAUtil.sleep(2000);

        INonBlockingConnection serverCon = srvHdl.getConection();

        serverCon.write(QAUtil.generateByteArray(4));
