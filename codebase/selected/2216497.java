package org.apache.axis2.clustering.tribes;

import org.apache.axis2.clustering.ClusteringFault;
import org.apache.axis2.clustering.Member;
import org.apache.axis2.clustering.MembershipScheme;
import org.apache.axis2.clustering.MembershipListener;
import org.apache.axis2.clustering.control.wka.JoinGroupCommand;
import org.apache.axis2.clustering.control.wka.MemberListCommand;
import org.apache.axis2.clustering.control.wka.RpcMembershipRequestHandler;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.util.Utils;
import org.apache.catalina.tribes.Channel;
import org.apache.catalina.tribes.ManagedChannel;
import org.apache.catalina.tribes.group.Response;
import org.apache.catalina.tribes.group.RpcChannel;
import org.apache.catalina.tribes.group.interceptors.OrderInterceptor;
import org.apache.catalina.tribes.group.interceptors.StaticMembershipInterceptor;
import org.apache.catalina.tribes.group.interceptors.TcpFailureDetector;
import org.apache.catalina.tribes.group.interceptors.TcpPingInterceptor;
import org.apache.catalina.tribes.membership.StaticMember;
import org.apache.catalina.tribes.transport.ReceiverBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the WKA(well-known address) based membership scheme. In this scheme,
 * membership is discovered using a few well-known members (who run at well-known IP addresses)
 */
public class WkaBasedMembershipScheme implements MembershipScheme {

    private static final Log log = LogFactory.getLog(WkaBasedMembershipScheme.class);

    /**
     * The Tribes channel
     */
    private ManagedChannel channel;

    private MembershipManager primaryMembershipManager;

    private List<MembershipManager> applicationDomainMembershipManagers;

    private StaticMembershipInterceptor staticMembershipInterceptor;

    private Map<String, Parameter> parameters;

    /**
     * The loadBalancerDomain to which the members belong to
     */
    private byte[] localDomain;

    /**
     * The static(well-known) members
     */
    private List<Member> members;

    /**
     * The mode in which this member operates such as "loadBalance" or "application"
     */
    private OperationMode mode;

    private boolean atmostOnceMessageSemantics;

    private boolean preserverMsgOrder;

    public WkaBasedMembershipScheme(ManagedChannel channel, OperationMode mode, List<MembershipManager> applicationDomainMembershipManagers, MembershipManager primaryMembershipManager, Map<String, Parameter> parameters, byte[] domain, List<Member> members, boolean atmostOnceMessageSemantics, boolean preserverMsgOrder) {
        this.channel = channel;
        this.mode = mode;
        this.applicationDomainMembershipManagers = applicationDomainMembershipManagers;
        this.primaryMembershipManager = primaryMembershipManager;
        this.parameters = parameters;
        this.localDomain = domain;
        this.members = members;
        this.atmostOnceMessageSemantics = atmostOnceMessageSemantics;
        this.preserverMsgOrder = preserverMsgOrder;
    }

    /**
     * Configure the membership related to the WKA based scheme
     *
     * @throws org.apache.axis2.clustering.ClusteringFault
     *          If an error occurs while configuring this scheme
     */
    public void init() throws ClusteringFault {
        addInterceptors();
        configureStaticMembership();
    }

