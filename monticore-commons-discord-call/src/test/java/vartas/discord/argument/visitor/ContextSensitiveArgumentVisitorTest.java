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

import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import vartas.chart.interval._ast.ASTInterval;
import vartas.discord.argument._ast.*;
import vartas.discord.call._parser.CallParser;
import vartas.discord.onlinestatus._ast.ASTOnlineStatus;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static vartas.arithmeticexpressions.calculator.ArithmeticExpressionsValueCalculator.valueOf;

@RunWith(Enclosed.class)
public class ContextSensitiveArgumentVisitorTest {
    protected static ASTArgument argument;
    protected static int visits;

    protected static ASTArgument parse(String content) throws IOException {
        CallParser parser = new CallParser();
        return parser.parse_StringArgument(content).get();
    }
    public static class ContextSensitiveTest extends ContextSensitiveArgumentVisitor{
        @Test
        public void testSetRealThis(){
            super.setRealThis(null);
            assertThat(super.realThis).isNull();
        }
        @Test
        public void testGetRealThis(){
            assertThat(super.getRealThis()).isEqualTo(this);
        }
    }

    public static class ContextSensitiveDateTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("22-11-3333");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(3);
        }

        @Override
        public void visit(ASTDateArgumentEntry ast){
            assertThat(valueOf(ast.getDay()).intValueExact()).isEqualTo(22);
            assertThat(valueOf(ast.getMonth()).intValueExact()).isEqualTo(11);
            assertThat(valueOf(ast.getYear()).intValueExact()).isEqualTo(3333);
            ++visits;
        }

        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            assertThat(valueOf(ast.getExpression()).intValueExact()).isEqualTo(22-11-3333);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("22-11-3333");
            ++visits;
        }
    }

    public static class ContextSensitiveIntervalTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("Day");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTIntervalArgumentEntry ast){
            assertThat(ast.getIntervalName().getInterval()).isEqualTo(ASTInterval.DAY);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("Day");
            ++visits;
        }
    }

    public static class ContextSensitiveOnlineStatusTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("Online");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTOnlineStatusArgumentEntry ast){
            assertThat(ast.getOnlineStatusName().getOnlineStatus()).isEqualTo(ASTOnlineStatus.ONLINE);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("Online");
            ++visits;
        }
    }

    public static class ContextSensitiveUserTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("<@12345>");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTUserArgumentEntry ast){
            assertThat(ast.getUser().getId().getValue()).isEqualTo(12345);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("<@12345>");
            ++visits;
        }
    }

    public static class ContextSensitiveRoleTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("<@&12345>");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTRoleArgumentEntry ast){
            assertThat(ast.getRole().getId().getValue()).isEqualTo(12345);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("<@&12345>");
            ++visits;
        }
    }

    public static class ContextSensitiveTextChannelTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("<#12345>");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTTextChannelArgumentEntry ast){
            assertThat(ast.getTextChannel().getId().getValue()).isEqualTo(12345);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("<#12345>");
            ++visits;
        }
    }

    public static class ContextSensitiveExpressionTest extends ContextSensitiveArgumentVisitor{
        @Before
        public void setUp() throws IOException {
            argument = parse("12345");
            visits = 0;
        }

        @Test
        public void testVisit(){
            argument.accept(getRealThis());
            assertThat(visits).isEqualTo(2);
        }

        @Override
        public void handle(ASTExpressionArgumentEntry ast){
            assertThat(valueOf(ast.getExpression()).intValueExact()).isEqualTo(12345);
            ++visits;
        }

        @Override
        public void handle(ASTStringLiteral ast){
            assertThat(ast.getValue()).isEqualTo("12345");
            ++visits;
        }
    }
}
