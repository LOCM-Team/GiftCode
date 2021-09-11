package me.phuongaz.giftcode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import me.phuongaz.giftcode.command.AdminCommand;
import me.phuongaz.giftcode.command.GiftCodeCommand;

public class GiftCodeLoader extends PluginBase{

    private Map<String, GiftCode> giftcodes = new HashMap<>();
    private static GiftCodeLoader _instance;
    private Config dataconfig;

    @Override
    public void onEnable(){
        _instance = this;
        saveDefaultConfig();
        loadCodes();
        File datafile = new File(getDataFolder(), "playerdata.yml");
        this.dataconfig = new Config(datafile);
        getServer().getCommandMap().register("GiftCode", new AdminCommand());
        getServer().getCommandMap().register("GiftCode", new GiftCodeCommand());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    public static GiftCodeLoader getInstance(){
        return _instance;
    }

    public void saveGiftCode(GiftCode giftcode){
        getConfig().set(giftcode.getGiftcode() + ".commands", giftcode.getRewards());
        getConfig().save();
        getConfig().reload();
        this.loadCodes();
    }

    public Map<String, GiftCode> getGiftCodes(){
        return this.giftcodes;
    }

    public void loadData(){
        File datafile = new File(getDataFolder(), "playerdata.yml");
        this.dataconfig = new Config(datafile);  
    }

    public void loadCodes(){
        getConfig().getAll().forEach((code, cpn) -> {
            GiftCode giftcode = new GiftCode(code);
            giftcode.setRewards(getConfig().getStringList(code + ".commands"));
            this.giftcodes.put(code, giftcode);
        });
    }

    public GiftCode getGiftCodeByString(String code){
        if(this.isGiftCode(code)){
            return giftcodes.get(code);
        }
        return null;
    }

    public boolean isUse(Player player, GiftCode giftcode){
        if(this.dataconfig.exists(player.getName() + "." + giftcode.getGiftcode())){
            return true;
        }
        return false;
    }

    public boolean isGiftCode(String code){
        return giftcodes.containsKey(code);
    }

    public void setData(Player player, GiftCode giftcode){
        this.dataconfig.set(player.getName(), giftcode.getGiftcode());
        this.dataconfig.save();
        this.loadData();
    }

}