package org.agilewiki.jactor2.modules.filters;

/**
 * Used when all content is of interest.
 */
public class NullFilter<CONTENT> implements Filter<CONTENT> {
    @Override
    public boolean match(final Object _content) {
        return true;
    }
}
