import java.io.IOException;

import java.net.Socket;

import java.net.SocketAddress;

import java.net.SocketException;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;

import rpg.server.packets.Packet;



/**

 * rothens.tumblr.com

 * @author Rothens

 */

public class NetworkManager {



    public static final Object threadSyncObject = new Object();
