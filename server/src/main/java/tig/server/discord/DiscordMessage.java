package tig.server.discord;

public record DiscordMessage(
        String content
) {
    public static DiscordMessage createApplicationMessage(String message, String memberName, String clubName) {
        return new DiscordMessage("\n--------------------------------------------\n| "
                                    + message + "\n| " + memberName + " 님이 " + clubName + " 업체를 예약했습니다.\n"
                                    + "--------------------------------------------\n");
    }

    public static DiscordMessage createCancelMessage(String message, String memberName, String clubName) {
        return new DiscordMessage("\n--------------------------------------------\n| "
                + message + "\n| " + memberName + " 님이 " + clubName + " 업체예약을 취소했습니다.\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createJoinMessage(String message) {
        return new DiscordMessage(message);
    }
}
