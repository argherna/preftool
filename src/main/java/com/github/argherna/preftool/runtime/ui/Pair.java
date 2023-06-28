package com.github.argherna.preftool.runtime.ui;

import java.util.Objects;

/**
 * Generic holder class.
 */
class Pair<K, V> {

    private final K k;

    private final V v;

    /**
     * Construct a new Pair.
     *
     * @param k the key for this Pair.
     * @param v the value for this Pair.
     */
    Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    /**
     *
     * @return the key of this Pair.
     */
    K getK() {
        return k;
    }

    /**
     *
     * @return the value of this Pair.
     */
    V getV() {
        return v;
    }

    @Override
    public String toString() {
        /*
         * These are rendered in JComboBoxes and the #toString method is called by it to
         * get what it should render. So render the key part of the Pair.
         * 
         * @see https://stackoverflow.com/a/5661669/37776
         */
        return String.valueOf(k);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("unchecked")
        Pair<K, V> other = (Pair<K, V>) obj;
        return Objects.equals(k, other.k) && Objects.equals(v, other.v);
    }
}
