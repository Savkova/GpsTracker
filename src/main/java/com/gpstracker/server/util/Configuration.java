package com.gpstracker.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Properties;

import com.gpstracker.server.util.Constants.Loggers;

public class Configuration extends Properties {
    private String server_name;
    private String server_ip;
    private int server_port;
    private String database_url;
    private String database_user;
    private String database_password;

    Configuration(String filename) {
        readPropertiesFromFile(filename);
        initPropertiesValues();
        Loggers.SERVER_LOGGER.info("Configuration properties downloaded from file '" + filename + "'");
    }

    private void readPropertiesFromFile(String fileName) {
        FileInputStream in = null;
        try {
            Loggers.SERVER_LOGGER.info("Reading file '" + fileName + "' ...");
            in = new FileInputStream(fileName);
            this.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            Loggers.SERVER_LOGGER.error("File '" + fileName + "' not found or cannot be opened.");
            System.exit(-1);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void initPropertiesValues() {
        try {
            server_port = Integer.parseInt(this.getProperty(PropertyKeys.server_port.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Loggers.SERVER_LOGGER.error("Invalid format of parameter '" + PropertyKeys.server_port + "'");
            System.exit(-1);
        }
        server_name = this.getProperty(PropertyKeys.server_name.toString());
        server_ip = this.getProperty(PropertyKeys.server_ip.toString());
        database_url = this.getProperty(PropertyKeys.database_url.toString());
        database_user = this.getProperty(PropertyKeys.database_user.toString());
        database_password = this.getProperty(PropertyKeys.database_password.toString());

    }

    @Override
    public String getProperty(final String key) {
        final String property = super.getProperty(key);
        if (property == null) {
            Loggers.SERVER_LOGGER.error("Missing value for key '" + key + "'");
            throw new InvalidParameterException(MessageFormat.format("Missing value for key {0}", key));
        }
        return property;
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        final String property = super.getProperty(key, defaultValue);
        if (property == null) {
            Loggers.SERVER_LOGGER.error("Missing value for key '" + key + "'");
            throw new InvalidParameterException(MessageFormat.format("Missing value for key {0}", key));
        }
        return property;
    }

    public String getServer_name() {
        return server_name;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public int getServer_port() {
        return server_port;
    }

    public String getDatabase_url() {
        return database_url;
    }

    public String getDatabase_user() {
        return database_user;
    }

    public String getDatabase_password() {
        return database_password;
    }

    public enum PropertyKeys {
        server_name,
        server_port,
        server_ip,
        database_url,
        database_user,
        database_password
    }
}
