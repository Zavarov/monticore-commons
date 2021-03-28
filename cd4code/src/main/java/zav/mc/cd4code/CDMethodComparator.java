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

package zav.mc.cd4code;

import de.monticore.cd.cd4analysis._ast.ASTCDMethod;
import de.monticore.cd.cd4analysis._ast.ASTCDParameter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCFullGenericTypesPrettyPrinter;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.Comparator;
import java.util.List;

/**
 * A comparator for detecting duplicate methods.<br>
 * We consider two methods to be equal, iff they have the same name
 * and parameter types. However, the return type may differ.<br>
 * e.g. <code>public Integer get()</code> and <code>public String get()</code> will
 * be treated as equivalent, since not every programming language is able to resolve
 * the ambiguity with the return type alone.
 */
public class CDMethodComparator extends ComparatorChain<ASTCDMethod> {

    public CDMethodComparator(){
        super();
        addComparator(new NameComparator());
        addComparator(new ParameterComparator());
    }

    private static class NameComparator implements Comparator<ASTCDMethod> {
        @Override
        public int compare(ASTCDMethod m1, ASTCDMethod m2) {
            return m1.getName().compareTo(m2.getName());
        }
    }

    private static class ParameterComparator implements Comparator<ASTCDMethod>{
        private MCFullGenericTypesPrettyPrinter printer = new MCFullGenericTypesPrettyPrinter(new IndentPrinter());

        @Override
        public int compare(ASTCDMethod m1, ASTCDMethod m2) {
            if(m1.getCDParameterList().size() != m2.getCDParameterList().size())
                return Integer.compare(m1.getCDParameterList().size(), m2.getCDParameterList().size());
            else
                return compare(m1.getCDParameterList(), m2.getCDParameterList());
        }

        private int compare(List<ASTCDParameter> p1, List<ASTCDParameter> p2){
            for(int i = 0 ; i < p1.size() ; ++i)
                //Stop at the first parameters that differ
                if(compare(p1.get(i), p2.get(i)) != 0)
                    return compare(p1.get(i), p2.get(i));
            return 0;
        }

        private int compare(ASTCDParameter p1, ASTCDParameter p2){
            if(p1.deepEquals(p2))
                return 0;
            else
                //TODO Find a better way to compare the parameter
                return printer.prettyprint(p1.getMCType()).compareTo(printer.prettyprint(p2.getMCType()));
        }
    }
}
