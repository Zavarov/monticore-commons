        if(!message.isFromGuild())
            throw new IllegalStateException("The message needs to be sent inside a guild");