package org.prikic.yafr.loaders;

public enum Loaders {

    GET_ALL_RSS_CHANNELS;

    public static Loaders getLoaders(int ordinal) {
        return Loaders.values()[ordinal];
    }
}
