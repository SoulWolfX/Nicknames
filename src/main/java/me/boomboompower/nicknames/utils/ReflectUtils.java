/*
 *     Copyright (C) 2016 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.boomboompower.nicknames.utils;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static <T> MethodHandle findMethod(Class<T> clazz, String[] methodNames, Class<?>... methodTypes) {
        final Method method = ReflectionHelper.findMethod(clazz, null, methodNames, methodTypes);

        try {
            return MethodHandles.lookup().unreflect(method);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToFindMethodException(methodNames, e);
        }
    }

    public static MethodHandle findFieldGetter(Class<?> clazz, String... fieldNames) {
        final Field field = ReflectionHelper.findField(clazz, fieldNames);

        try {
            return MethodHandles.lookup().unreflectGetter(field);
        } catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static MethodHandle findFieldSetter(Class<?> clazz, String... fieldNames) {
        final Field field = ReflectionHelper.findField(clazz, fieldNames);

        try {
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (IllegalAccessException e) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
        }
    }
}
