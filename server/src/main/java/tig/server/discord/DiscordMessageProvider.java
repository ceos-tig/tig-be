package tig.server.discord;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.discord.event.DiscordFeignApplication;
import tig.server.discord.event.DiscordFeignCancel;
import tig.server.discord.event.DiscordFeignJoin;
import tig.server.member.domain.Member;
import tig.server.reservation.dto.ReservationResponse;

import java.text.ParseException;

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

    public void sendApplicationMessage(EventMessage eventMessage, ReservationResponse reservationResponse) throws ParseException {
        DiscordMessage discordMessage = createApplicationMessage(eventMessage.getMessage(), reservationResponse);
        sendApplicationMessageToDiscord(discordMessage);
    }

    public void sendCancelMessage(EventMessage eventMessage, ReservationResponse reservationResponse) throws ParseException {
        DiscordMessage discordMessage = createCancelMessage(eventMessage.getMessage(), reservationResponse);
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

