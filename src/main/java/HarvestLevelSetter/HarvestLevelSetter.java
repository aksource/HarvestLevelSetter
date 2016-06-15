package HarvestLevelSetter;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.logging.Logger;

@Mod(modid=HarvestLevelSetter.MOD_ID,
		name=HarvestLevelSetter.MOD_NAME,
        version=HarvestLevelSetter.MOD_VERSION,
        dependencies=HarvestLevelSetter.MOD_DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = HarvestLevelSetter.MOD_MC_VERSION)
public class HarvestLevelSetter {
	public static final String MOD_ID = "HarvestLevelSetter";
	public static final String MOD_NAME = "HarvestLevelSetter";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[11.14.0.1237,)";
	public static final String MOD_MC_VERSION = "[1.8,1.8.9]";
	public static String[] blockPickaxe;
	public static String[] blockShovel;
	public static String[] blockAxe;
	public static String[] toolPickaxe;
	public static String[] toolShovel;
	public static String[] toolAxe;

    public static final Logger LOGGER = Logger.getLogger("HarvestLevelSetter");

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		blockPickaxe = config.get(Configuration.CATEGORY_GENERAL, "blockPickaxe", new String[]{}, "Harvet Pickaxe Block Name List Format(No Space):BlockUniqueName#HarvestLevel#meta or BlockUniqueName,HarvestLevel").getStringList();
		blockShovel = config.get(Configuration.CATEGORY_GENERAL, "blockShovel", new String[]{}, "Harvet Shovel Block Name List Format(No Space):BlockUniqueName,HarvestLevel,meta or BlockUniqueName,HarvestLevel").getStringList();
		blockAxe = config.get(Configuration.CATEGORY_GENERAL, "blockAxe", new String[]{}, "Harvet Axe Block Name List Format(No Space):BlockUniqueName,HarvestLevel,meta or BlockUniqueName,HarvestLevel").getStringList();
		toolPickaxe = config.get(Configuration.CATEGORY_GENERAL, "toolPickaxe", new String[]{}, "Tool Pickaxe Name List Format(No Space):ToolUniqueName,HarvestLevel").getStringList();
		toolShovel = config.get(Configuration.CATEGORY_GENERAL, "toolShovel", new String[]{}, "Tool Shovel Name List Format(No Space):ToolUniqueName,HarvestLevel").getStringList();
		toolAxe = config.get(Configuration.CATEGORY_GENERAL, "toolAxe", new String[]{}, "Tool Axe Name List Format(No Space):ToolUniqueName,HarvestLevel").getStringList();
		config.save();
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		setHarvestLevel(blockPickaxe, toolPickaxe, "pickaxe");
		setHarvestLevel(blockShovel, toolShovel, "shovel");
		setHarvestLevel(blockAxe, toolAxe, "axe");
	}
	public void setHarvestLevel(String[] blocklist, String[] toolList, String toolKind)
	{
		int i;
		Item tool;
		Block block;
		int meta;
		String[] toolNameAndLv;
		String[] blockNameAndLv;
		int lv;
		for(i = 0; i < toolList.length;i++){
			toolNameAndLv = toolList[i].split(",");
			if(toolNameAndLv.length < 2)continue;
			tool = GameData.getItemRegistry().getObject(toolNameAndLv[0]);
			lv = getInt(toolNameAndLv[1]);
			if(tool != null && lv >= 0){
				tool.setHarvestLevel(toolKind, lv);
				FMLLog.fine(String.format("#Tools %s %s %d", tool.getUnlocalizedNameInefficiently(new ItemStack(tool)), toolKind, lv));
			}
		}
		for(i = 0;i < blocklist.length;i++){
			blockNameAndLv = blocklist[i].split(",");
			if(blockNameAndLv.length < 2)continue;
			block = GameData.getBlockRegistry().getObject(blockNameAndLv[0]);
			meta = getInt(blockNameAndLv[2]);
			lv = getInt(blockNameAndLv[1]);
			if(block != null && lv >=0){
				if(blockNameAndLv.length < 3){
					block.setHarvestLevel(toolKind, lv);
					LOGGER.fine(String.format("#Block %s %s %d", block.getLocalizedName(), toolKind, lv));
				}else{
					block.setHarvestLevel(toolKind, lv, block.getStateFromMeta(meta));
					LOGGER.fine(String.format("#Block %s %s %d %d", block.getLocalizedName(), toolKind, lv, meta));
				}
			}
		}
	}
	public int getInt(String str)
	{
		try{
			return Integer.parseInt(str.trim());
		}catch(NumberFormatException e){
			return -1;
		}
	}
}