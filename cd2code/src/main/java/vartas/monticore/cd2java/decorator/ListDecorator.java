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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Applies the Decorator pattern for all attributes of a {@link ASTCDClass CDClass} that are instances
 * of {@link List}. This decorator copies the methods of the attribute to provide easier access.
 */
@Nonnull
public class ListDecorator extends CollectionDecorator{
    /**
     * @see List#add(int, Object) 
     */
    @Nonnull
    private static final String ADD = "public void add%s(int index, %s element);";
    /**
     * @see List#addAll(int, Collection) 
     */
    @Nonnull
    private static final String ADD_ALL = "public boolean addAll%s(" +
            "int index, " +
            "Collection<? extends %s> c" +
    ");";
    /**
     * @see List#get(int) 
     */
    @Nonnull
    private static final String GET = "<<Nonnull>> public %2$s get%1$s(" +
            "int index" +
    ");";
    /**
     * @see List#indexOf(Object) 
     */
    @Nonnull
    private static final String INDEX_OF = "public int indexOf%s(" +
            "Object o" +
    ");";
    /**
     * @see List#lastIndexOf(Object) 
     */
    @Nonnull
    private static final String LAST_INDEX_OF = "public int lastIndexOf%s(" +
            "Object o" +
    ");";
    /**
     * @see List#listIterator() 
     */
    @Nonnull
    private static final String LIST_ITERATOR = "<<Nonnull>> public ListIterator<%2$s> listIterator%1$s();";
    /**
     * @see List#listIterator(int) 
     */
    @Nonnull
    private static final String LIST_ITERATOR_INDEX = "<<Nonnull>> public ListIterator<%2$s> listIterator%1$s(" +
            "int index" +
    ");";
    /**
     * @see List#remove(int) 
     */
    @Nonnull
    private static final String REMOVE = "<<Nonnull>> public %2$s remove%1$s(" +
            "int index" +
    ");";
    /**
     * @see List#replaceAll(UnaryOperator) 
     */
    @Nonnull
    private static final String REPLACE_ALL = "public void replaceAll%s(" +
            "UnaryOperator<%s> operator" +
    ");";
    /**
     * @see List#set(int, Object) 
     */
    @Nonnull
    private static final String SET = "<<Nonnull>> public %2$s set%1$s(" +
            "int index, " +
            "%2$s element" +
    ");";
    /**
     * @see List#sort(Comparator) 
     */
    @Nonnull
    private static final String SORT = "public void sort%s(" +
            "Comparator<? super %s> c" +
    ");";
    /**
     * @see List#subList(int, int)
     */
    @Nonnull
    private static final String SUB_LIST = "<<Nonnull>> public List<%2$s> subList%1$s(" +
            "int fromIndex, " +
            "int toIndex" +
    ");";

    public ListDecorator(@Nonnull GlobalExtensionManagement glex){
        super(glex);
        TEMPLATES.put(ADD, "decorator.list.Add");
        TEMPLATES.put(ADD_ALL, "decorator.list.AddAll");
        TEMPLATES.put(GET, "decorator.list.Get");
        TEMPLATES.put(INDEX_OF, "decorator.list.IndexOf");
        TEMPLATES.put(LAST_INDEX_OF, "decorator.list.LastIndexOf");
        TEMPLATES.put(LIST_ITERATOR, "decorator.list.ListIterator");
        TEMPLATES.put(LIST_ITERATOR_INDEX, "decorator.list.ListIteratorIndex");
        TEMPLATES.put(REMOVE, "decorator.list.Remove");
        TEMPLATES.put(REPLACE_ALL, "decorator.list.ReplaceAll");
        TEMPLATES.put(SET, "decorator.list.Set");
        TEMPLATES.put(SORT, "decorator.list.Sort");
        TEMPLATES.put(SUB_LIST, "decorator.list.SubList");
    }

    @Override
    @Nonnull
    public LinkedHashMap<String, String> getMethodSignatures(@Nonnull ASTCDAttribute cdAttribute){
        LinkedHashMap<String, String> signatures = new LinkedHashMap<>();

        //Element
        String typeArgumentName = CDGeneratorHelper.getMCTypeArgumentName(cdAttribute, 0);

        //void add(int index, E element)
        signatures.put(ADD, String.format
                (
                        ADD,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //boolean addAll(int index, Collection<? extends E> c)
        signatures.put(ADD_ALL, String.format
                (
                        ADD_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //E get(int index)
        signatures.put(GET, String.format
                (
                        GET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //int indexOf(Object)
        signatures.put(INDEX_OF, String.format
                (
                        INDEX_OF,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //int lastIndexOf(Object o)
        signatures.put(LAST_INDEX_OF, String.format
                (
                        LAST_INDEX_OF,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute)
                )
        );
        //ListIterator<E> listIterator()
        signatures.put(LIST_ITERATOR, String.format
                (
                        LIST_ITERATOR,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //ListIterator<E> listIterator(int index)
        signatures.put(LIST_ITERATOR_INDEX, String.format
                (
                        LIST_ITERATOR_INDEX,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //E remove(int index)
        signatures.put(REMOVE, String.format
                (
                        REMOVE,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //void replaceAll(UnaryOperator<E> operator)
        signatures.put(REPLACE_ALL, String.format
                (
                        REPLACE_ALL,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //E set(int index, E element)
        signatures.put(SET, String.format
                (
                        SET,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //void sort(Comparator<? super E> c)
        signatures.put(SORT, String.format
                (
                        SORT,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        //List<E> subList(int fromIndex, int toIndex)
        signatures.put(SUB_LIST, String.format
                (
                        SUB_LIST,
                        CDGeneratorHelper.toSingularCapitalized(cdAttribute),
                        typeArgumentName
                )
        );
        return signatures;
    }
}
