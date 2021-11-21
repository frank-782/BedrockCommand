package org.swallowmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class StickListener implements Listener {
    FloodgateApi floodgate = FloodgateApi.getInstance();
    private final BedrockCommand plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (floodgate.isFloodgatePlayer(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "小提示：您可以通过使用" +
                    ChatColor.GOLD + "木棍" +
                    ChatColor.GREEN + "来执行命令 或者 将\"/\"换成\".\"");
            return;
        }
    }

    public StickListener(BedrockCommand plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String prefix = ".";
        if (event.getMessage().startsWith(prefix)) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(plugin, () -> event.getPlayer().performCommand(event.getMessage().replace(prefix, "")));
        }
    }

    @EventHandler
    public void onClickStick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getMaterial() == Material.STICK && floodgate.isFloodgatePlayer(player.getUniqueId())) {
            floodgate.sendForm(player.getUniqueId(), 
                CustomForm.builder()
                    .title("执行命令")
                    .label("通过此表单，你可以在基岩版中执行命令\n" + 
                        "常用命令：\n"+
                        "res tp <领地名>   传送到指定领地\n"+
                        "cp  修改账号密码\n"+
                        "musicbox   打开音乐盒\n"+
                        "tpa <玩家>   请求传送到指定玩家")
                    .input("命令", "请输入要执行的命令 不需要带“/”")
                    .responseHandler((form,responseData) -> {
                        CustomFormResponse response = form.parseResponse(responseData);
                        if (!response.isCorrect()) {
                            return;
                        }
                        String userCommand = response.getInput(1);
                        player.performCommand(userCommand);
                    })
                    .build()
            );
        }
        
    }
}