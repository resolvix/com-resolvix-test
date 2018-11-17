package com.resolvix.test.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

@Deprecated
public class EntityMatcherTest {

    public class LocalEntity {

        private byte b;

        private char unboxedC;

        private Character boxedC;

        private int unboxedInt;

        private Integer boxedInt;

        private long unboxedLong;

        private Long boxedLong;

        private short unboxedShort;

        private Short boxedShort;

        private String s;

        public LocalEntity() {

        }

        public LocalEntity(
            byte b,
            char unboxedC,
            Character boxedC,
            int unboxedInt,
            Integer boxedInt,
            long unboxedLong,
            Long boxedLong,
            short unboxedShort,
            Short boxedShort,
            String s
        ) {
            this.b = b;
            this.unboxedC = unboxedC;
            this.boxedC = boxedC;
            this.unboxedInt = unboxedInt;
            this.boxedInt = boxedInt;
            this.unboxedLong = unboxedLong;
            this.boxedLong = boxedLong;
            this.unboxedShort = unboxedShort;
            this.boxedShort = boxedShort;
            this.s = s;
        }

        public byte getB() {
            return b;
        }

        public char getUnboxedC() {
            return unboxedC;
        }

        public Character getBoxedC() {
            return boxedC;
        }

        public int getUnboxedInt() {
            return unboxedInt;
        }

        public Integer getBoxedInt() {
            return boxedInt;
        }

        public long getUnboxedLong() {
            return unboxedLong;
        }

        public Long getBoxedLong() {
            return boxedLong;
        }

        public short getUnboxedShort() {
            return unboxedShort;
        }

        public Short getBoxedShort() {
            return boxedShort;
        }

        public String getS() {
            return s;
        }
    }

    @Test
    public void entityMatcherTest_success() {

        Assert.assertThat(
            LocalEntity::new,
            new EntityMatcher<LocalEntity>(
                new EntityMatcher.PropertyValue("b", byte.class, (byte) 0),
                new EntityMatcher.PropertyValue("unboxedC", char.class, (char) 0),
                new EntityMatcher.PropertyValue("boxedC", Character.class, null),
                new EntityMatcher.PropertyValue("unboxedInt", int.class, (int) 0),
                new EntityMatcher.PropertyValue("boxedInt", Integer.class, null),
                new EntityMatcher.PropertyValue("unboxedLong", long.class, (long) 0),
                new EntityMatcher.PropertyValue("boxedLong", Long.class, null),
                new EntityMatcher.PropertyValue("unboxedShort", short.class, (short) 0),
                new EntityMatcher.PropertyValue("boxedShort", Short.class, null),
                new EntityMatcher.PropertyValue("s", String.class, null)));

        Assert.assertThat(
            new Supplier<LocalEntity>() {
                public LocalEntity get() {
                    return new LocalEntity(
                        (byte) 1, 'A', 'B', 2, 3, 4L, 5L, (short) 6, (short) 7, "test");
                }
            },
            new EntityMatcher<LocalEntity>(
                new EntityMatcher.PropertyValue("b", byte.class, (byte) 1),
                new EntityMatcher.PropertyValue("unboxedC", char.class, (char) 'A'),
                new EntityMatcher.PropertyValue("boxedC", Character.class, 'B'),
                new EntityMatcher.PropertyValue("unboxedInt", int.class, (int) 2),
                new EntityMatcher.PropertyValue("boxedInt", Integer.class, 3),
                new EntityMatcher.PropertyValue("unboxedLong", long.class, (long) 4),
                new EntityMatcher.PropertyValue("boxedLong", Long.class, (long) 5),
                new EntityMatcher.PropertyValue("unboxedShort", short.class, (short) 6),
                new EntityMatcher.PropertyValue("boxedShort", Short.class, (short) 7),
                new EntityMatcher.PropertyValue("s", String.class, "test")));
    }
}
