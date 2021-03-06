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

package zav.mc.monticore.cd4code;

import de.monticore.cd.cd4analysis._ast.ASTCDAttribute;
import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.cd.cd4analysis._ast.ASTCDType;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbol;
import de.monticore.cd.cd4analysis._symboltable.CDTypeSymbolSurrogate;
import de.monticore.cd.cd4code._visitor.CD4CodeVisitor;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.se_rwth.commons.Joiners;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import zav.mc.cd4code.CDGeneratorHelper;
import zav.mc.monticore.BasicCDTest;

public class ModelTest extends BasicCDTest {
    @ParameterizedTest
    @CsvSource(value = {
            "java.io : IOException",
            "java.io : Serializable",

            "java.lang : CharSequence",
            "java.lang : Comparable",
            "java.lang : Exception",
            "java.lang : InterruptedException",
            "java.lang : Iterable",
            "java.lang : Number",
            "java.lang : Object",
            "java.lang : Runnable",
            "java.lang : RuntimeException",
            "java.lang : StackTraceElement",
            "java.lang : String",
            "java.lang : StringBuffer",
            "java.lang : Throwable",

            "java.nio.file : Path",
            "java.nio.file : Watchable",

            "java.math : BigDecimal",
            "java.math : BigInteger",
            "java.math : MathContext",
            "java.math : RoundingMode",

            //TODO "java.time.chrono : ChronoLocalDate",
            //TODO "java.time.chrono : ChronoLocalDateTime",

            "java.time.format : ResolverStyle",
            //TODO "java.time.format : DateTimeFormatter",

            "java.time.temporal : ChronoUnit",
            "java.time.temporal : Temporal",
            "java.time.temporal : TemporalAccessor",
            "java.time.temporal : TemporalAdjuster",
            "java.time.temporal : TemporalAmount",
            "java.time.temporal : TemporalField",
            "java.time.temporal : TemporalUnit",
            "java.time.temporal : ValueRange",

            //TODO "java.time : DayOfWeek",
            "java.time : Duration",
            //TODO "java.time : Instant",
            //TODO "java.time : LocalDate",
            "java.time : LocalDateTime",
            //TODO "java.time : LocalTime",
            //TODO "java.time : Month",
            "java.time : OffsetDateTime",
            "java.time : OffsetTime",
            //TODO "java.time : ZonedDateTime",
            //TODO "java.time : ZoneId",
            //TODO "java.time : ZoneOffset",

            "java.util.concurrent : Callable",
            "java.util.concurrent : ConcurrentMap",
            "java.util.concurrent : ExecutionException",

            "java.util.function : BiConsumer",
            "java.util.function : BiFunction",
            "java.util.function : Consumer",
            "java.util.function : Function",
            "java.util.function : IntFunction",
            "java.util.function : ObjIntConsumer",
            "java.util.function : Predicate",
            "java.util.function : Supplier",
            "java.util.function : UnaryOperator",

            "java.util.stream : BaseStream",
            //TODO "java.util.stream : IntStream",
            //TODO "java.util.stream : Stream",


            "java.util : AbstractCollection",
            "java.util : AbstractList",
            "java.util : Collection",
            //TODO "java.util : Comparator",
            "java.util : Iterator",
            "java.util : List",
            "java.util : ListIterator",
            "java.util : Locale",
            "java.util : Map",
            "java.util : Optional",
            //TODO "java.util : Random",
            "java.util : Spliterator",
            "java.util : Set",

            //TODO "com.google.common.cache : Cache",
            "com.google.common.collect : Multimap",
            "com.google.common.collect : Multiset"
    }, delimiter = ':')
    public void testModel(String packageName, String className){
        String qualifiedName;

        qualifiedName = Joiners.DOT.join(packageName, className);
        Assertions.assertThat(globalScope.resolveCDDefinition(qualifiedName)).isPresent();

        qualifiedName = Joiners.DOT.join(packageName, className, className);
        Assertions.assertThat(globalScope.resolveCDType(qualifiedName)).isPresent();

        ASTCDType type = globalScope.resolveCDType(qualifiedName).map(CDTypeSymbol::getAstNode).orElseThrow();
        testCDAttribute(type);
        testCDMethod(type);
    }

    private void testCDAttribute(ASTCDType node){
        CD4CodeVisitor visitor = new CD4CodeVisitor() {
            @Override
            public void visit(ASTCDAttribute attribute){
                if(checkAttribute(node, attribute))
                    attribute.getSymbol().getType().lazyLoadDelegate();
            }
        };
        node.accept(visitor);
    }

    private void testCDMethod(ASTCDType node){
        CD4CodeVisitor visitor = new CD4CodeVisitor() {
            @Override
            public void visit(ASTCDMethod method){
                if(checkReturnType(node, method))
                    method.getSymbol().getReturnType().lazyLoadDelegate();

                for(ASTCDParameter parameter : method.getCDParameterList())
                    if(checkParameter(node, parameter))
                        parameter.getSymbol().getType().lazyLoadDelegate();

                for(CDTypeSymbolSurrogate exceptions : method.getSymbol().getExceptionsList())
                    exceptions.lazyLoadDelegate();
            }
        };
        node.accept(visitor);
    }

    private boolean checkAttribute(ASTCDType type, ASTCDAttribute attribute){
        if(isGeneric(type, attribute.getMCType()))
            return false;
        return !CDGeneratorHelper.isPrimitive(attribute);
    }

    private boolean checkParameter(ASTCDType type, ASTCDParameter parameter){
        if(isGeneric(type, parameter.getMCType()))
            return false;
        return !CDGeneratorHelper.isPrimitive(parameter);
    }

    private boolean checkReturnType(ASTCDType type, ASTCDMethod method){
        if(!method.getMCReturnType().isPresentMCType())
            return false;
        if(isGeneric(type, method.getMCReturnType().getMCType()))
            return false;
        return !CDGeneratorHelper.isPrimitive(method.getMCReturnType().getMCType());
    }

    private boolean isGeneric(ASTCDType type, ASTMCType mcType){
        return type.getSymbol().getStereotype(printer.prettyprint(mcType).split("[<\\[]")[0]).isPresent();
    }
}
