package com.donutesp;

import java.util.Set;

public final class ModHider {

    private ModHider() {}

    public static final Set<String> HIDDEN_IDS = Set.of("donutesp");

    public static final Set<String> BLOCKED_CHANNELS = Set.of(
            "fabric:registry/sync/id_map",
            "fabric:registry/sync/count",
            "fabric:container/open",
            "forge:handshake",
            "fml:handshake",
            "packet-limiter:check",
            "servux:metadata",
            "minihud:metadata"
    );

    public static final String FAKE_BRAND = "vanilla";
}
