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

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd2code.DecoratorHelper;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

/**
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are instances of {@link Cache}.
 * This decorator copies the methods of the attribute to provide easier access.
 */
@Nonnull
public class CacheDecorator extends AbstractMethodDecorator<ASTCDAttribute> {
    /**
     * @see Cache#asMap()
     */
    @Nonnull
    private static final String AS_MAP = "<<Nonnull>> public ConcurrentMap<%2$s,%3$s> asMap%1$s();";
    /**
     * @see Cache#cleanUp()
     */
    @Nonnull
    private static final String CLEAN_UP = "public void cleanUp%s();";
    /**
     * This method simulates {@link LoadingCache#get(Object)}. If there is a value associated with the given key, it
     * will be returned. Otherwise an {@link NoSuchElementException} will be thrown.
     * @see LoadingCache#get(Object)
     */
    @Nonnull
    private static final String GET = "<<Nonnull>> public %3$s get%1$s(" +
            "%2$s key" +
    ") throws NoSuchElementException;";
    /**
     * @see Cache#get(Object, Callable)
     */
    @Nonnull
    private static final String GET_LOADER = "<<Nonnull>> public %3$s get%1$s(" +
            "%2$s key," +
            "Callable<? extends %3$s> loader" +
    ") throws ExecutionException;";
    /**
     * @see Cache#getAllPresent(Iterable) 
     */
    @Nonnull
    private static final String GET_ALL_PRESENT = "<<Nonnull>> public ImmutableMap<%2$s,%3$s> getAllPresent%1$s(" +
            "Iterable<?> keys" +
    ");";
    /**
     * @see Cache#getIfPresent(Object) 
     */
    @Nonnull
    private static final String GET_IF_PRESENT = "<<Nullable>> public %2$s getIfPresent%1$s(" +
            "Object key" +
    ");";
    /**
     * @see Cache#invalidate(Object) 
     */
    @Nonnull
    private static final String INVALIDATE = "public void invalidate%s(" +
            "Object key" +
    ");";
    /**
     * @see Cache#invalidateAll() 
     */
    @Nonnull
    private static final String INVALIDATE_ALL = "public void invalidateAll%s();";
    /**
     * @see Cache#invalidateAll(Iterable)
     */
    @Nonnull
    private static final String INVALIDATE_ALL_KEYS = "public void invalidateAll%s(" +
            "Iterable<?> keys" +
    ");";
    /**
     * @see Cache#put(Object, Object)
     */
    @Nonnull
    private static final String PUT = "public void put%s(" +
            "%s key," +
            "%s value" +
    ");";
    /**
     * @see Cache#putAll(Map)
     */
    @Nonnull
    private static final String PUT_ALL = "public void putAll%s(" +
            "Map<? extends %s, ? extends %s> map" +
    ");";
    /**
     * @see Cache#size()
     */
    @Nonnull
    private static final String SIZE = "public long size%s();";
    /**
     * @see Cache#stats()
     */
    @Nonnull
    private static final String STATS = "<<Nonnull>> public CacheStats stats%s();";
    /**
     * This method simulates {@link Map#values()} to allow easier access over all elements
     * currently present in the {@link Cache}.
     * @see Map#values()
     */
    @Nonnull
    private static final String VALUES = "<<Nonnull>> public Collection<%2$s> values%1$s();";

    public CacheDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
        TEMPLATES.put(AS_MAP, "decorator.cache.AsMap");
        TEMPLATES.put(CLEAN_UP, "decorator.cache.CleanUp");
        TEMPLATES.put(GET, "decorator.cache.Get");
        TEMPLATES.put(GET_LOADER, "decorator.cache.GetLoader");
        TEMPLATES.put(GET_ALL_PRESENT, "decorator.cache.GetAllPresent");
        TEMPLATES.put(GET_IF_PRESENT, "decorator.cache.GetIfPresent");
        TEMPLATES.put(INVALIDATE, "decorator.cache.Invalidate");
        TEMPLATES.put(INVALIDATE_ALL, "decorator.cache.InvalidateAll");
        TEMPLATES.put(INVALIDATE_ALL_KEYS, "decorator.cache.InvalidateAllKeys");
        TEMPLATES.put(PUT, "decorator.cache.Put");
        TEMPLATES.put(PUT_ALL, "decorator.cache.PutAll");
        TEMPLATES.put(SIZE, "decorator.cache.Size");
        TEMPLATES.put(STATS, "decorator.cache.Stats");
        TEMPLATES.put(VALUES, "decorator.cache.Values");
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute){
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Key and Value
        String keyTypeArgumentName = DecoratorHelper.getMCTypeArgumentName(cdAttribute, 0);
        String valueTypeArgumentName = DecoratorHelper.getMCTypeArgumentName(cdAttribute, 1);

        //ConcurrentMap<K, V> asMap()
        signatures.put(AS_MAP, String.format
                (
                        AS_MAP,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //void cleanUp()
        signatures.put(CLEAN_UP, String.format
                (
                        CLEAN_UP,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //V get(K key)
        signatures.put(GET, String.format
                (
                        GET,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V get(K key, Callable<? extends V> loader)
        signatures.put(GET_LOADER, String.format
                (
                        GET_LOADER,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //ImmutableMap<K,V> getAllPresent(Iterable<?> keys)
        signatures.put(GET_ALL_PRESENT, String.format
                (
                        GET_ALL_PRESENT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //V getIfPresent(Object k)
        signatures.put(GET_IF_PRESENT, String.format
                (
                        GET_IF_PRESENT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        valueTypeArgumentName
                )
        );
        //void invalidate(Object key)
        signatures.put(INVALIDATE, String.format
                (
                        INVALIDATE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );

        //void invalidateAll()
        signatures.put(INVALIDATE_ALL, String.format
                (
                        INVALIDATE_ALL,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //void invalidateAll(Iterable<?> keys)
        signatures.put(INVALIDATE_ALL_KEYS, String.format
                (
                        INVALIDATE_ALL_KEYS,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //void put(K key, V value)
        signatures.put(PUT, String.format
                (
                        PUT,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName
                )
        );
        //void putAll(Map<? extends K, ? extends V> map)
        signatures.put(PUT_ALL, String.format
                (
                        PUT_ALL,
                        DecoratorHelper.toSingularCapitalized(cdAttribute),
                        keyTypeArgumentName,
                        valueTypeArgumentName)
        );
        //long size()
        signatures.put(SIZE, String.format
                (
                        SIZE,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //CacheStats stats()
        signatures.put(STATS, String.format
                (
                        STATS,
                        DecoratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Collection<V> values()
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
