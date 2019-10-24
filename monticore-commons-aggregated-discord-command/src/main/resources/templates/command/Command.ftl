${signature("package", "existsHandwrittenClass", "name", "abstractName")}
<#assign symbol = ast.getCommandSymbol()>
<#assign parameters = symbol.getParameters()>
package ${package};

import vartas.discord.bot.command.*;
import vartas.discord.bot.command.entity._ast.*;
import vartas.discord.bot.command.parameter._symboltable.*;
import vartas.discord.bot.io.guild.*;
import vartas.discord.bot.io.rank.*;
import vartas.discord.bot.api.communicator.*;
import vartas.discord.bot.api.environment.*;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.*;

import org.slf4j.*;

import java.util.*;

public <#if existsHandwrittenClass>abstract </#if>class ${name} extends ${abstractName}{
    ${includeArgs("command.VariableDeclaration", parameters)}

    public ${name}(Message source, CommunicatorInterface communicator, List<ASTEntityType> parameters) throws IllegalArgumentException, IllegalStateException
    {
        super(source, communicator, parameters);
        ${includeArgs("command.VariableInitialization", parameters)}
    }
}