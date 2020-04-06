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

package vartas.monticore.cd2java.decorator;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDClass;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import vartas.monticore.cd2code.CDGeneratorHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are instances
 * of {@link Collection}. This decorator copies the methods of the attribute to provide easier access.
 */
@Nonnull
public class CollectionDecorator extends AbstractMethodDecorator<ASTCDAttribute> {
    /**
     * @see Collection#add(Object) 
     */
    @Nonnull
    private static final String ADD = "public boolean add%s(" +
            "%s e" +
    ");";
    /**
     * @see Collection#addAll(Collection) 
     */
    @Nonnull
    private static final String ADD_ALL = "public boolean addAll%s(" +
            "Collection<? extends %s> c" +
    ");";
    /**
     * @see Collection#clear() 
     */
    @Nonnull
    private static final String CLEAR = "public void clear%s();";
    /**
     * @see Collection#contains(Object) 
     */
    @Nonnull
    private static final String CONTAINS = "public boolean contains%s(" +
            "Object o" +
    ");";
    /**
     * @see Collection#containsAll(Collection) 
     */
    @Nonnull
    private static final String CONTAINS_ALL = "public boolean containsAll%s(" +
            "Collection<?> c" +
    ");";
    /**
     * @see Collection#equals(Object)
     */
    @Nonnull
    private static final String EQUALS = "public boolean equals%s(" +
            "Object o" +
    ");";
    /**
     * @see Iterable#forEach(Consumer)
     */
    @Nonnull
    private static final String FOR_EACH = "public void forEach%s(" +
            "Consumer<? super %s> action" +
    ");";
    /**
     * @see Collection#hashCode()
     */
    @Nonnull
    private static final String HASH_CODE = "public int hashCode%s();";
    /**
     * @see Collection#isEmpty()
     */
    @Nonnull
    private static final String IS_EMPTY = "public boolean isEmpty%s();";
    /**
     * @see Collection#iterator()
     */
    @Nonnull
    private static final String ITERATOR = "<<Nonnull>> public Iterator<%2$s> iterator%1$s();";
    /**
     * @see Collection#parallelStream() 
     */
    @Nonnull
    private static final String PARALLEL_STREAM = "<<Nonnull>> public Stream<%2$s> parallelStream%s();";
    /**
     * @see Collection#remove(Object) 
     */
    @Nonnull
    private static final String REMOVE = "public boolean remove%s(" +
            "Object o" +
    ");";
    /**
     * @see Collection#removeAll(Collection) 
     */
    @Nonnull
    private static final String REMOVE_ALL = "public boolean removeAll%s(" +
            "Collection<?> c" +
    ");";
    /**
     * @see Collection#removeIf(Predicate) 
     */
    @Nonnull
    private static final String REMOVE_IF = "public boolean removeIf%s(" +
            "Predicate<? super %s> filter" +
    ");";
    /**
     * @see Collection#retainAll(Collection)
     */
    @Nonnull
    private static final String RETAIN_ALL = "public boolean retainAll%s(" +
            "Collection<?> c" +
    ");";
    /**
     * @see Collection#size()
     */
    @Nonnull
    private static final String SIZE = "public int size%s();";
    /**
     * @see Collection#spliterator()
     */
    @Nonnull
    private static final String SPLITERATOR = "<<Nonnull>> public Spliterator<%2$s> spliterator%s();";
    /**
     * @see Collection#stream()
     */
    @Nonnull
    private static final String STREAM = "<<Nonnull>> public Stream<%2$s> stream%s();";
    /**
     * @see Collection#toArray() 
     */
    @Nonnull
    private static final String TO_ARRAY = "<<Nonnull>> public Object[] toArray%s();";
    /**
     * @see Collection#toArray(IntFunction)
     */
    @Nonnull
    private static final String TO_INT_FUNCTION_ARRAY = "<<Nonnull>> public %2$s[] toArray%1$s(" +
            "IntFunction<%2$s[]> generator" +
    ");";
    /**
     * @see Collection#toArray(Object[])
     */
    @Nonnull
    private static final String TO_GENERIC_ARRAY = "<<Nonnull>> public %2$s[] toArray%1$s(" +
            "%2$s[] a" +
    ");";

