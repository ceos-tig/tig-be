package tig.server.discord;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tig.server.discord.event.DiscordFeignApplication;
import tig.server.discord.event.DiscordFeignCancel;
import tig.server.discord.event.DiscordFeignJoin;

import static tig.server.discord.DiscordMessage.*;

@Component
@RequiredArgsConstructor
public class DiscordMessageProvider {

    private final DiscordFeignApplication discordFeignApplication;
    private final DiscordFeignJoin discordFeignJoin;
    private final DiscordFeignCancel discordFeignCancel;


    public void sendJoinMessage(EventMessage eventMessage) {
        DiscordMessage discordMessage = createJoinMessage(eventMessage.getMessage());
        sendJoinMessageToDiscord(discordMessage);
    }

    public void sendApplicationMessage(EventMessage eventMessage, String memberName, String clubName) {
        DiscordMessage discordMessage = createApplicationMessage(eventMessage.getMessage(), memberName, clubName);
        sendApplicationMessageToDiscord(discordMessage);
    }

    public void sendCancelMessage(EventMessage eventMessage, String memberName, String clubName) {
        DiscordMessage discordMessage = createCancelMessage(eventMessage.getMessage(), memberName, clubName);
        sendCancelMessageToDiscord(discordMessage);
    }


    private void sendJoinMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordFeignJoin.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new RuntimeException("ErrorMessage: INVALID_DISCORD_MESSAGE");
        }
    }

    private void sendApplicationMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordFeignApplication.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new RuntimeException("ErrorMessage: INVALID_DISCORD_MESSAGE");
        }
    }

    private void sendCancelMessageToDiscord(DiscordMessage discordMessage) {
        try {
            discordFeignCancel.sendMessage(discordMessage);
        } catch (FeignException e) {
            throw new RuntimeException("ErrorMessage: INVALID_DISCORD_MESSAGE");
        }
    }

}

