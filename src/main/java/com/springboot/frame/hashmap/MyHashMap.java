package com.springboot.frame.hashmap;

import org.apache.lucene.analysis.CharArrayMap;
import org.apache.lucene.search.suggest.tst.TernaryTreeNode;

import java.io.Serializable;
import java.util.*;

/**
 * 重写HashMap
 *
 * @Author: Administrator
 * @Date: 2021/3/13 17:29
 */
public class MyHashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = null;
        return es;
    }

    /**
     * 无参构造函数
     */
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTORY;
    }


    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTORY);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load Factory: " + loadFactor);
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * 通过右移的方式，将输入的capacity 调整至最近一个比他大的二次幂的值
     *
     * @param cap capacity
     * @return 返回调整的table size
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n > MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * Basic hash bin node, used for most entries.
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K, V> implements Map.Entry<K, V> {

        /**
         * Node 节点的hash 值
         */
        final int hash;

        /**
         * Map 的key 值
         */
        final K key;

        /**
         * Map 的value 值
         */
        V value;

        /**
         * Node 的下一节点
         */
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * The maximum capacity
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor
     */
    static final float DEFAULT_LOAD_FACTORY = 0.75f;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    /**
     * Holds cached entrySet().
     */
    transient Set<Map.Entry<K, V>> entrySet;

    /**
     * The table, initialized on first use, and resized as necessary.
     * When allocated, length is always a power of two.
     */
    transient Node<K, V>[] table;

    /**
     * Initializes or doubles table size.
     *
     * @return
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        // 通过oldCap 判断是否是已经初始化过HashMap
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        // 通过oldThr 判断HashMap是通过哪个构造函数进行初始化的
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 已经初始化过的情况下，若HashMap的容量大于Integer.MAX_VALUE的情况下
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                // 已经初始化过的情况下，若数组容量扩大一倍之后，阈值也扩大一倍
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            // oldCap = 0 && oldThr > 0的情况，说明是调用了HashMap的带参的初始化函数
            newCap = oldThr;
            // HashMap的初始化函数，是将capacity 赋值给threshold， 故而此处将threshold赋值给newCap
        } else {
            // oldCap = 0 && oldThr = 0 的情况，说明是调用了HashMap的无参初始化函数
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTORY);
        }
        if (newThr == 0) {
            float ft = newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }

        // 将初始化好的阈值，复制给threshold
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];

        table = newTab;
        // 当已经初始化好了HashMap 之后，调整HashMap上的元素
        if (oldTab != null) {
            for (int i = 0; i < oldCap; i++) {
                Node<K, V> e;
                // 当oldTable上有值的情况下
                if ((e = oldTab[i]) != null) {
                    oldTab[i] = null;
                    // 当table上的值的next为null时，意味着当前位置没有冲突;则重新计算位置。
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else if (e instanceof Object) {
                        // 当table 上的值为tree bin时，意味着table的此处产生哈希冲突，链表长度超过8
                    } else {
                        // 当table此处的类型为链表类型时: loHead存储table的第一位，loTail递归设置当前节点的next值
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        // 循环链表的节点
                        do {
                            // 与下面的判断条件呼应
                            next = e.next;
                            // 当为0时，索引位置不变；当为1时，当前索引位置 + oldCap
                            if ((e.hash & oldCap) == 0) {
                                // 首次，loTail 是为null的
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    // 将loTail 的next置为为loTail
                                    loTail.next = e;
                                }
                                // 将loTail 置为为当前元素
                                loTail = e;
                            } else {
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);

                        if (loTail != null) {
                            loTail.next = null;
                            newTab[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiHead.next = null;
                            newTab[i] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 获取HashMap 的值
     *
     * @param key
     * @return
     */
    public V get(Object key) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.getValue();
    }

    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        // 全局变量赋值给局部变量，并且计算hash表的位置上有值的情况下
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
            // 从第一个结点开始判断：hash 相等，key 相等的情况下
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
            // 第一个节点之后，还有值的情况
            if ((e = first.next) != null) {
                if (first instanceof Object) {
                    // 当这里时TreeNode的类型时
                } else {
                    // 循环遍历链表，直到找到为止
                    do {
                        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                            return e;
                        }
                    } while ((e = e.next) != null);
                }
            }
        }
        return null;
    }

}
