package vartas.discord.bot.exec.example;

command @ example{
    test @ guild{
             class : TestCommand
        permission : administrator, manage messages
         parameter : g:guild, d:date
              rank : root, dev
    }

    date @ guild{
             class : DateCommand
         parameter : d:date
    }

    expression @ guild{
             class : ExpressionCommand
         parameter : e:expression
    }

    guild @ guild{
             class : GuildCommand
         parameter : g:guild
    }

    interval @ guild{
             class : IntervalCommand
         parameter : i:interval
    }

    member @ guild{
             class : MemberCommand
         parameter : m:member
    }

    message @ guild{
             class : MessageCommand
         parameter : m:message
    }

    onlinestatus @ guild{
             class : OnlineStatusCommand
         parameter : o:onlinestatus
    }

    role @ guild{
             class : RoleCommand
         parameter : r:role
    }

    string @ guild{
             class : StringCommand
         parameter : s:string
    }

    textchannel @ guild{
             class : TextChannelCommand
         parameter : t:textchannel
    }

    user @ guild{
             class : UserCommand
         parameter : u:user
    }
}