    private void configureStaticMembership() throws ClusteringFault {
        channel.setMembershipService(new WkaMembershipService(primaryMembershipManager));
        StaticMember localMember = new StaticMember();
        primaryMembershipManager.setLocalMember(localMember);
        ReceiverBase receiver = (ReceiverBase) channel.getChannelReceiver();
        Parameter localHost = getParameter(TribesConstants.LOCAL_MEMBER_HOST);
        String host;
        if (localHost != null) {
            host = ((String) localHost.getValue()).trim();
        } else {
            try {
                try {
                    host = Utils.getIpAddress();
                } catch (SocketException e) {
                    String msg = "Could not get local IP address";
                    log.error(msg, e);
                    throw new ClusteringFault(msg, e);
                }
            } catch (Exception e) {
                String msg = "Could not get the localhost name";
                log.error(msg, e);
                throw new ClusteringFault(msg, e);
            }
        }
        receiver.setAddress(host);
        try {
            localMember.setHostname(host);
        } catch (IOException e) {
            String msg = "Could not set the local member's name";
            log.error(msg, e);
            throw new ClusteringFault(msg, e);
        }
        Parameter localPort = getParameter(TribesConstants.LOCAL_MEMBER_PORT);
        int port;
        try {
            if (localPort != null) {
                port = Integer.parseInt(((String) localPort.getValue()).trim());
                port = getLocalPort(new ServerSocket(), localMember.getHostname(), port, 4000, 100);
            } else {
                port = getLocalPort(new ServerSocket(), localMember.getHostname(), -1, 4000, 100);
            }
        } catch (IOException e) {
            String msg = "Could not allocate the specified port or a port in the range 4000-4100 " + "for local host " + localMember.getHostname() + ". Check whether the IP address specified or inferred for the local " + "member is correct.";
            log.error(msg, e);
            throw new ClusteringFault(msg, e);
        }
        byte[] payload = "ping".getBytes();
        localMember.setPayload(payload);
        receiver.setPort(port);
        localMember.setPort(port);
        localMember.setDomain(localDomain);
        staticMembershipInterceptor.setLocalMember(localMember);
        for (Member member : members) {
            StaticMember tribesMember;
            try {
                tribesMember = new StaticMember(member.getHostName(), member.getPort(), 0, payload);
            } catch (IOException e) {
                String msg = "Could not add static member " + member.getHostName() + ":" + member.getPort();
                log.error(msg, e);
                throw new ClusteringFault(msg, e);
            }
            if (!(Arrays.equals(localMember.getHost(), tribesMember.getHost()) && localMember.getPort() == tribesMember.getPort())) {
                tribesMember.setDomain(localDomain);
                staticMembershipInterceptor.addStaticMember(tribesMember);
                primaryMembershipManager.addWellKnownMember(tribesMember);
                if (canConnect(member)) {
                    primaryMembershipManager.memberAdded(tribesMember);
                    log.info("Added static member " + TribesUtil.getName(tribesMember));
                } else {
                    log.info("Could not connect to member " + TribesUtil.getName(tribesMember));
                }
            }
        }
    }

    /**
     * Before adding a static member, we will try to verify whether we can connect to it
     *
     * @param member The member whose connectvity needs to be verified
     * @return true, if the member can be contacted; false, otherwise.
     */
    private boolean canConnect(org.apache.axis2.clustering.Member member) {
        for (int retries = 5; retries > 0; retries--) {
            try {
                InetAddress addr = InetAddress.getByName(member.getHostName());
                SocketAddress sockaddr = new InetSocketAddress(addr, member.getPort());
                new Socket().connect(sockaddr, 500);
                return true;
            } catch (IOException e) {
                String msg = e.getMessage();
                if (msg.indexOf("Connection refused") == -1 && msg.indexOf("connect timed out") == -1) {
                    log.error("Cannot connect to member " + member.getHostName() + ":" + member.getPort(), e);
                }
            }
        }
        return false;
    }

