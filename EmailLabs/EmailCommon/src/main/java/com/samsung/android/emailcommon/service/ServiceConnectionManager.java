package com.samsung.android.emailcommon.service;

import android.content.Context;
import android.os.Handler;
import android.util.Pair;

import com.samsung.android.emailcommon.utility.EmailLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServiceConnectionManager {

    private static final String LOG_TAG = "ServiceConnectionManager";

    private static ServiceConnectionManager mInstance = null;

    private Services mBoundServices = new Services();
    private Context mContext = null;

    protected ServiceConnectionManager(Context context)     {
        mContext = context.getApplicationContext(); 
    }


    protected static class ServiceConnections {
        private HashMap<String, ServiceConnectionAdapter> mConnections = new HashMap<String, ServiceConnectionAdapter>();

        protected void put(String connectionName, ServiceConnectionAdapter connectionObject) {
            if(mConnections.containsKey(connectionName)) {
                remove(connectionName);
            }
            mConnections.put(connectionName, connectionObject);
        }

        protected boolean remove(String connectionName) {
            if(mConnections.containsKey(connectionName)) {
                ServiceConnectionAdapter connection = mConnections.remove(connectionName);
                try {
                    connection.unbind();
                } catch (Exception e) {
                    EmailLog.d(LOG_TAG, "Exception in remove, ignore it: " + e.toString());
                }
                return true;
            }
            return false;
        }

        protected void removeAll() {
            Iterator<ServiceConnectionAdapter> it = mConnections.values().iterator();
            while(it.hasNext()) {
                try {
                    it.next().unbind();
                } catch (Exception e) {
                    EmailLog.d(LOG_TAG, "Exception in removeAll, ignore it: " + e.toString());
                }
                it.remove();
            }
        }

        protected boolean isEmpty() {
            return mConnections.isEmpty();
        }

    }


    protected static class Services {
        private HashMap<String, Pair<ServiceConnections, String>> mServices = 
                new HashMap<String, Pair<ServiceConnections, String>>();

        public ServiceConnections getConnections(String serviceClass) {
            ServiceConnections connections = null;
            if(mServices.containsKey(serviceClass)) {
                connections = mServices.get(serviceClass).first;
                return connections;
            }
            connections = new ServiceConnections();
            mServices.put(serviceClass, new Pair<ServiceConnections, String>(connections, serviceClass));
            return connections;
        }

        public void remove(String serviceClass, String commandName) {
            if(mServices.containsKey(serviceClass)) {
                ServiceConnections connections = mServices.get(serviceClass).first;
                connections.remove(commandName);
                if(connections.isEmpty()) {
                    mServices.remove(serviceClass);
                }
            }
        }

        public void removeAllConnections(String serviceClass) {
            EmailLog.d(LOG_TAG, "Before remove all: " + mServices.keySet().toString() + "&" + serviceClass);
            if(mServices.containsKey(serviceClass)) {
                ServiceConnections connections = mServices.get(serviceClass).first;
                connections.removeAll();
                if(connections.isEmpty()) {
                    mServices.remove(serviceClass);
                }
            }
            EmailLog.d(LOG_TAG, "After remove all: " + mServices.keySet().toString() + "&" + serviceClass);
        }

        public boolean hasClass(String serviceClass) {
            return mServices.containsKey(serviceClass);
        }

        protected ArrayList<String> getClasses() {
            ArrayList<String> list = new ArrayList<String>();
            Iterator<String> it = mServices.keySet().iterator();
            while(it.hasNext()) {
                list.add(mServices.get(it.next()).second);
            }
            return list;
        }
    }

    public static ServiceConnectionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServiceConnectionManager(context);
        }
        return mInstance;
    }


    public ServiceConnectionAdapter addBind(String connectionClass, String connectionPkg, Context context, Handler handler, OnServiceConnectedCommand command) {
        EmailLog.d(LOG_TAG, "addBind: " + command.getName());
        //Get or create connections bound services
        ServiceConnections connections = mBoundServices.getConnections(connectionClass);
        ServiceConnectionAdapter connection = new ServiceConnectionAdapter(context, handler, command);

        connection.bind(connectionClass, connectionPkg);
        connections.put(command.getName(), connection);
        return connection;
    }


    public void removeBound(String serviceClass, String commandName) {
        if(mBoundServices.hasClass(serviceClass)) {
            mBoundServices.remove(serviceClass, commandName);
        }
    }


    private void removeAllBound(String serviceClass) {
        if(mBoundServices.hasClass(serviceClass)) {
            mBoundServices.removeAllConnections(serviceClass);
        }
    }


    public void finishConnections() {
        for(String serviceClass : mBoundServices.getClasses()) {
            removeAllBound(serviceClass);
        }
    }

}
