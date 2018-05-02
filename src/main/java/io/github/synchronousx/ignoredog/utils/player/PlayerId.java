package io.github.synchronousx.ignoredog.utils.player;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerId {
    private UUID id;
    private String name;

    public PlayerId() {
    }

    public PlayerId(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }

    public PlayerId(final String idString, final String name) {
        this(PlayerUtils.getUuidFromString(idString), name);
    }

    public Optional<UUID> getId() {
        return Optional.ofNullable(this.id);
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public void setId(final String idString) {
        this.setId(PlayerUtils.getUuidFromString(idString));
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PlayerId) {
            final PlayerId playerId = (PlayerId) obj;
            return this.getId().equals(playerId.getId()) && this.getName().equals(playerId.getName());
        }

        return false;
    }
}
