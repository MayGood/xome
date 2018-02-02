package com.xxhx.xome.config;

/**
 * Created by xxhx on 2017/7/14.
 */

public enum Identity {
    Visitor(0), Manager(1), X(Integer.MAX_VALUE);

    private int securityLevel;

    private Identity(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }
}
