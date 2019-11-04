package vartas.discord.bot.exec.example;
prefix example;

command {
    test @ Guild, Attachment{
             class : "TestCommand"
        permission : Administrator, Manage Messages
         parameter : g:Guild, d:Date
              rank : Root, Dev
    }
}