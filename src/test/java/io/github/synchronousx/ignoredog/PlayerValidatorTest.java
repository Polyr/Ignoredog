package io.github.synchronousx.ignoredog;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.synchronousx.ignoredog.utils.player.PlayerId;
import io.github.synchronousx.ignoredog.utils.request.ErrorResponse;
import io.github.synchronousx.ignoredog.utils.request.RequestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class PlayerValidatorTest {
    private static final Set<PlayerId> PLAYER_IDS = ConcurrentHashMap.newKeySet();
    private static final IgnoredogMod MOD = new IgnoredogMod();

    @BeforeAll
    static void initAll() {
        PlayerValidatorTest.PLAYER_IDS.add(new PlayerId(UUID.fromString("15fffb7e-57c6-4b70-bbc3-a42dddaf0f81"), "Synchronous"));
        PlayerValidatorTest.PLAYER_IDS.add(new PlayerId(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"));
        PlayerValidatorTest.PLAYER_IDS.add(new PlayerId(UUID.nameUUIDFromBytes("NonexistentPlayer".getBytes(StandardCharsets.UTF_8)), "NonexistentPlayer"));
        PlayerValidatorTest.PLAYER_IDS.add(new PlayerId(UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6"), "jeb_"));
    }

    @Test
    void testPlayerValidator() {
        final CompletableFuture<Void> blocker = new CompletableFuture<>();
        final SimpleImmutableEntry<Set<PlayerId>, List<Future<HttpResponse<JsonNode>>>> requestResponsePair = PlayerValidatorTest.MOD.getPlayerValidator().requestPlayerIds(PlayerValidatorTest.PLAYER_IDS, new Callback<JsonNode>() {
            @Override
            public void completed(final HttpResponse<JsonNode> response) {
                PlayerId[] playerIds = null;
                ErrorResponse errorResponse = null;

                try {
                    playerIds = RequestUtils.JACKSON_OBJECT_MAPPER.readValue(response.getBody().toString(), PlayerId[].class);
                } catch (final MismatchedInputException e) {
                    try {
                        errorResponse = RequestUtils.JACKSON_OBJECT_MAPPER.readValue(response.getBody().toString(), ErrorResponse.class);
                    } catch (final IOException e1) {
                        blocker.complete(null);
                    }
                } catch (final IOException e) {
                    blocker.complete(null);
                }

                if (Optional.ofNullable(playerIds).isPresent()) {
                    Arrays.stream(playerIds).forEach(playerId -> Assertions.assertTrue(PLAYER_IDS.remove(playerId)));
                    try {
                        Assertions.assertEquals(1, PlayerValidatorTest.PLAYER_IDS.size());
                    } catch (final AssertionFailedError e) {
                        blocker.complete(null);
                        throw e;
                    }
                } else {
                    try {
                        Assertions.assertNotNull(errorResponse);
                    } catch (final AssertionFailedError e) {
                        blocker.complete(null);
                        throw e;
                    }
                }

                blocker.complete(null);
            }

            @Override
            public void failed(final UnirestException ignored) {
                blocker.complete(null);
            }

            @Override
            public void cancelled() {
                blocker.complete(null);
            }
        });

        try {
            Assertions.assertEquals(1, requestResponsePair.getValue().size());
        } catch (final AssertionFailedError e) {
            blocker.complete(null);
            throw e;
        }

        try {
            blocker.get();
        } catch (final InterruptedException | ExecutionException e) {
            blocker.complete(null);
        }
    }
}
