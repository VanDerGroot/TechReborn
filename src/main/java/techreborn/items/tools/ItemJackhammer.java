package techreborn.items.tools;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.utils.OreDictUtils;

import java.util.List;
import java.util.Random;

public class ItemJackhammer extends ItemPickaxe implements IEnergyItemInfo {

	public static int tier = 1;
	public int maxCharge = 1;
	public int cost = 250;
	public double transferLimit = 100;

	public ItemJackhammer(ToolMaterial material, String unlocalizedName, int energyCapacity, int tier) {
		super(material);
		efficiencyOnProperMaterial = 20F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(240);
		setUnlocalizedName(unlocalizedName);
		this.maxCharge = energyCapacity;
		this.tier = tier;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
	                                EntityLivingBase entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			PoweredItem.useEnergy(cost, stack);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		// TODO needs // FIXME: 13/03/2016
		return OreDictUtils.isOre(state, "stone") || state.getBlock().getMaterial(state) == Material.ROCK && PoweredItem.canUseEnergy(cost, null);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if ((OreDictUtils.isOre(state, "stone") || state.getBlock() == Blocks.STONE) && PoweredItem.canUseEnergy(cost, stack)) {
			return efficiencyOnProperMaterial;
		} else {
			return 0.5F;
		}
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
		return true;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return transferLimit;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return tier;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		PoweredItem.setEnergy(getMaxPower(charged), charged);
		itemList.add(charged);
	}

}
