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
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.expressionsbasis._ast.ExpressionsBasisNodeFactory;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.literals.mccommonliterals._ast.MCCommonLiteralsNodeFactory;
import vartas.arithmeticexpressions._ast.ArithmeticExpressionsNodeFactory;
import vartas.arithmeticexpressions.calculator.ExpressionsBasisValueCalculator;
import vartas.discord.argument._ast.*;
import vartas.discord.argument._visitor.ArgumentVisitor;


public class ContextSensitiveArgumentVisitor implements ArgumentVisitor {
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
    public void handle(ASTDateArgument ast){
        //xx-xx-xxxx can both be a date and an arithmetic expression
        ArgumentVisitor.super.handle(ast);

        ASTExpressionArgument argument = ArgumentNodeFactory.createASTExpressionArgument();
        ASTMinusExpression m1 = ArithmeticExpressionsNodeFactory.createASTMinusExpression();
        ASTMinusExpression m2 = ArithmeticExpressionsNodeFactory.createASTMinusExpression();
        ASTExpression day = ast.getDay();
        ASTExpression month = ast.getMonth();
        ASTExpression year = ast.getYear();

        m1.setLeft(day);
        m1.setRight(month);

        m2.setLeft(m1);
        m2.setRight(year);
        argument.setExpression(m2);
        ArgumentVisitor.super.handle(argument);
    }

    @Override
    public void handle(ASTOnlineStatusArgument ast){
        //the name of the online status is also an English word
        ArgumentVisitor.super.handle(ast);
        handle(ast.getOnlineStatus().getName());
    }

    @Override
    public void handle(ASTIntervalArgument ast){
        //the name of the interval is also an English word
        ArgumentVisitor.super.handle(ast);
        handle(ast.getInterval().getName());
    }

    public void handle(String value){
        ASTStringLiteral literal = MCCommonLiteralsNodeFactory.createASTStringLiteral();
        ASTStringArgument argument = ArgumentNodeFactory.createASTStringArgument();

        literal.setSource(value);
        argument.setStringLiteral(literal);
        ArgumentVisitor.super.handle(argument);
    }

    @Override
    public void handle(ASTRawTextArgument ast){
        String text = ast.getText();
        switch(text){
            case ExpressionsBasisValueCalculator.PI:
            case ExpressionsBasisValueCalculator.E:
                ASTNameExpression expression = ExpressionsBasisNodeFactory.createASTNameExpression();
                ASTExpressionArgument argument = ArgumentNodeFactory.createASTExpressionArgument();

                expression.setName(text);
                argument.setExpression(expression);
                ArgumentVisitor.super.handle(argument);
            //Always treat the raw text as a string
            default:
                handle(text);
        }
    }
}
