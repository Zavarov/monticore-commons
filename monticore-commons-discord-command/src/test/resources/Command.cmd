package "vartas.discord.bot.exec.example"
prefix "example"

command {
    "test" @ guild, attachment{
             class : "TestCommand"
        permission : administrator, manage messages
         parameter : g:guild, d:date
              rank : root, dev
    }
}