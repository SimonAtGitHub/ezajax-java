package me.danwi.eazyajax.container.core;

import me.danwi.eazyajax.channel.core.PrivilegeBase;

import java.util.*;

/**
 * Created by Demon on 2/5/14.
 */
public class PrivilegeContainer {
    public static Hashtable<String,PrivilegeBase> privilegeInstances = new Hashtable<String, PrivilegeBase>();

    public static void initPrivilege(String privilegeName,String clazzName) throws Exception {
        Class<PrivilegeBase> clazz = (Class<PrivilegeBase>) Class.forName(clazzName);
        PrivilegeContainer.privilegeInstances.put(privilegeName,clazz.newInstance());
    }

    public static PrivilegeBase getPrivilegeInstance(String privilageName)
    {
        return privilegeInstances.get(privilageName);
    }
}
