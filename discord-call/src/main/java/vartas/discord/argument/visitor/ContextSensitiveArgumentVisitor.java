/*
 * Copyright (c) 2019 Zavarov
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

package vartas.discord.argument.visitor;

import de.monticore.expressions.commonexpressions._ast.ASTMinusExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._ast.MCCommonLiteralsNodeFactory;
import de.monticore.prettyprint.IndentPrinter;
import org.apache.commons.lang3.StringUtils;
import vartas.arithmeticexpressions._ast.ArithmeticExpressionsNodeFactory;
import vartas.arithmeticexpressions.prettyprint.ArithmeticExpressionsPrettyPrinter;
import vartas.discord.argument._ast.*;
import vartas.discord.argument._visitor.ArgumentInheritanceVisitor;
import vartas.discord.argument._visitor.ArgumentVisitor;
import vartas.discord.entity.prettyprinter.EntityPrettyPrinter;

import java.util.Locale;

/**
 * This class solves the conflict of ambiguous calls.<br>
 * For example, a date could be interpreted as a date, a (raw) string or an arithmetic expression
 * in which case, all branches are visited.
 */
public class ContextSensitiveArgumentVisitor implements ArgumentInheritanceVisitor {
    protected ArithmeticExpressionsPrettyPrinter expressionsPrinter = new ArithmeticExpressionsPrettyPrinter(new IndentPrinter());
    protected EntityPrettyPrinter entityPrinter = new EntityPrettyPrinter(new IndentPrinter());
    protected ArgumentVisitor realThis = this;

    @Override
    public void setRealThis(ArgumentVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public ArgumentVisitor getRealThis(){
        return realThis;
    }

    @Override
    public void handle(ASTIntervalArgument node){
        node.getIntervalArgumentEntry().accept(getRealThis());
        handleAsString(node.getIntervalArgumentEntry());
    }

    private void handleAsString(ASTIntervalArgumentEntry node){
        String name = node.getIntervalName().getName();
        name = name.toLowerCase(Locale.ENGLISH);
        name = StringUtils.capitalize(name);
        handleAsRawString(name);
    }

    @Override
    public void handle(ASTOnlineStatusArgument node){
        node.getOnlineStatusArgumentEntry().accept(getRealThis());
        handleAsString(node.getOnlineStatusArgumentEntry());
    }

    private void handleAsString(ASTOnlineStatusArgumentEntry node){
        String name = node.getOnlineStatusName().getName();
        name = name.toLowerCase(Locale.ENGLISH);
        name = StringUtils.capitalize(name);
        handleAsRawString(name);
    }

    @Override
    public void handle(ASTDateArgument node){
        node.getDateArgumentEntry().accept(getRealThis());
        handleAsString(node.getDateArgumentEntry());
        handleAsExpression(node.getDateArgumentEntry());
    }

    private void handleAsString(ASTDateArgumentEntry node){
        StringBuilder builder = new StringBuilder();
        builder.append(expressionsPrinter.prettyprint(node.getDay()));
        builder.append("-");
        builder.append(expressionsPrinter.prettyprint(node.getMonth()));
        builder.append("-");
        builder.append(expressionsPrinter.prettyprint(node.getYear()));
        handleAsRawString(builder.toString());
    }

    private void handleAsExpression(ASTDateArgumentEntry node){
        ASTExpressionArgumentEntry target = ArgumentNodeFactory.createASTExpressionArgumentEntry();
        ASTMinusExpression root = ArithmeticExpressionsNodeFactory.createASTMinusExpression();
        ASTMinusExpression child = ArithmeticExpressionsNodeFactory.createASTMinusExpression();
        ASTExpression day = node.getDay();
        ASTExpression month = node.getMonth();
        ASTExpression year = node.getYear();

        child.setLeft(day);
        child.setRight(month);

        root.setLeft(child);
        root.setRight(year);

        target.setExpression(root);

        target.accept(getRealThis());
    }

    @Override
    public void handle(ASTUserArgument node){
        node.getUserArgumentEntry().accept(getRealThis());
        handleAsString(node.getUserArgumentEntry());
    }

    public void handleAsString(ASTUserArgumentEntry node){
        String name = entityPrinter.prettyprint(node.getUser());
        handleAsRawString(name);
    }

    @Override
    public void handle(ASTTextChannelArgument node){
        node.getTextChannelArgumentEntry().accept(getRealThis());
        handleAsString(node.getTextChannelArgumentEntry());
    }

    public void handleAsString(ASTTextChannelArgumentEntry node){
        String name = entityPrinter.prettyprint(node.getTextChannel());
        handleAsRawString(name);
    }

    @Override
    public void handle(ASTRoleArgument node){
        node.getRoleArgumentEntry().accept(getRealThis());
        handleAsString(node.getRoleArgumentEntry());
    }

    public void handleAsString(ASTRoleArgumentEntry node){
        String name = entityPrinter.prettyprint(node.getRole());
        handleAsRawString(name);
    }

    @Override
    public void handle(ASTExpressionArgument node){
        node.getExpressionArgumentEntry().accept(getRealThis());
        handleAsString(node.getExpressionArgumentEntry());
    }

    public void handleAsString(ASTExpressionArgumentEntry node){
        String name = expressionsPrinter.prettyprint(node.getExpression());
        handleAsRawString(name);
    }

    private void handleAsRawString(String string){
        ASTStringLiteral target = MCCommonLiteralsNodeFactory.createASTStringLiteral();
        target.setSource(StringUtils.deleteWhitespace(string));
        target.accept(getRealThis());
    }
}
