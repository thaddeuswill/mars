package mars.selftest;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

/**
 * Dev-only self-test harness (docs/tech/test-strategy.md 2.2): a step log on stdout, a boot guard,
 * a watchdog, and a pass/fail tally that sets the exit code. Active only with {@code
 * -Dmars.selftest=true}. Static state here is a deliberate exception to the "no static mutable
 * state" rule: it is a process-wide test harness, never active in normal play.
 */
public final class SelfTest {
  private static final boolean ENABLED = Boolean.getBoolean("mars.selftest");
  private static final long START = System.nanoTime();
  private static final AtomicReference<String> LAST_STEP = new AtomicReference<>("jvm_start");
  private static final AtomicLong LAST_STEP_NANOS = new AtomicLong(System.nanoTime());
  private static final AtomicInteger PASSED = new AtomicInteger();
  private static final AtomicInteger FAILED = new AtomicInteger();
  private static final AtomicReference<String> FIRST_FAIL = new AtomicReference<>();
  private static volatile boolean serverStarted;

  private SelfTest() {}

  public static boolean enabled() {
    return ENABLED;
  }

  /** Record a lifecycle or test step. Printed as {@code MARS-SELFTEST STEP <id> t=<s>}. */
  public static void step(String id) {
    if (!ENABLED) {
      return;
    }
    LAST_STEP.set(id);
    LAST_STEP_NANOS.set(System.nanoTime());
    out("STEP " + id + " t=" + secondsSinceStart() + "s");
  }

  /** Record a check result, printing what was expected and what was read. */
  public static boolean check(String id, String expected, String read, boolean ok) {
    step("check:" + id);
    out(
        (ok ? "PASS" : "FAIL")
            + " check="
            + id
            + " expected=\""
            + expected
            + "\" read=\""
            + read
            + "\"");
    if (ok) {
      PASSED.incrementAndGet();
    } else {
      FAILED.incrementAndGet();
      FIRST_FAIL.compareAndSet(null, id);
    }
    return ok;
  }

  /** Print an informational line that is not a pass/fail check. */
  public static void info(String id, String text) {
    if (ENABLED) {
      out("INFO " + id + " " + text);
    }
  }

  public static void installIfEnabled(IEventBus modBus) {
    if (!ENABLED) {
      return;
    }
    long bootTimeout = Long.getLong("mars.selftest.bootTimeoutSec", 240);
    long stallTimeout = Long.getLong("mars.selftest.stallTimeoutSec", 120);
    Thread guard = new Thread(() -> guardLoop(bootTimeout, stallTimeout), "mars-selftest-watchdog");
    guard.setDaemon(true);
    guard.start();
    out("boot guard on: boot timeout " + bootTimeout + " s, stall timeout " + stallTimeout + " s");

    modBus.addListener((FMLCommonSetupEvent e) -> step("common_setup"));
    NeoForge.EVENT_BUS.addListener((ServerStartingEvent e) -> step("server_starting"));
    NeoForge.EVENT_BUS.addListener(
        (ServerStartedEvent e) -> {
          serverStarted = true;
          step("server_started");
          if (Boolean.getBoolean("mars.selftest.autostop")) {
            // Dedicated-server self-test: run the boot checks, then stop the server cleanly.
            BootChecks.run(e.getServer(), true);
            step("autostop_requested");
            e.getServer().halt(false);
          }
        });
    NeoForge.EVENT_BUS.addListener(
        (LevelEvent.Load e) -> {
          if (e.getLevel() instanceof net.minecraft.server.level.ServerLevel sl) {
            step("level_loaded:" + sl.dimension().location());
          }
        });
    NeoForge.EVENT_BUS.addListener((ServerStoppingEvent e) -> step("server_stopping"));
    NeoForge.EVENT_BUS.addListener((ServerStoppedEvent e) -> finish());
  }

  private static void finish() {
    step("server_stopped");
    int p = PASSED.get();
    int f = FAILED.get();
    String first = FIRST_FAIL.get();
    if (f == 0 && p > 0) {
      out("RESULT PASS passed=" + p + " failed=0");
    } else {
      out(
          "RESULT FAIL passed="
              + p
              + " failed="
              + f
              + (first != null ? " first_failing_check=" + first : " reason=no_checks_ran"));
      System.out.flush();
      Runtime.getRuntime().halt(5);
    }
  }

  private static void guardLoop(long bootTimeoutSec, long stallTimeoutSec) {
    while (true) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        return;
      }
      if (!serverStarted && secondsSinceStart() > bootTimeoutSec) {
        fail("boot_guard", 3);
      }
      long stalled = (System.nanoTime() - LAST_STEP_NANOS.get()) / 1_000_000_000L;
      if (serverStarted && stalled > stallTimeoutSec) {
        fail("watchdog", 4);
      }
    }
  }

  private static void fail(String what, int code) {
    out(
        "FAIL "
            + what
            + " last_step="
            + LAST_STEP.get()
            + " elapsed="
            + secondsSinceStart()
            + "s exit="
            + code);
    System.out.flush();
    Runtime.getRuntime().halt(code);
  }

  private static long secondsSinceStart() {
    return (System.nanoTime() - START) / 1_000_000_000L;
  }

  private static void out(String line) {
    System.out.println("MARS-SELFTEST " + line);
  }
}