    protected int getLocalPort(ServerSocket socket, String hostname, int preferredPort, int portstart, int retries) throws IOException {
        if (preferredPort != -1) {
            try {
                return getLocalPort(socket, hostname, preferredPort);
            } catch (IOException ignored) {
            }
        }
        InetSocketAddress addr = null;
        if (retries > 0) {
            try {
                return getLocalPort(socket, hostname, portstart);
            } catch (IOException x) {
                retries--;
                if (retries <= 0) {
                    log.error("Unable to bind server socket to:" + addr + " throwing error.");
                    throw x;
                }
                portstart++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
                }
                getLocalPort(socket, hostname, portstart, retries, -1);
            }
        }
        return portstart;
    }

    private int getLocalPort(ServerSocket socket, String hostname, int port) throws IOException {
        InetSocketAddress addr;
        addr = new InetSocketAddress(hostname, port);
        socket.bind(addr);
        log.info("Receiver Server Socket bound to:" + addr);
        socket.setSoTimeout(5);
        socket.close();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
            ignored.printStackTrace();
        }
        return port;
    }

    /**
     * Add ChannelInterceptors. The order of the interceptors that are added will depend on the
     * membership management scheme
     */
    private void addInterceptors() {
        if (log.isDebugEnabled()) {
            log.debug("Adding Interceptors...");
        }
        TcpPingInterceptor tcpPingInterceptor = new TcpPingInterceptor();
        tcpPingInterceptor.setInterval(100);
        channel.addInterceptor(tcpPingInterceptor);
        if (log.isDebugEnabled()) {
            log.debug("Added TCP Ping Interceptor");
        }
        TcpFailureDetector tcpFailureDetector = new TcpFailureDetector();
        tcpFailureDetector.setReadTestTimeout(120000);
        tcpFailureDetector.setConnectTimeout(60000);
        channel.addInterceptor(tcpFailureDetector);
        if (log.isDebugEnabled()) {
            log.debug("Added TCP Failure Detector");
        }
        staticMembershipInterceptor = new StaticMembershipInterceptor();
        staticMembershipInterceptor.setLocalMember(primaryMembershipManager.getLocalMember());
        primaryMembershipManager.setStaticMembershipInterceptor(staticMembershipInterceptor);
        channel.addInterceptor(staticMembershipInterceptor);
        if (log.isDebugEnabled()) {
            log.debug("Added Static Membership Interceptor");
        }
        channel.getMembershipService().setDomain(localDomain);
        mode.addInterceptors(channel);
        if (atmostOnceMessageSemantics) {
            AtMostOnceInterceptor atMostOnceInterceptor = new AtMostOnceInterceptor();
            atMostOnceInterceptor.setOptionFlag(TribesConstants.AT_MOST_ONCE_OPTION);
            channel.addInterceptor(atMostOnceInterceptor);
            if (log.isDebugEnabled()) {
                log.debug("Added At-most-once Interceptor");
            }
        }
        if (preserverMsgOrder) {
            OrderInterceptor orderInterceptor = new OrderInterceptor();
            orderInterceptor.setOptionFlag(TribesConstants.MSG_ORDER_OPTION);
            channel.addInterceptor(orderInterceptor);
            if (log.isDebugEnabled()) {
                log.debug("Added Message Order Interceptor");
            }
        }
    }

    /**
     * JOIN the group and get the member list
     *
     * @throws ClusteringFault If an error occurs while joining the group
     */
    public void joinGroup() throws ClusteringFault {
        for (MembershipManager appDomainMembershipManager : applicationDomainMembershipManagers) {
            appDomainMembershipManager.setStaticMembershipInterceptor(staticMembershipInterceptor);
            String domain = new String(appDomainMembershipManager.getDomain());
            RpcChannel rpcMembershipChannel = new RpcChannel(TribesUtil.getRpcMembershipChannelId(appDomainMembershipManager.getDomain()), channel, new RpcMembershipRequestHandler(appDomainMembershipManager, this));
            appDomainMembershipManager.setRpcMembershipChannel(rpcMembershipChannel);
            if (log.isDebugEnabled()) {
                log.debug("Created RPC Membership Channel for application domain " + domain);
            }
        }
        RpcChannel rpcMembershipChannel = new RpcChannel(TribesUtil.getRpcMembershipChannelId(localDomain), channel, new RpcMembershipRequestHandler(primaryMembershipManager, this));
        if (log.isDebugEnabled()) {
            log.debug("Created primary membership channel " + new String(localDomain));
        }
        primaryMembershipManager.setRpcMembershipChannel(rpcMembershipChannel);
        if (primaryMembershipManager.getMembers().length > 0) {
            log.info("Sending JOIN message to WKA members...");
            org.apache.catalina.tribes.Member[] wkaMembers = primaryMembershipManager.getMembers();
            Response[] responses = null;
            do {
                try {
                    responses = rpcMembershipChannel.send(wkaMembers, new JoinGroupCommand(), RpcChannel.ALL_REPLY, Channel.SEND_OPTIONS_ASYNCHRONOUS | TribesConstants.MEMBERSHIP_MSG_OPTION, 10000);
                    if (responses.length == 0) {
                        try {
                            if (log.isDebugEnabled()) {
                                log.debug("No responses received");
                            }
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                        }
                    }
                } catch (Exception e) {
                    String msg = "Error occurred while trying to send JOIN request to WKA members";
                    log.error(msg, e);
                    wkaMembers = primaryMembershipManager.getMembers();
                    if (wkaMembers.length == 0) {
                        log.warn("There are no well-known members");
                        break;
                    }
                }
            } while (responses == null || responses.length == 0);
            for (Response response : responses) {
                MemberListCommand command = (MemberListCommand) response.getMessage();
                command.setMembershipManager(primaryMembershipManager);
                command.execute(null);
                if (!TribesUtil.areInSameDomain(response.getSource(), primaryMembershipManager.getLocalMember())) {
                    primaryMembershipManager.memberDisappeared(response.getSource());
                    if (log.isDebugEnabled()) {
                        log.debug("Removed member " + TribesUtil.getName(response.getSource()) + " since it does not belong to the local domain " + new String(primaryMembershipManager.getLocalMember().getDomain()));
                    }
                }
            }
        }
    }

    /**
     * When a JOIN message is received from some other member, it is notified using this method,
     * so that membership scheme specific processing can be carried out
     *
     * @param member The member who just joined
     */
    public void processJoin(org.apache.catalina.tribes.Member member) {
        mode.notifyMemberJoin(member);
    }

    public Parameter getParameter(String name) {
        return parameters.get(name);
    }
}
