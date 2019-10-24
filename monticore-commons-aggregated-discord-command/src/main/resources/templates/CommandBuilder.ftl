${signature("asts", "package")}
package ${package};

import de.monticore.symboltable.*;
import net.dv8tion.jda.core.entities.*;
import vartas.discord.bot.api.command.*;
import vartas.discord.bot.api.communicator.*;
import vartas.discord.bot.command.*;
import vartas.discord.bot.command.call.*;
import vartas.discord.bot.command.call._ast.*;
import vartas.discord.bot.command.command._symboltable.*;
import vartas.discord.bot.command.entity._ast.*;

import java.util.*;
import java.util.function.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommandBuilder extends AbstractCommandBuilder{
    protected Map<String, BiFunction<Message, List<ASTEntityType>, AbstractCommand>> commands = new HashMap<>();

    public CommandBuilder(GlobalScope scope, CommunicatorInterface communicator){
        super(scope, communicator);
<#list asts as ast>
    <#assign commandPackage = helper.getPackage(ast)>
    <#list ast.getCommandList() as command>
        <#assign symbol = command.getCommandSymbol()>
        <#assign name = symbol.getFullName()>
        <#assign className = symbol.getClassName()>
        commands.put("${name}", (context, parameter) -> new ${commandPackage}.${className}(context, communicator, parameter));
    </#list>
</#list>
    }

    @Override
    public AbstractCommand build(String content, Message source) {
        checkNotNull(content);
        checkNotNull(source);

        ASTCallArtifact artifact = CallHelper.parse(scope, content);

        return commands.get(artifact.getQualifiedName()).apply(source, artifact.getParameterList());
    }
}