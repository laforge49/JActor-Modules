package org.agilewiki.jactor2.modules.properties;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;

/**
 * A source of immutable properties.
 */
public interface PropertiesSource extends ImmutableSource<ISMap<String>> {
}
