package cn.icframework.test;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author iceFire
 * @date 2023/6/5
 */
public class TestGen {
    private String i;

    @Test
    public void gen(){
        try {
            Field declaredField = TestGen.class.getDeclaredField("i");
            Class<?> declaringClass = declaredField.getDeclaringClass();
            System.out.println();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
