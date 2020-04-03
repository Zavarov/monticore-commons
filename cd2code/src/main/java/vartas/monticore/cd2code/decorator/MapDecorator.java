/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.monticore.cd2code.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd2code.DecoratorHelper;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
/**
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are instances
 * of {@link Map}. This decorator copies the methods of the attribute to provide easier access.
 */
public class MapDecorator extends AbstractMethodDecorator<ASTCDAttribute>{
    /**
     * @see Map#clear()
     */
    @Nonnull
    private static final String CLEAR = "public void clear%s();";
    /**
     * @see Map#compute(Object, BiFunction)
     */
    @Nonnull
    private static final String COMPUTE = "<<Nonnull>> public %3$s compute%1$s(" +
            "%2$s key, " +
            "BiFunction<? super %2$s, ? super %3$s, ? extends %3$s> remappingFunction" +
    ");";
    /**
     * @see Map#computeIfAbsent(Object, Function)
     */
    @Nonnull
    private static final String COMPUTE_IF_ABSENT = "<<Nonnull>> public %3$s computeIfAbsent%1$s(" +
            "%2$s key, " +
            "Function<? super %2$s, ? extends %3$s> mappingFunction" +
    ");";
    /**
     * @see Map#computeIfPresent(Object, BiFunction)
     */
    @Nonnull
    private static final String COMPUTE_IF_PRESENT = "<<Nonnull>> public %3$s computeIfPresent%1$s(" +
            "%2$s key, " +
            "BiFunction<? super %2$s, ? super %3$s, ? extends %3$s> remappingFunction" +
    ");";
    /**
     * @see Map#containsKey(Object)
     */
    @Nonnull
    private static final String CONTAINS_KEY = "public boolean containsKey%s(" +
            "Object key" +
    ");";
    /**
     * @see Map#containsValue(Object)
     */
    @Nonnull
    private static final String CONTAINS_VALUE = "public boolean containsValue%s(" +
            "Object value" +
    ");";
    /**
     * @see Map#entrySet()  
     */
    @Nonnull
    private static final String ENTRY_SET = "<<Nonnull>> public Set<Map.Entry<%2$s,%3$s>> entrySet%1$s();";
    /**
     * @see Map#equals(Object) 
     */
    @Nonnull
    private static final String EQUALS = "public boolean equals%s(" +
            "Object o" +
    ");";
    /**
     * @see Map#forEach(BiConsumer) 
     */
    @Nonnull
    private static final String FOR_EACH = "public void forEach%s(" +
            "BiConsumer<? super %s, ? super %s> action" +
    ");";
    /**
     * @see Map#get(Object) 
     */
    @Nonnull
    private static final String GET = "<<Nullable>> public %2$s get%1$s(" +
            "Object key" +
    ");";
    /**
     * @see Map#getOrDefault(Object, Object) 
     */
    @Nonnull
    private static final String GET_OR_DEFAULT = "<<Nonnull>> public %2$s getOrDefault%1$s(" +
            "Object key, " +
            "%2$s defaultValue" +
    ");";
    /**
     * @see Map#hashCode() 
     */
    @Nonnull
    private static final String HASH_CODE = "public int hashCode%s();";
    /**
     * @see Map#isEmpty() 
     */
    @Nonnull
    private static final String IS_EMPTY = "public boolean isEmpty%s();";
    /**
     * @see Map#keySet() 
     */
    @Nonnull
    private static final String KEY_SET = "<<Nonnull>> public Set<%2$s> keySet%1$s();";
    /**
     * @see Map#merge(Object, Object, BiFunction) 
     */
    @Nonnull
    private static final String MERGE = "<<Nullable>> public %3$s merge%1$s(" +
            "%2$s key, " +
            "%3$s value, " +
            "BiFunction<? super %3$s, ? super %3$s, ? extends %3$s> remappingFunction" +
    ");";
    /**
     * @see Map#put(Object, Object)
     */
    @Nonnull
    private static final String PUT = "<<Nullable>> public %3$s put%1$s(" +
            "%2$s key, " +
            "%3$s value" +
    ");";
    /**
     * @see Map#putAll(Map)
     */
    @Nonnull
    private static final String PUT_ALL = "public void putAll%s(" +
            "Map<? extends %s, ? extends %s> m" +
    ");";
    /**
     * @see Map#putIfAbsent(Object, Object)
     */
    @Nonnull
    private static final String PUT_IF_ABSENT = "<<Nullable>> public %3$s putIfAbsent%1$s(" +
            "%2$s key, " +
            "%3$s value" +
    ");";
    /**
     * @see Map#remove(Object)
     */
    @Nonnull
    private static final String REMOVE_KEY = "<<Nullable>> public %2$s remove%1$s(" +
            "Object key" +
    ");";
    /**
     * @see Map#remove(Object, Object)
     */
    @Nonnull
    private static final String REMOVE_KEY_VALUE = "public boolean remove%1$s(" +
            "Object key, " +
            "Object value" +
    ");";
    /**
     * @see Map#replace(Object, Object)
     */
    @Nonnull
    private static final String REPLACE_KEY = "<<Nullable>> public %3$s replace%1$s(" +
            "%2$s key, " +
            "%3$s value" +
    ");";
    /**
     * @see Map#replace(Object, Object, Object)
     */
    @Nonnull
    private static final String REPLACE_KEY_VALUE = "public boolean replace%1$s(" +
            "%2$s key, " +
            "%3$s oldValue, " +
            "%3$s newValue" +
    ");";
    /**
     * @see Map#replaceAll(BiFunction)
     */
    @Nonnull
    private static final String REPLACE_ALL = "public void replaceAll%1$s(" +
            "BiFunction<? super %2$s, ? super %3$s, ? extends %3$s> function" +
    ");";
    /**
     * @see Map#size()
     */
    @Nonnull
    private static final String SIZE = "public int size%s();";
    /**
     * @see Map#values()
     */
    @Nonnull
    private static final String VALUES = "<<Nonnull>> public Collection<%2$s> values%1$s();";

