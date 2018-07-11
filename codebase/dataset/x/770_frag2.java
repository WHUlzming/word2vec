import javax.management.openmbean.*;

import java.io.IOException;

import java.lang.reflect.Constructor;

import java.util.Dictionary;

import java.util.HashMap;

import java.util.Map;



/**

 * @author dmytro.pishchukhin

 */

public class MonitorAdmin extends ServiceAbstractMBean<org.osgi.service.monitor.MonitorAdmin> implements MonitorAdminMBean, NotificationBroadcaster {



    private NotificationBroadcasterSupport nbs;



    private MBeanNotificationInfo[] notificationInfos;



    private ServiceRegistration handlerRegistration;



    public MonitorAdmin() throws NotCompliantMBeanException {

        super(MonitorAdminMBean.class);

        nbs = new NotificationBroadcasterSupport();

    }



    @Override

    public void init() {

        super.init();

        try {

            Class<?> handlerClass = Class.forName("org.knowhowlab.osgi.jmx.beans.service.monitor.MonitorAdminEventHandler");

            Constructor<?> constructor = handlerClass.getConstructor(OsgiVisitor.class, LogVisitor.class, NotificationBroadcasterSupport.class, NotificationBroadcaster.class);
