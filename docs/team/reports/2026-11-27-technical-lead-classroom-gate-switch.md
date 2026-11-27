# Classroom gate switch

## Handoffs received

The owner accepted the Monitor plot and chose to keep the Creative bypass by default, with a classroom switch in Stage 3.

## Done

The server config option `creativeGateBypass` defaults to true. Setting it false makes Creative placement of gate-locked machines require the same gate as Survival.

The Linux Gradle build passed formatting and 174 unit tests. The GameTest runner reported `RESULT PASS passed=82 failed=0`. The placement test used the same Creative player and block context with the switch on and off. It read `canPlace=true` when enabled and `canPlace=false` when disabled. The Survival check passed after Dome stable latched.

## Decisions needed

None.

## Handoffs

Configuration is documented in the launch guide. Use the settings file generated for the server or local game instance.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Placement policy | Creative bypass or Survival unlock | GameTest with the option in both states and after Dome stable | Passed |
| Server config | Option missing at startup | Boot GameTests and inspect generated server TOML | Passed |

## Shared model updates

The switch controls Creative player placement. Operator commands such as `/setblock` retain their existing behavior.