    public MapDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
        TEMPLATES.put(CLEAR, "decorator.map.Clear");
        TEMPLATES.put(COMPUTE, "decorator.map.Compute");
        TEMPLATES.put(COMPUTE_IF_ABSENT, "decorator.map.ComputeIfAbsent");
        TEMPLATES.put(COMPUTE_IF_PRESENT, "decorator.map.ComputeIfPresent");
        TEMPLATES.put(CONTAINS_KEY, "decorator.map.ContainsKey");
        TEMPLATES.put(CONTAINS_VALUE, "decorator.map.ContainsValue");
        TEMPLATES.put(ENTRY_SET, "decorator.map.EntrySet");
        TEMPLATES.put(EQUALS, "decorator.map.Equals");
        TEMPLATES.put(FOR_EACH, "decorator.map.ForEach");
        TEMPLATES.put(GET, "decorator.map.Get");
        TEMPLATES.put(GET_OR_DEFAULT, "decorator.map.GetOrDefault");
        TEMPLATES.put(HASH_CODE, "decorator.map.HashCode");
        TEMPLATES.put(IS_EMPTY, "decorator.map.IsEmpty");
        TEMPLATES.put(KEY_SET, "decorator.map.KeySet");
        TEMPLATES.put(MERGE, "decorator.map.Merge");
        TEMPLATES.put(PUT, "decorator.map.Put");
        TEMPLATES.put(PUT_ALL, "decorator.map.PutAll");
        TEMPLATES.put(PUT_IF_ABSENT, "decorator.map.PutIfAbsent");
        TEMPLATES.put(REMOVE_KEY, "decorator.map.RemoveKey");
        TEMPLATES.put(REMOVE_KEY_VALUE, "decorator.map.RemoveKeyValue");
        TEMPLATES.put(REPLACE_KEY, "decorator.map.ReplaceKey");
        TEMPLATES.put(REPLACE_KEY_VALUE, "decorator.map.ReplaceKeyValue");
        TEMPLATES.put(REPLACE_ALL, "decorator.map.ReplaceAll");
        TEMPLATES.put(SIZE, "decorator.map.Size");
        TEMPLATES.put(VALUES, "decorator.map.Values");
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute){
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Key and value
        String keyTypeArgumentName = DecoratorHelper.getMCTypeArgumentName(cdAttribute, 0);
        String valueTypeArgumentName = DecoratorHelper.getMCTypeArgumentName(cdAttribute, 1);

        //void clear()
        signatures.put(CLEAR, String.format
                (
                        CLEAR,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
        signatures.put(COMPUTE, String.format
                (
                        COMPUTE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
        signatures.put(COMPUTE_IF_ABSENT, String.format
                (
                        COMPUTE_IF_ABSENT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
        signatures.put(COMPUTE_IF_PRESENT, String.format
                (
                        COMPUTE_IF_PRESENT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //boolean containsKey(Object key)
        signatures.put(CONTAINS_KEY, String.format
                (
                        CONTAINS_KEY,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean containsValue(Object value)
        signatures.put(CONTAINS_VALUE, String.format
                (
                        CONTAINS_VALUE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Set<Map.Entry<K,V>> entrySet()
        signatures.put(ENTRY_SET, String.format
                (
                        ENTRY_SET,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //boolean equals(Object o)
        signatures.put(EQUALS, String.format
                (
                        EQUALS,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //void forEach(BiConsumer<? super K, ? super V> action)
        signatures.put(FOR_EACH, String.format
                (
                        FOR_EACH,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V get(Object key)
        signatures.put(GET, String.format
                (
                        GET,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        valueTypeArgumentName
                )
        );
        //V getOrDefault(Object key, V defaultValue)
        signatures.put(GET_OR_DEFAULT, String.format
                (
                        GET_OR_DEFAULT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        valueTypeArgumentName
                )
        );
        //int hashCode()
        signatures.put(HASH_CODE, String.format
                (
                        HASH_CODE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean isEmpty()
        signatures.put(IS_EMPTY, String.format
                (
                        IS_EMPTY,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Set<K> keySet()
        signatures.put(KEY_SET, String.format
                (
                        KEY_SET,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName
                )
        );
        //V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
        signatures.put(MERGE, String.format
                (
                        MERGE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V put(K key, V value)
        signatures.put(PUT, String.format
                (
                        PUT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //void putAll(Map<? extends K, ? extends V> m)
        signatures.put(PUT_ALL, String.format
                (
                        PUT_ALL,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V putIfAbsent(K key, V value)
        signatures.put(PUT_IF_ABSENT, String.format
                (
                        PUT_IF_ABSENT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V remove(Object key)
        signatures.put(REMOVE_KEY, String.format
                (
                        REMOVE_KEY,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        valueTypeArgumentName
                )
        );
        //boolean remove(Object key, Object value)
        signatures.put(REMOVE_KEY_VALUE, String.format
                (
                        REMOVE_KEY_VALUE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //V replace(K key, V value)
        signatures.put(REPLACE_KEY, String.format
                (
                        REPLACE_KEY,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //boolean replace(K key, V oldValue, V newValue)
        signatures.put(REPLACE_KEY_VALUE, String.format
                (
                        REPLACE_KEY_VALUE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
        signatures.put(REPLACE_ALL, String.format
                (
                        REPLACE_ALL,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //int size()
        signatures.put(SIZE, String.format
                (
                        SIZE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Collection<V> values
        signatures.put(VALUES, String.format
                (
                        VALUES,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        valueTypeArgumentName
                )
        );
        return signatures;
    }
}
