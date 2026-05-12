package com.sniklz.repelreforged.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.sniklz.repelreforged.RepelReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin 
{
    @Inject(method = "addFreshEntity", at = @At("HEAD"), cancellable = true)
    private void cobblemonrepel$checkRepelOnSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) 
    {
        if (!(entity instanceof PokemonEntity pokemonEntity))
        {
            return;
        }
        if (pokemonEntity.getPokemon().getOwnerUUID() != null) 
        {
            return;
        }
        if (pokemonEntity.getPokemon().getAspects().contains("poke_snack_crumbed")) 
        {
            return;
        }
        ServerLevel level = (ServerLevel) (Object) this;
        if (level.getGameRules().getInt(RepelReforged.REPEL_RANGE) == 0) 
        {
            return;
        }
        BlockPos spawnPos = entity.blockPosition(); 
        if (RepelReforged.isRepelNearby(level, spawnPos)) 
        {
            entity.discard();
            cir.setReturnValue(false);
        }
    }
}