package tig.server.discord.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tig.server.discord.DiscordMessage;

@FeignClient(name = "${discord.name-application}", url = "${webhooks.webhook-1}")
public interface DiscordFeignApplication {
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(@RequestBody DiscordMessage discordMessage);
}

