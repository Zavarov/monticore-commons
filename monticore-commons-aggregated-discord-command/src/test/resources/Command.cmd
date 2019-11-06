package vartas.discord.command.example;
prefix example;

command {
    test @ Guild{
             class : "TestCommand"
        permission : Administrator, Manage Messages
         parameter : g:Guild, d:Date
              rank : Root, Dev
    }

    date @ Guild{
             class : "DateCommand"
         parameter : d:Date
    }

    expression @ Guild{
             class : "ExpressionCommand"
         parameter : e:Expression
    }

    guild @ Guild{
             class : "GuildCommand"
         parameter : g:Guild
    }

    interval @ Guild{
             class : "IntervalCommand"
         parameter : i:Interval
    }

    member @ Guild{
             class : "MemberCommand"
         parameter : m:Member
    }

    message @ Guild{
             class : "MessageCommand"
         parameter : m:Message
    }

    onlinestatus @ Guild{
             class : "OnlineStatusCommand"
         parameter : o:Onlinestatus
    }

    role @ Guild{
             class : "RoleCommand"
         parameter : r:Role
    }

    string @ Guild{
             class : "StringCommand"
         parameter : s:String
    }

    textchannel @ Guild{
             class : "TextChannelCommand"
         parameter : t:Textchannel
    }

    user @ Guild{
             class : "UserCommand"
         parameter : u:User
    }

    many @ Guild{
             class : "ManyCommand"
         parameter : s:String, e:String*
    }
}