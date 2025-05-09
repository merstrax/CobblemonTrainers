package com.poketon.cobblemontrainers.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.poketon.cobblemontrainers.CobblemonTrainers;
import com.poketon.cobblemontrainers.trainer.Trainer;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class TrainerGroupSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        CobblemonTrainers.INSTANCE.getTrainerRegistry().getAllTrainers().stream()
            .map(Trainer::getGroup)
            .distinct()
            .forEach(builder::suggest);
        return builder.buildFuture();
    }

}
