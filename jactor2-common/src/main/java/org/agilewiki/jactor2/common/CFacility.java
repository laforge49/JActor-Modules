package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.common.services.ClassLoaderService;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.xeustechnologies.jcl.JarClassLoader;

public class CFacility extends Facility {
    public CFacility(String _name) throws Exception {
        super(_name);
    }

    public CFacility(String _name, Facility _parentReactor) throws Exception {
        super(_name, _parentReactor);
    }

    public CFacility(String _name, int _initialOutboxSize, int _initialLocalQueueSize) throws Exception {
        super(_name, _initialOutboxSize, _initialLocalQueueSize);
    }

    public CFacility(String _name, Void _parentReactor, int _initialOutboxSize, int _initialLocalQueueSize) throws Exception {
        super(_name, _parentReactor, _initialOutboxSize, _initialLocalQueueSize);
    }

    public JarClassLoader getJCL() throws Exception {
        return ClassLoaderService.getClassLoaderService(this).jcl;
    }
}
