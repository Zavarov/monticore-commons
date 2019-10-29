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

package vartas.discord.aggregated.parameter.visitor;

import vartas.discord.aggregated.generator.CommandGeneratorHelper;
import vartas.discord.parameter._ast.*;
import vartas.discord.parameter._visitor.ParameterVisitor;

import java.util.Optional;

public class ParameterTypeVisitor implements ParameterVisitor {
    protected String qualifiedName;
    protected ParameterVisitor realThis = this;

    @Override
    public ParameterVisitor getRealThis(){
        return realThis;
    }

    public Optional<String> accept(ASTParameter ast){
        qualifiedName = null;
        ast.accept(getRealThis());
        return Optional.ofNullable(qualifiedName);
    }

    @Override
    public void setRealThis(ParameterVisitor realThis){
        this.realThis = realThis;
    }

    @Override
    public void visit(ASTMessageParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_MESSAGE;
    }

    @Override
    public void visit(ASTUserParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_USER;
    }

    @Override
    public void visit(ASTTextChannelParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_TEXTCHANNEL;
    }

    @Override
    public void visit(ASTStringParameter ast){
        qualifiedName = CommandGeneratorHelper.JAVA_STRING;
    }

    @Override
    public void visit(ASTRoleParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_ROLE;
    }

    @Override
    public void visit(ASTOnlineStatusParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_ONLINESTATUS;
    }

    @Override
    public void visit(ASTMemberParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_MEMBER;
    }

    @Override
    public void visit(ASTIntervalParameter ast){
        qualifiedName = CommandGeneratorHelper.CHART_INTERVAL;
    }

    @Override
    public void visit(ASTGuildParameter ast){
        qualifiedName = CommandGeneratorHelper.DISCORD_GUILD;
    }

    @Override
    public void visit(ASTExpressionParameter ast){
        qualifiedName = CommandGeneratorHelper.MONTICORE_EXPRESSION;
    }

    @Override
    public void visit(ASTDateParameter ast){
        qualifiedName = CommandGeneratorHelper.JAVA_DATE;
    }
}
