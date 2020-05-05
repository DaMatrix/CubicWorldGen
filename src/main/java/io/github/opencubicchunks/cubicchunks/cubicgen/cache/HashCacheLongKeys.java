/*
 *  This file is part of Cubic World Generation, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015-2020 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package io.github.opencubicchunks.cubicchunks.cubicgen.cache;

import io.github.opencubicchunks.cubicchunks.cubicgen.customcubic.builder.BiomeSource;
import it.unimi.dsi.fastutil.HashCommon;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.LongFunction;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HashCacheLongKeys<V> {

    private final V[] cache;
    private final long[] keys;
    private final LongFunction<V> source;

    @SuppressWarnings("unchecked")
    public HashCacheLongKeys(int size, LongFunction<V> source) {
        this.cache = (V[]) new Object[size];
        this.keys = new long[size];
        this.source = source;
    }

    public V get(long key) {
        int index = Math.floorMod(this.hash(key), this.cache.length);
        if (key != this.keys[index]) {
            this.keys[index] = key;
            return this.cache[index] = this.source.apply(key);
        }
        return this.cache[index];
    }

    protected int hash(long key) {
        return HashCommon.long2int(HashCommon.murmurHash3(key));
    }

    public static class Chunk<K> extends HashCacheLongKeys<K>   {
        public Chunk(int size, LongFunction<K> source) {
            super(size, source);
        }

        @Override
        protected int hash(long key) {
            return (int) (key >> 32L) * BiomeSource.CHUNKS_CACHE_RADIUS + (int) key;
        }
    }
}
