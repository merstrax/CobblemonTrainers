package com.poketon.cobblemontrainers.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.BattleStartError;
import com.cobblemon.mod.common.battles.BattleStartResult;
import com.cobblemon.mod.common.battles.ErroredBattleStart;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.StrongBattleAI;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.poketon.cobblemontrainers.CobblemonTrainers;
import com.poketon.cobblemontrainers.battle.TrainerMaximumLevelError;
import com.poketon.cobblemontrainers.battle.TrainersNotDefeatedError;
import com.poketon.cobblemontrainers.trainer.EntityBackerTrainerBattleActor;
import com.poketon.cobblemontrainers.trainer.Trainer;
import com.poketon.cobblemontrainers.trainer.TrainerBattleListener;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class PokemonUtility {
    public static final Set<UUID> IN_TRAINER_BATTLE = new HashSet<>();

    private static BattleStartResult createTrainerBattle(ServerPlayerEntity player, Trainer trainer, LivingEntity trainerEntity, BattleFormat battleFormat) {
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        UUID leadingPokemon = null;
        for (Pokemon pokemon : party) {
            if (!pokemon.isFainted()) {
                leadingPokemon = pokemon.getUuid();
                break;
            }
        }

        BattleActor playerActor = new PlayerBattleActor(player.getUuid(), party.toBattleTeam(false, true, leadingPokemon));
        BattleAI battleAI = new StrongBattleAI(5);
        BattleActor trainerActor = trainerEntity == null ?
                new TrainerBattleActor(trainer.getName(), UUID.randomUUID(), trainer.getBattleTeam(), battleAI) :
                new EntityBackerTrainerBattleActor(trainer.getName(), trainerEntity, UUID.randomUUID(), trainer.getBattleTeam(), battleAI, player.getPos());
        ErroredBattleStart errors = new ErroredBattleStart();
        Set<BattleStartError> playerErrors = errors.getParticipantErrors().get(playerActor);

        for(Pokemon pokemon : party) {
            if (pokemon.getLevel() > trainer.getPartyMaximumLevel()) {
                playerErrors.add(new TrainerMaximumLevelError(trainer.getPartyMaximumLevel()));
                break;
            }
        }

        List<String> notDefeatedTrainers = trainer.getDefeatRequiredTrainers().stream().filter((mustDefeat) -> !CobblemonTrainers.INSTANCE.getTrainerWinTracker().hasBeaten(player, mustDefeat)).toList();
        if (!notDefeatedTrainers.isEmpty()) {
            playerErrors.add(new TrainersNotDefeatedError(notDefeatedTrainers));
        }

        if (playerActor.getPokemonList().size() < battleFormat.getBattleType().getSlotsPerActor()) {
            playerErrors.add(BattleStartError.Companion.insufficientPokemon(
                    player,
                    battleFormat.getBattleType().getSlotsPerActor(),
                    playerActor.getPokemonList().size()
            ));
        }

        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            playerErrors.add(BattleStartError.Companion.alreadyInBattle(player));
        }

        if (trainer.getTeamSize() == 0) {
            playerErrors.add(entity -> Text.literal("Trainer " + trainer.getName() + " has no Pokémon."));
        }

        return errors.isEmpty() ? Cobblemon.INSTANCE.getBattleRegistry().startBattle(BattleFormat.Companion.getGEN_9_SINGLES(), new BattleSide(playerActor), new BattleSide(trainerActor), false) : errors;
    }

    public static void startTrainerBattle(ServerPlayerEntity player, Trainer trainer, LivingEntity trainerEntity) {
        if (IN_TRAINER_BATTLE.contains(player.getUuid())) return;
        if (trainer.canOnlyBeatOnce() && CobblemonTrainers.INSTANCE.getTrainerWinTracker().hasBeaten(player, trainer)) {
            player.sendMessage(Text.literal(Formatting.RED + "You have already beaten this trainer!"));
        }else {
            long cooldownMillis = CobblemonTrainers.INSTANCE.getTrainerCooldownTracker().remainingCooldownMillis(player, trainer);
            if (cooldownMillis > 0) {
                player.sendMessage(Text.literal(Formatting.RED + "You can't battle this trainer for another " + (cooldownMillis / 1000) + " seconds"));
            }else {
                PokemonUtility.createTrainerBattle(player, trainer, trainerEntity, BattleFormat.Companion.getGEN_9_SINGLES()).ifErrored(error -> {
                    error.sendTo(player, t -> t);
                    return Unit.INSTANCE;
                }).ifSuccessful(battle -> {
                    CobblemonTrainers.INSTANCE.getTrainerCooldownTracker().onBattleStart(player, trainer);
                    TrainerBattleListener.getInstance().addOnBattleVictory(battle, trainer);
                    TrainerBattleListener.getInstance().addOnBattleLoss(battle, trainer.getLossCommand());
                    IN_TRAINER_BATTLE.add(player.getUuid());
                    return Unit.INSTANCE;
                });
            }
        }
    }

}