    public CollectionDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
        TEMPLATES.put(ADD, "decorator.collection.Add");
        TEMPLATES.put(ADD_ALL, "decorator.collection.AddAll");
        TEMPLATES.put(CLEAR, "decorator.collection.Clear");
        TEMPLATES.put(CONTAINS, "decorator.collection.Contains");
        TEMPLATES.put(CONTAINS_ALL, "decorator.collection.ContainsAll");
        TEMPLATES.put(EQUALS, "decorator.collection.Equals");
        TEMPLATES.put(FOR_EACH, "decorator.collection.ForEach");
        TEMPLATES.put(HASH_CODE, "decorator.collection.HashCode");
        TEMPLATES.put(IS_EMPTY, "decorator.collection.IsEmpty");
        TEMPLATES.put(ITERATOR, "decorator.collection.Iterator");
        TEMPLATES.put(PARALLEL_STREAM, "decorator.collection.ParallelStream");
        TEMPLATES.put(REMOVE, "decorator.collection.Remove");
        TEMPLATES.put(REMOVE_ALL, "decorator.collection.RemoveAll");
        TEMPLATES.put(REMOVE_IF, "decorator.collection.RemoveIf");
        TEMPLATES.put(RETAIN_ALL, "decorator.collection.RetainAll");
        TEMPLATES.put(SIZE, "decorator.collection.Size");
        TEMPLATES.put(SPLITERATOR, "decorator.collection.Spliterator");
        TEMPLATES.put(STREAM, "decorator.collection.Spliterator");
        TEMPLATES.put(TO_ARRAY, "decorator.collection.ToArray");
        TEMPLATES.put(TO_INT_FUNCTION_ARRAY, "decorator.collection.ToIntFunctionArray");
        TEMPLATES.put(TO_GENERIC_ARRAY, "decorator.collection.ToGenericArray");
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute){
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Element
        String typeArgumentName = CDGeneratorHelper.getMCTypeArgumentName(cdAttribute, 0);

        //boolean add(E e)
        signatures.put(ADD, String.format
                (
                        ADD,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //boolean addAll(Collection<? extends E> c)
        signatures.put(ADD_ALL, String.format
                (
                        ADD_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //void clear()
        signatures.put(CLEAR, String.format
                (
                        CLEAR,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean contains(Object o)
        signatures.put(CONTAINS, String.format
                (
                        CONTAINS,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean containsAll(Collection<?> o)
        signatures.put(CONTAINS_ALL, String.format
                (
                        CONTAINS_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean equals(Object o)
        signatures.put(EQUALS, String.format
                (
                        EQUALS,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //void forEach(Consumer<? super T> action)
        signatures.put(FOR_EACH, String.format
                (
                        FOR_EACH,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //int hashCode()
        signatures.put(HASH_CODE, String.format
                (
                        HASH_CODE,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean isEmpty()
        signatures.put(IS_EMPTY, String.format
                (
                        IS_EMPTY,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Iterator<E> iterator()
        signatures.put(ITERATOR, String.format
                (
                        ITERATOR,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //Stream<E> parallelStream()
        signatures.put(PARALLEL_STREAM, String.format
                (
                        PARALLEL_STREAM,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //boolean remove(Object o)
        signatures.put(REMOVE, String.format
                (
                        REMOVE,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean removeAll(Collection<?> c)
        signatures.put(REMOVE_ALL, String.format
                (
                        REMOVE_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //boolean removeIf(Predicate<? super E> filter)
        signatures.put(REMOVE_IF, String.format
                (
                        REMOVE_IF,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //boolean retainAll(Collection<?> c)
        signatures.put(RETAIN_ALL, String.format
                (
                        RETAIN_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //int size()
        signatures.put(SIZE, String.format
                (
                        SIZE,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //Spliterator<E> spliterator()
        signatures.put(SPLITERATOR, String.format
                (
                        SPLITERATOR,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //Object[] toArray()
        signatures.put(TO_ARRAY, String.format
                (
                        TO_ARRAY,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //<T> T[] toArray(IntFunction<T[]> generator)
        signatures.put(TO_INT_FUNCTION_ARRAY, String.format
                (
                        TO_INT_FUNCTION_ARRAY,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //<T> T[] toArray(T[] a)
        signatures.put(TO_GENERIC_ARRAY, String.format
                (
                        TO_GENERIC_ARRAY,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        return signatures;
    }
}
