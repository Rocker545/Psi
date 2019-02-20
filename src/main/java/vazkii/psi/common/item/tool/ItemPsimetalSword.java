/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [06/02/2016, 21:14:38 (GMT)]
 */
package vazkii.psi.common.item.tool;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.arl.item.ItemModSword;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPsimetalSword extends ItemModSword implements IPsimetalTool, IPsiItem {

	public ItemPsimetalSword() {
		super(LibItemNames.PSIMETAL_SWORD, PsiAPI.PSIMETAL_TOOL_MATERIAL);
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
		super.hitEntity(itemstack, target, attacker);

		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;

			PlayerData data = PlayerDataHandler.get(player);
			ItemStack playerCad = PsiAPI.getPlayerCAD(player);

			if(!playerCad.isEmpty()) {
				ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
				ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F,
						(SpellContext context) -> {
					context.attackedEntity = target;
					context.tool = itemstack;
						});
			}
		}

		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		IPsimetalTool.regen(stack, entityIn, isSelected);
	}
	
	@Override
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
		ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
	}

	@Override
	public boolean getIsRepairable(ItemStack thisStack, @Nonnull ItemStack material) {
		return IPsimetalTool.isRepairableBy(material) || super.getIsRepairable(thisStack, material);
	}
	
	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}

}
