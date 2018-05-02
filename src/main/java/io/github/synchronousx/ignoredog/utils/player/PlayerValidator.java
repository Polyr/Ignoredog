package io.github.synchronousx.ignoredog.utils.player;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.synchronousx.ignoredog.IgnoredogMod;
import io.github.synchronousx.ignoredog.utils.Logger;
import io.github.synchronousx.ignoredog.utils.player.PlayerUtils.AccountType;
import io.github.synchronousx.ignoredog.utils.request.ErrorResponse;
import io.github.synchronousx.ignoredog.utils.request.RequestUtils;

import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PlayerValidator {
    private static final int MAX_PLAYERS_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private static final String[] JSON_CONTENT_HEADER = new String[] {
            "Content-Type",
            "application/json"
    };

    private final Map<PlayerId, AccountType> playerCache = new ConcurrentHashMap<>();
    private final IgnoredogMod mod;

    public PlayerValidator(final IgnoredogMod mod) {
        this.mod = mod;
        Unirest.setObjectMapper(RequestUtils.UNIREST_OBJECT_MAPPER);
    }

    public void cachePlayerIds(final Collection<PlayerId> playerIds) {
        final CompletableFuture<Void> responseProcessingBlocker = new CompletableFuture<>();
        final CompletableFuture<Boolean> postResponseProcessingBlocker = new CompletableFuture<>();
        final SimpleImmutableEntry<Set<PlayerId>, List<Future<HttpResponse<JsonNode>>>> requestResponsePair = this.requestPlayerIds(playerIds, new Callback<JsonNode>() {
            @Override
            public void completed(final HttpResponse<JsonNode> response) {
                try {
                    responseProcessingBlocker.get();
                } catch (final InterruptedException | ExecutionException ignored) {
                }

                PlayerId[] playerIdArray = null;
                ErrorResponse errorResponse = null;

                try {
                    playerIdArray = RequestUtils.JACKSON_OBJECT_MAPPER.readValue(response.getRawBody(), PlayerId[].class);
                } catch (final MismatchedInputException exception) {
                    try {
                        errorResponse = RequestUtils.JACKSON_OBJECT_MAPPER.readValue(response.getRawBody(), ErrorResponse.class);
                    } catch (final IOException ignored) {
                    }
                } catch (final IOException ignored) {
                }

                Optional.ofNullable(playerIdArray).ifPresent(presentPlayerIdArray -> {
                    Arrays.stream(presentPlayerIdArray).forEach(playerId -> playerCache.put(playerId, AccountType.EXISTENT));

                    postResponseProcessingBlocker.complete(true);
                    return;
                });

                postResponseProcessingBlocker.complete(false);
            }

            @Override
            public void failed(final UnirestException ignored) {
                postResponseProcessingBlocker.complete(false);
            }

            @Override
            public void cancelled() {
                postResponseProcessingBlocker.complete(false);
            }
        });

        requestResponsePair.getKey().forEach(playerId -> this.playerCache.put(playerId, AccountType.REQUESTED));
        responseProcessingBlocker.complete(null);

        postResponseProcessingBlocker.thenAccept(result -> {
            if (result) {
                requestResponsePair.getKey().stream().filter(playerId -> {
                    if (this.mod.sendDebugMessages()) {
                        Logger.log(Logger.translateAmpersandFormatting("Cached &d" + playerId.getName().orElse("") + "&r."));
                    }

                    return Optional.ofNullable(this.playerCache.get(playerId)).orElse(AccountType.REQUESTED) == AccountType.REQUESTED;
                }).forEach(playerId -> this.playerCache.put(playerId, AccountType.NONEXISTENT));
            } else {
                requestResponsePair.getKey().forEach(this.playerCache::remove);
            }
        });
    }

    public SimpleImmutableEntry<Set<PlayerId>, List<Future<HttpResponse<JsonNode>>>> requestPlayerIds(final Collection<PlayerId> playerIds, final Callback<JsonNode> callback) {
        final Set<PlayerId> playerIdSet = playerIds.stream().filter(playerId -> playerId.getName().isPresent() && !playerId.getName().get().isEmpty() && !this.playerCache.containsKey(playerId)).collect(Collectors.toSet());
        final SimpleImmutableEntry<Set<PlayerId>, List<Future<HttpResponse<JsonNode>>>> requestResponsePair = new SimpleImmutableEntry<>(new HashSet<>(), new ArrayList<>());

        if (!playerIdSet.isEmpty()) {
            for (int i = 0; ; ++i) {
                playerIdSet.stream().skip(PlayerValidator.MAX_PLAYERS_PER_REQUEST * i).limit(PlayerValidator.MAX_PLAYERS_PER_REQUEST).forEach(playerId -> {
                    requestResponsePair.getKey().add(playerId);
                    if (this.mod.sendDebugMessages()) {
                        Logger.log(Logger.translateAmpersandFormatting("Requested &d" + playerId.getName().orElse("") + "&r."));
                    }
                });

                requestResponsePair.getValue().add(Unirest.post(PlayerValidator.PROFILE_URL).header(PlayerValidator.JSON_CONTENT_HEADER[0], PlayerValidator.JSON_CONTENT_HEADER[1]).body(requestResponsePair.getKey().stream().map(PlayerId::getName).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet())).asJsonAsync(callback));

                if (playerIdSet.stream().skip(MAX_PLAYERS_PER_REQUEST * (i + 1)).count() == 0) {
                    break;
                }
            }
        }

        return requestResponsePair;
    }

    public Map<PlayerId, AccountType> getPlayerCache() {
        return this.playerCache;
    }
}
