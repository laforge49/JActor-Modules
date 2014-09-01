package org.agilewiki.jactor2.common.service;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CPlant;
import org.xeustechnologies.jcl.JarClassLoader;

public class ClassLoaderServiceTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            ClassLoaderService.register();
            ClassLoaderService cls = ClassLoaderService.getClassLoaderService();
            JarClassLoader cl = cls.jcl;
            String smileName = Smile.class.getName();
            Class c = cl.loadClass(smileName);
            c.newInstance();
        } finally {
            CPlant.close();
        }
    }
}
