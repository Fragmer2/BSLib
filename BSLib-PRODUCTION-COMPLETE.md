# BSLib Production Complete Guide
> Audience: senior plugin maintainers, platform teams, and operators running BSLib-based plugins in shared production environments.
> Scope: this document defines production operating expectations that complement existing API, developer, and reference documentation.
---
## 1. Framework Mental Model
### 1.1 Build features as bounded systems, not isolated classes
BSLib is most effective when each feature is treated as a bounded runtime system with explicit contracts:
- Inputs: commands, events, scheduled triggers, messages, and service calls.
- Processing: state transitions, validation, domain policy, and persistence.
- Outputs: player-visible effects, metrics, logs, messages, and service mutations.
A production plugin should define those boundaries before writing implementation code.
### 1.2 Think in ownership scopes
Every registration in BSLib should have a clear owner scope:
- Plugin-scope resources survive for full plugin runtime.
- Module-scope resources survive while module is enabled.
- Session-scope resources survive while a player/session/menu is active.
- Request-scope resources survive one operation only.
If ownership cannot be named, the design is incomplete.
### 1.3 Lifecycle is the backbone of correctness
In BSLib, lifecycle correctness is not optional polish; it is primary correctness.
- Enable path must be deterministic and idempotent under restart conditions.
- Runtime path must avoid leaking cross-scope references.
- Disable path must reclaim all resources created during runtime.
### 1.4 Treat reload as restart with strict cleanup
Production operators will reload unexpectedly.
Your plugin must assume:
- in-flight tasks may still be running,
- services may be queried during transition,
- other plugins may call capabilities at awkward times.
Design for orderly shutdown despite these realities.
### 1.5 Separate domain logic from framework plumbing
Use BSLib APIs as orchestration surfaces:
- Command handlers should delegate to domain services.
- Menu callbacks should delegate to session services.
- Task bodies should call pure functions when possible.
- Messaging handlers should validate payloads then dispatch.
This separation improves testability and incident triage speed.
### 1.6 Production reasoning model
When designing any subsystem, answer these six questions:
1. Who owns this resource?
2. When is it created?
3. What thread may touch it?
4. How is failure surfaced?
5. How is it observed in production?
6. How is it torn down?
If any answer is ambiguous, do not ship.
### 1.7 Operationally safe defaults
Choose defaults that minimize blast radius:
- deny unknown external message payloads,
- timeout all outbound I/O,
- disable optional features when dependencies disappear,
- fail startup for critical dependency gaps,
- avoid global mutable static registries.
### 1.8 Design for shared ecosystem coexistence
BSLib plugins frequently coexist with unknown third-party code.
Assume:
- global scheduler pressure,
- unpredictable plugin ordering,
- conflicting capability names unless namespaced,
- non-cooperative shutdown behavior from other plugins.
Namespace contracts and guard all cross-plugin boundaries.
---
## 2. Versioning & Compatibility Policy
### 2.1 Semantic version contract
Adopt strict SemVer interpretation for public BSLib-facing plugin APIs:
- MAJOR: incompatible public API or behavioral contract change.
- MINOR: backward-compatible API additions and safe behavior extensions.
- PATCH: backward-compatible fixes, docs, and non-contract internals.
### 2.2 Public API classification
Classify all exported surfaces:
- Stable Public: long-term compatibility target.
- Experimental Public: allowed to change with warning.
- Internal: no compatibility promises.
Use naming and annotation conventions to make classification obvious.
### 2.3 `@Internal` guidance
For members marked `@Internal`:
- do not use from external plugins,
- do not rely on behavior or side effects,
- do not report as breaking if removed.
If operational need forces use, wrap behind your own adapter and pin exact versions.
### 2.4 `@Experimental` guidance
For `@Experimental` APIs:
- require explicit opt-in in your codebase (package-level note or build flag),
- keep call sites isolated to one module,
- add migration budget in each release plan.
### 2.5 Compatibility matrix publication
For each release, publish a matrix:
- BSLib version,
- minimum and maximum tested Paper versions,
- Java runtime baseline,
- known incompatible plugin combinations.
### 2.6 Upgrade strategy for ecosystem users
Recommended path:
1. Read changelog and migration notes.
2. Update staging to target versions.
3. Run lifecycle and integration suite.
4. Canary deploy.
5. Observe for one stability window.
6. Promote globally.
### 2.7 Breaking-change policy
For MAJOR changes:
- announce intent before release,
- provide migration guide with before/after examples,
- supply compatibility shims where feasible,
- define deprecation window if possible.
### 2.8 Behavioral compatibility rules
Even without signature changes, behavioral breaks count as incompatible if they alter:
- lifecycle order expectations,
- ownership enforcement behavior,
- threading guarantees,
- exception propagation contracts,
- scheduling semantics.
### 2.9 Deprecation lifecycle
Minimum deprecation process:
1. Mark deprecated with replacement path.
2. Emit doc warning and changelog entry.
3. Provide at least one MINOR release overlap.
4. Remove in next MAJOR unless security risk requires faster removal.
### 2.10 Plugin-side compatibility adapter pattern
Use adapters for cross-version resilience:
```java
public interface MessageBridge {
    void publishEconomyChanged(UUID playerId, int delta);
}
public final class BslibV1MessageBridge implements MessageBridge {
    @Override
    public void publishEconomyChanged(UUID playerId, int delta) {
        PluginMessageBus.publish("economy/changed", new EconomyChanged(playerId, delta));
    }
}
```
### 2.11 Schema and config compatibility coupling
Version code and config migrations together:
- plugin binary version alone is insufficient,
- config schema version must be checked at startup,
- migration failures should block startup for critical config.
### 2.12 Hotfix policy
PATCH releases may include behavior changes only when all conditions hold:
- issue is production-severity,
- change is smallest viable fix,
- regression tests cover scenario,
- release notes describe risk.
---
## 3. Plugin Lifecycle Contract
### 3.1 Lifecycle timeline
The operational timeline is:
1. Bootstrap (class loading, framework wiring)
2. Enable (resource registration and start)
3. Runtime (steady-state operations)
4. Reload transition (disable + enable sequence)
5. Disable (orderly stop and teardown)
### 3.2 Bootstrap requirements
During bootstrap:
- avoid heavy side effects in constructors,
- avoid static references to runtime objects,
- prepare immutable configuration descriptors only.
### 3.3 Enable phase contract
Enable phase must:
- validate config and dependency assumptions,
- register services/capabilities with owner,
- register and enable modules,
- subscribe to message topics with owner,
- schedule tasks and retain handles,
- emit startup health log.
### 3.4 Runtime contract
During runtime:
- all asynchronous operations must have timeout and error path,
- long-lived resources must expose explicit stop hooks,
- state transitions must be observable via metrics/logging,
- cross-plugin interactions must be validated.
### 3.5 Reload transition contract
Reload transition means:
- stop creating new workload,
- drain or cancel in-flight workload,
- reclaim resources,
- clear owner registrations,
- re-enter enable cleanly.
### 3.6 Disable phase contract
Disable must be deterministic:
1. gate new requests,
2. cancel tasks,
3. disable modules,
4. unsubscribe/destroy reactive/session objects,
5. unregister service/message/capability ownership,
6. release external clients,
7. emit teardown summary.
### 3.7 Safe registration pattern
```java
@Override
public void onFrameworkEnable() {
    validateConfigOrThrow();
    Services.provide(this, EconomyService.class, economyService);
    PluginCapabilityRegistry.provide(this, "economy-api");
    getModuleManager().register(new DatabaseModule());
    getModuleManager().register(new EconomyModule());
    getModuleManager().enableAll();
    this.subscriptions.add(PluginMessageBus.subscribe(this, "shop/purchase", this::onPurchase));
    this.tasks.add(Tasks.async().repeat(20 * 30).runTracked(this::refreshCaches));
}
```
### 3.8 Safe teardown pattern
```java
@Override
public void onFrameworkDisable() {
    this.acceptingRequests.set(false);
    this.tasks.forEach(FrameworkTask::cancel);
    this.subscriptions.forEach(Subscription::unsubscribe);
    this.sessions.values().forEach(PlayerSession::close);
    getModuleManager().disableAll();
    Services.unregisterOwner(this);
    PluginMessageBus.unregisterOwner(this);
    PluginCapabilityRegistry.unregisterOwner(this);
    closeExternalClients();
}
```
### 3.9 Async shutdown rules
- Never block disable forever waiting for async completion.
- Use bounded await durations for graceful async drains.
- After deadline, cancel remaining work and log unfinished units.
- Never touch Bukkit objects from async shutdown threads.
### 3.10 Lifecycle verification checklist
- start and stop plugin 10+ times in staging without growth in tracked task count,
- verify no retained session objects after player disconnect,
- verify service/message/capability registries are empty for plugin owner after disable,
- verify startup fails fast on critical dependency absence.
### 3.11 Failure semantics during enable
If enable fails mid-way:
- best-effort rollback already-created resources,
- call teardown path for partially initialized subsystems,
- mark plugin state as failed and non-operational,
- log exact failed step with cause.
### 3.12 Inter-plugin lifecycle ordering
- Do not assume another plugin is fully ready unless capability check passes.
- Handle dependency disappear/reload by transitioning to degraded mode.
- Revalidate dependency contracts after reload events.
---
## 4. Threading & Concurrency Model
### 4.1 Main thread responsibilities
Main thread is mandatory for:
- world/entity/inventory mutations,
- command execution surfaces unless explicitly offloaded,
- menu interaction callbacks,
- Bukkit event listeners touching game state.
### 4.2 Async thread responsibilities
Async threads are appropriate for:
- database operations,
- HTTP calls,
- file I/O,
- expensive CPU transformations.
### 4.3 Scheduler guarantees and expectations
- `Tasks.sync()` executes on server main thread.
- `Tasks.async()` executes off main thread.
- Repeating tasks must be cancelable and ownership-tracked.
### 4.4 Reactive concurrency expectations
Reactive chains should:
- avoid performing Bukkit-side effects in async callback contexts,
- apply throttling/debouncing on high-frequency streams,
- explicitly destroy derived streams at scope end.
### 4.5 Safe async handoff pattern
```java
Tasks.async().run(() -> {
    PlayerSnapshot snapshot = repository.loadSnapshot(playerId);
    Tasks.sync().run(() -> applySnapshotToPlayer(playerId, snapshot));
});
```
### 4.6 GOOD example: bounded async I/O
```java
CompletableFuture
    .supplyAsync(() -> httpClient.fetchProfile(playerId), ioExecutor)
    .orTimeout(2, TimeUnit.SECONDS)
    .thenAccept(profile -> Tasks.sync().run(() -> updateProfileView(playerId, profile)))
    .exceptionally(ex -> {
        logger.warning("profile fetch failed for " + playerId + ": " + ex.getMessage());
        return null;
    });
```
### 4.7 BAD example: Bukkit API in async task
```java
Tasks.async().run(() -> {
    Player player = Bukkit.getPlayer(playerId);
    player.getInventory().addItem(rewardItem); // BAD: async inventory mutation
});
```
### 4.8 BAD example: unbounded async fanout
```java
for (UUID playerId : onlinePlayers) {
    Tasks.async().run(() -> repository.refresh(playerId)); // BAD: no backpressure/limit
}
```
### 4.9 GOOD example: bounded worker pool
```java
ExecutorService pool = Executors.newFixedThreadPool(8);
Semaphore permits = new Semaphore(32);
for (UUID playerId : onlinePlayers) {
    permits.acquireUninterruptibly();
    CompletableFuture
        .runAsync(() -> repository.refresh(playerId), pool)
        .whenComplete((ok, ex) -> permits.release());
}
```
### 4.10 Forbidden concurrency patterns
- Sharing non-thread-safe mutable objects across sync and async paths.
- Holding locks while calling Bukkit APIs or message bus callbacks.
- Performing blocking network calls inside command handlers.
- Creating fire-and-forget repeating tasks without handles.
### 4.11 Locking policy
Prefer:
- immutable data transfer objects,
- concurrent collections for read-heavy maps,
- fine-grained synchronization around ownership transitions only.
Avoid global locks covering unrelated subsystems.
### 4.12 Queue/backpressure guidance
For high-volume async work:
- use bounded queues,
- reject or degrade when queue is full,
- surface queue depth metric,
- avoid hidden executor growth.
### 4.13 Cancellation model
All long-running tasks should support cancellation checks:
```java
while (!Thread.currentThread().isInterrupted() && running.get()) {
    processBatch();
}
```
### 4.14 Thread-affinity documentation
Document thread affinity for each service method:
- sync-only,
- async-only,
- thread-agnostic.
This prevents accidental misuse by other plugin teams.
---
## 5. Resource Ownership & Isolation
### 5.1 Ownership is required metadata
Every global registration must include owner identity.
No anonymous global mutable state in production.
### 5.2 Service ownership rules
- Services are owned by provider plugin.
- Consumers must not mutate provider internals.
- Owner unregistration must remove all provided entries.
### 5.3 Messaging boundaries
- Topics must be namespaced (`plugin-domain/action`).
- Payload classes should be immutable.
- Consumers must validate payload shape and semantic bounds.
### 5.4 Capability ownership rules
- Capability names should be stable and documented.
- Ownership must prevent hijacking by other plugins.
- Requirement validation should occur before feature activation.
### 5.5 Session resource ownership
Player-bound resources must include explicit close/destroy:
- menu views,
- reactive subscriptions,
- session caches,
- temporary cooldown entries.
### 5.6 Multi-plugin collision prevention
Use namespaced keys:
- Service names: `myplugin:primary`.
- Topic names: `myplugin/economy-changed`.
- Capability names: `myplugin-api.v1`.
### 5.7 GOOD example: owner-safe service registration
```java
Services.provide(this, EconomyFacade.class, "myplugin:default", economyFacade);
```
### 5.8 BAD example: non-owner overwrite attempt
```java
Services.provide(OtherPluginClass.class, economyFacade); // BAD: ambiguous ownership intent
```
### 5.9 Cross-plugin API facade pattern
Expose thin immutable facade interfaces for consumers:
```java
public interface ShopReadApi {
    OptionalInt getBalance(UUID playerId);
    PriceQuote quote(String itemId, int amount);
}
```
### 5.10 Teardown responsibility matrix
- Plugin owner: registry unregistration, executor shutdown, module disable.
- Module owner: module-level listeners/tasks/services.
- Session owner: menu/reactive/task resources tied to user lifecycle.
### 5.11 Leak detection checklist
- tracked tasks count returns to baseline on disable,
- no remaining subscriptions by owner,
- no player/session maps retaining disconnected players,
- no classloader references retained in static fields.
### 5.12 Isolation for optional integrations
Optional integrations must be isolated modules that can disable independently when provider disappears.
---
## 6. Production Deployment Model
### 6.1 Single Paper server deployment
Characteristics:
- low coordination complexity,
- direct local storage is common,
- reload risk still present.
Guidance:
- keep startup under strict target budget,
- run regular restart drills,
- verify teardown does not degrade over repeated cycles.
### 6.2 Multi-world server deployment
Characteristics:
- world-specific configuration and workload,
- uneven event volume across worlds.
Guidance:
- avoid global assumptions tied to one world,
- shard caches by world when needed,
- include world labels in metrics/logs.
### 6.3 Proxy network deployment
Characteristics:
- player movement across nodes,
- distributed consistency concerns.
Guidance:
- treat each node plugin as stateless where feasible,
- externalize shared state to DB/cache/message broker,
- design idempotent cross-node operations.
### 6.4 Clustered server deployment
Characteristics:
- many plugin instances sharing backing services,
- increased need for coordination and rate limits.
Guidance:
- set per-node concurrency quotas,
- use distributed locks sparingly and with timeouts,
- include node-id in all operational telemetry.
### 6.5 Rollout strategies
Supported production rollout patterns:
- blue/green,
- canary percentage rollout,
- ring-based deployment (internal, vip, full).
### 6.6 Rollout checklist
1. Build and sign artifacts.
2. Verify checksum on deploy targets.
3. Deploy staging and run full smoke suite.
4. Enable canary nodes.
5. Observe health metrics for one window.
6. Promote to all nodes.
7. Keep rollback artifact immediately available.
### 6.7 Rollback model
Rollback must include:
- binary rollback,
- config schema rollback plan,
- data migration backward compatibility or restore strategy,
- dependency compatibility validation.
### 6.8 Restart orchestration
In fleets:
- avoid simultaneous restart storms,
- drain players where possible,
- stagger node restarts by service tier.
### 6.9 Node identity and topology awareness
Expose node identity in plugin logs and metrics:
- `node_id`,
- `cluster`,
- `region` (if applicable),
- `build_version`.
### 6.10 Deployment anti-fragility
- rehearse failure drills quarterly,
- keep last two known-good versions packaged,
- automate health-based rollback where possible.
---
## 7. Observability & Metrics
### 7.1 Observability principles
- if it fails, it must be visible,
- if it slows down, it must be measurable,
- if it leaks, it must be attributable to owner.
### 7.2 Minimum required metrics
Collect at least:
- startup duration (ms),
- shutdown duration (ms),
- enabled module count,
- tracked task count,
- async queue depth,
- command latency (p50/p95/p99),
- error count by subsystem,
- message handler failures by topic,
- reactive subscription count,
- dependency health states.
### 7.3 Health indicators
Define health checks:
- `readiness`: plugin can accept new work,
- `liveness`: core loops are operational,
- `degraded`: optional integrations unavailable but core still functioning.
### 7.4 Doctor integration model
Use framework diagnostics as first-class signal:
- export tracked task counts,
- expose module states,
- monitor resource ownership leaks.
### 7.5 Logging philosophy
Logs should be:
- structured where possible,
- machine-searchable,
- contextual enough for incident triage,
- rate-limited for repetitive failures.
### 7.6 Log field conventions
Include fields:
- timestamp,
- severity,
- plugin,
- module,
- operation,
- player_id (if relevant),
- topic/service key,
- correlation_id,
- exception_class,
- message.
### 7.7 Metrics labeling policy
Avoid high-cardinality labels:
- good labels: module, operation type, result status,
- bad labels: full player name, raw stack trace, random IDs.
### 7.8 Alerting model
Define alert classes:
- P1: plugin unavailable or data integrity risk,
- P2: major feature degraded,
- P3: non-critical errors above baseline.
### 7.9 Suggested alert thresholds
- startup failure count > 0 after deploy,
- command error rate > 2% for 5m,
- p95 command latency > 250ms for 10m,
- tracked tasks growing monotonically for 15m,
- repeated topic handler exceptions > threshold.
### 7.10 Traceability across async boundaries
Propagate correlation IDs from command/event ingress to async tasks and message handlers.
### 7.11 Operational dashboards
Create dashboards per plugin with:
- golden signals (latency, errors, throughput, saturation),
- lifecycle events,
- queue and task pressure,
- dependency health.
### 7.12 Audit logging for critical actions
For sensitive operations (economy transfers, admin actions):
- log actor and target,
- log amount/parameters,
- log success/failure and reason,
- store tamper-evident if possible.
---
## 8. Capacity Planning & Performance
### 8.1 Capacity planning inputs
Before launch, estimate:
- peak concurrent players,
- command/menu interaction rates,
- background task frequency,
- expected DB and cache throughput.
### 8.2 Player scaling model
Estimate per-player overhead:
- session object memory,
- reactive subscriptions,
- periodic task participation,
- cache footprint.
### 8.3 Task limit budgeting
Set explicit budgets:
- max repeating tasks per plugin,
- max async concurrent jobs,
- max queue depth before degrade/reject.
### 8.4 Memory expectations
Track memory by category:
- static caches,
- per-player runtime state,
- message backlog,
- serialized payload pools.
### 8.5 Reactive usage limits
Define guardrails:
- max derived reactives per session,
- mandatory `destroy()` on close,
- require throttle/debounce for high-frequency updates.
### 8.6 Performance test matrix
Run:
- baseline idle test,
- peak player simulation,
- burst command storm,
- menu open/close churn,
- dependency slowdown injection,
- repeated reload cycles.
### 8.7 Stress testing methodology
1. Establish baseline metrics.
2. Ramp load gradually.
3. Hold sustained peak for soak duration.
4. Inject failures under load.
5. Verify recovery and leak absence.
### 8.8 Profiling expectations
Use profiling before optimization:
- CPU flamegraphs,
- allocation profiling,
- scheduler timing analysis,
- async queue pressure traces.
### 8.9 Hot path optimization priorities
- reduce object allocation in per-tick code,
- precompute immutable menu items,
- avoid repeated expensive lookups,
- batch small operations where consistency allows.
### 8.10 Capacity warning indicators
Watch for:
- rising GC pause times,
- growing async backlog,
- increasing p99 latencies,
- delayed shutdown completion,
- increasing task count without steady state.
### 8.11 Scaling decisions
Scale out when:
- CPU saturation sustained,
- queue depth exceeds SLO repeatedly,
- latency remains elevated after optimization.
### 8.12 Performance acceptance criteria
Define launch criteria:
- p95 command latency within target under peak,
- zero unbounded queues,
- shutdown within bounded duration,
- no monotonic memory/task growth in soak test.
---
## 9. Failure Modes & Incident Handling
### 9.1 Common failure scenarios
- startup failure due to missing capability,
- dependency timeout storms,
- async thread pool exhaustion,
- reactive subscription leaks,
- module enable ordering mistakes,
- message schema mismatch across plugins.
### 9.2 Degradation strategy tiers
Define tiers:
- Tier 0: full operation,
- Tier 1: optional features disabled,
- Tier 2: read-only mode,
- Tier 3: maintenance mode (critical admin commands only),
- Tier 4: emergency disable.
### 9.3 Emergency shutdown policy
If data integrity risk is detected:
1. stop accepting writes,
2. cancel periodic mutation tasks,
3. flush critical audit logs,
4. disable plugin cleanly,
5. notify operators with incident ID.
### 9.4 Rollback strategy
Rollback steps:
1. choose known-good binary and config schema pair,
2. verify dependency compatibility,
3. restore data snapshot if required,
4. restart with elevated observability,
5. validate golden path operations.
### 9.5 Recovery workflow
- stabilize platform,
- restore service dependencies,
- re-enable plugin in controlled mode,
- replay or reconcile failed operations,
- monitor until error rate baseline returns.
### 9.6 Incident triage checklist
- What changed recently?
- Which subsystem first emitted errors?
- Is failure isolated to one node or fleet-wide?
- Is data corruption possible?
- Is rollback safer than forward fix?
### 9.7 Data integrity safeguards
- use idempotency keys for external writes,
- ensure operation ordering where required,
- avoid partial writes without compensation path,
- log mutation intents before commit when practical.
### 9.8 Timeout and retry policy
- all external calls require timeout,
- retries must be bounded,
- use jitter to prevent thundering herds,
- avoid retrying non-idempotent writes blindly.
### 9.9 Failure mode examples
#### Example: missing capability at startup
- detect via `validateMissing`,
- block startup,
- log missing capability list,
- provide operator remediation path.
#### Example: message payload mismatch
- reject payload,
- increment validation-failure metric,
- keep subsystem alive,
- notify producer plugin maintainers.
### 9.10 Incident communication template
- Incident ID,
- impact summary,
- start time,
- affected features,
- mitigation in progress,
- ETA for next update.
### 9.11 Evidence collection during incidents
Collect:
- logs around first failure,
- task/queue metrics,
- dependency health snapshots,
- plugin version and config hash,
- recent deployment metadata.
### 9.12 Exit criteria for incident closure
Close only when:
- root cause identified or bounded,
- mitigation verified under load,
- monitoring shows stable baseline,
- follow-up tasks assigned with owners and due dates.
---
## 10. Security Model
### 10.1 Trust boundaries
In production, treat all external plugin communication as untrusted input.
Trust boundary exists at:
- incoming messages,
- service calls from other plugins,
- capability declarations,
- config files and operator commands.
### 10.2 Message validation requirements
For every message payload:
- validate type and schema,
- validate value ranges,
- reject unknown enum/state values,
- avoid deserializing arbitrary classes.
### 10.3 Capability abuse prevention
- namespaced capabilities to reduce collision risk,
- verify owner identity where possible,
- avoid granting privileged behavior solely on name presence.
### 10.4 Safe cross-plugin communication
- use immutable DTOs,
- keep payloads minimal,
- include version field for evolving schema,
- handle unknown versions gracefully.
### 10.5 Privilege separation
Separate interfaces:
- read-only API for consumers,
- privileged mutation API for trusted integrations only,
- admin operations gated by permissions.
### 10.6 Secrets management
- do not hardcode credentials,
- load from secure environment sources,
- support rotation without full redeploy when feasible,
- never log secrets.
### 10.7 Input hardening
Harden all external inputs:
- command parameters,
- message payloads,
- config values,
- file paths.
### 10.8 Denial-of-service resilience
- apply rate limits on expensive operations,
- cap async queue sizes,
- debounce high-frequency events,
- reject oversized payloads.
### 10.9 Audit requirements
For security-sensitive actions, retain audit entries with actor identity and outcome.
### 10.10 Secure defaults checklist
- least privilege permissions,
- strict parser modes,
- bounded retries/timeouts,
- disabled optional integrations unless configured.
---
## 11. Configuration Governance
### 11.1 Config schema versioning
Every config must include schema version:
```yaml
schemaVersion: 3
features:
  economy: true
```
### 11.2 Migration strategy
Implement migration steps by version:
- 1 -> 2
- 2 -> 3
- ...
Each step must be deterministic and idempotent.
### 11.3 Unknown key handling policy
Choose one mode per environment:
- strict: fail startup on unknown keys,
- permissive: warn and ignore unknown keys.
Production recommendation: strict for critical sections, permissive for optional extensions.
### 11.4 Validation patterns
Validate:
- ranges and units,
- cross-field constraints,
- required combinations,
- dependency-derived constraints.
### 11.5 Config loading lifecycle
Load config before registering runtime components that depend on it.
Do not partially initialize with invalid config.
### 11.6 Dynamic reload policy
If hot-reload supported:
- define which keys are reloadable,
- reinitialize affected subsystems safely,
- retain old config on validation failure.
### 11.7 Config diff audit logging
On config changes, log:
- changed keys,
- actor/source,
- timestamp,
- validation result.
### 11.8 Secrets in config
If secrets must be referenced:
- store references, not plaintext,
- resolve through secret provider at runtime,
- redact in logs and diagnostics.
### 11.9 Config test fixtures
Maintain fixture set:
- valid minimal config,
- valid full config,
- invalid schemas,
- migration edge cases.
### 11.10 Backward compatibility in config
When renaming keys:
- support old key for one deprecation window,
- emit warning with migration hint,
- remove only in major release.
---
## 12. Dependency & Ecosystem Design
### 12.1 Dependency declaration model
Use layered dependency types:
- hard dependency (plugin must exist),
- soft dependency (optional integration),
- capability dependency (runtime contract).
### 12.2 Capability negotiation
Startup negotiation flow:
1. declare provided capabilities,
2. declare required capabilities,
3. validate missing requirements,
4. choose full/degraded mode accordingly.
### 12.3 Optional dependency modules
Implement optional integrations as isolated modules:
- module enables only when provider available,
- module disables when provider unavailable,
- core plugin remains functional where possible.
### 12.4 Degraded startup modes
Define explicit degraded modes:
- no-economy mode,
- no-permissions enhancement mode,
- no-cross-server sync mode.
Each mode should document disabled features.
### 12.5 Cross-plugin API contracts
Publish contracts with:
- interface signatures,
- threading expectations,
- failure semantics,
- version compatibility guarantees.
### 12.6 Dependency health monitoring
Track per-dependency:
- availability,
- response time,
- error rate,
- last successful interaction.
### 12.7 Conflict management
When two plugins provide same capability:
- require explicit operator choice if ambiguity exists,
- fail safe rather than picking arbitrary provider,
- log deterministic selection rules.
### 12.8 Compatibility shims
Use shims for popular ecosystem dependencies with unstable APIs.
Keep shim code isolated from core domain logic.
### 12.9 Ecosystem contract testing
Run integration tests against representative third-party plugin versions before release.
### 12.10 Dependency removal handling
At runtime, if dependency disappears:
- transition dependent features to degraded mode,
- cancel related tasks/subscriptions,
- notify operators,
- recover automatically when dependency returns if safe.
---
## 13. Testing Strategy
### 13.1 Testing pyramid for BSLib plugins
- Unit tests for domain logic and validators.
- Component tests for services/modules.
- Integration tests for lifecycle and cross-plugin interactions.
- Soak/stress tests for performance and leak detection.
### 13.2 API logic tests
Test pure logic without Bukkit runtime dependency whenever possible.
### 13.3 Async system tests
Verify:
- timeout behavior,
- retry boundaries,
- cancellation correctness,
- thread-affinity rules.
### 13.4 Lifecycle correctness tests
Automate repeated enable/disable cycles and assert:
- zero leftover tasks,
- zero leftover subscriptions,
- zero leftover owner services/capabilities.
### 13.5 Fake runtime testing
Use fake adapters for scheduler/message/service registry where feasible to validate orchestration deterministically.
### 13.6 Integration tests in Paper environment
Include tests for:
- command execution paths,
- menu open/update/close lifecycle,
- module dependency ordering,
- capability negotiation with peer plugins.
### 13.7 Regression test policy
Every production incident should produce at least one deterministic regression test.
### 13.8 Contract tests for public APIs
For exposed cross-plugin APIs, maintain contract tests for:
- success cases,
- invalid inputs,
- version compatibility constraints,
- threading misuse guardrails.
### 13.9 Test data governance
- isolate fixtures by version,
- include migration fixtures,
- avoid secrets in fixtures,
- reset external dependencies between tests.
### 13.10 Example: lifecycle leak test pseudocode
```java
@Test
void repeatedEnableDisable_hasNoResourceLeak() {
    for (int i = 0; i < 25; i++) {
        plugin.onFrameworkEnable();
        plugin.onFrameworkDisable();
    }
    assertEquals(0, diagnostics.trackedTaskCount(plugin));
    assertEquals(0, diagnostics.subscriptionCount(plugin));
    assertEquals(0, diagnostics.serviceCount(plugin));
}
```
### 13.11 Example: async timeout test pseudocode
```java
@Test
void externalCall_timesOutAndDegradesGracefully() {
    fakeHttp.delay(5, TimeUnit.SECONDS);
    Result result = service.performExternalLookup(playerId);
    assertEquals(Result.DEGRADED, result.state());
    assertTrue(metrics.timeoutCount() > 0);
}
```
### 13.12 Pre-release test suite minimum
- full unit suite,
- lifecycle integration suite,
- async resilience suite,
- config migration suite,
- canary smoke checklist.
---
## 14. Release Engineering
### 14.1 Release checklist
Before tagging release:
- changelog updated,
- migration notes updated,
- compatibility matrix updated,
- docs updated for behavior changes,
- test suite green,
- artifacts reproducible.
### 14.2 CI expectations
CI should run:
- compile + static analysis,
- unit tests,
- integration tests,
- compatibility checks,
- packaging verification.
### 14.3 Binary compatibility checks
Use `japicmp` (or equivalent) for public API compatibility gate:
- fail build on unintended binary break,
- allow documented approved breaks for MAJOR only.
### 14.4 Suggested japicmp workflow
1. Compare previous release artifact with current build.
2. Review incompatible changes.
3. Classify intentional vs accidental.
4. Block release on accidental breaks.
### 14.5 Release gates
Mandatory gates:
- no P1/P2 open regressions,
- compatibility check passed,
- migration docs approved,
- canary validation complete,
- rollback package prepared.
### 14.6 Artifact integrity
- generate checksums,
- sign artifacts,
- verify signatures before deployment.
### 14.7 Branch and tagging policy
- release branches immutable except approved hotfixes,
- signed tags for production releases,
- tag includes compatibility metadata.
### 14.8 Documentation gate
No release if:
- behavior changed without docs update,
- deprecated API lacks migration path,
- production runbook impact undocumented.
### 14.9 Roll-forward vs rollback decision policy
- roll-forward if fix is low risk and validated quickly,
- rollback if data integrity or wide outage risk exists.
### 14.10 Release communication
Publish:
- what changed,
- who is affected,
- migration actions required,
- known issues,
- rollback instructions.
---
## 15. Operational Runbooks
### 15.1 Runbook: task growth/leak issues
Symptoms:
- tracked task count continuously rises,
- shutdown delays increase,
- repeated tasks continue after feature disabled.
Actions:
1. Identify task types and owners.
2. Verify all repeating tasks retain handles.
3. Inspect disable path cancellation coverage.
4. Add guard metrics per task family.
5. Patch and validate via repeated lifecycle test.
### 15.2 Runbook: reactive leak issues
Symptoms:
- memory growth tied to player churn,
- stale UI updates after session close,
- subscription count does not return to baseline.
Actions:
1. Trace creation sites for derived reactives.
2. Ensure `destroy()` called on close/quit/disable.
3. Ensure `unsubscribe()` for temporary listeners.
4. Add session close assertions in tests.
5. Validate with player connect/disconnect soak test.
### 15.3 Runbook: module enable failure
Symptoms:
- startup aborts at module stage,
- missing dependency warning logs,
- partial system initialization.
Actions:
1. Capture failed module ID and exception.
2. Check dependency graph declarations.
3. Verify provider service/capability readiness.
4. Roll back partial init via teardown path.
5. Fix module ordering or dependency checks.
### 15.4 Runbook: messaging handler failures
Symptoms:
- handler exception spikes by topic,
- downstream cache inconsistency,
- elevated command/menu failures.
Actions:
1. Isolate failing topic and payload class.
2. Validate schema version compatibility.
3. Add strict payload validation and rejection counters.
4. Degrade dependent features if topic unusable.
5. Coordinate fix with producer plugin maintainers.
### 15.5 Runbook: startup failure
Symptoms:
- plugin fails enable,
- missing capability or invalid config errors,
- incompatible dependency version.
Actions:
1. Read first cause log line (not only final stack trace).
2. Verify config schema/version and migration status.
3. Verify dependency/capability availability.
4. Re-run with debug diagnostics enabled.
5. Restore last known-good build if unresolved.
### 15.6 Runbook: async pool saturation
Symptoms:
- timeout spikes,
- growing queue depth,
- delayed feature responses.
Actions:
1. Check queue depth and worker utilization.
2. Identify slow external dependencies.
3. Reduce concurrency and apply backpressure.
4. Enable degraded mode for optional features.
5. Tune pool sizes after profiling.
### 15.7 Runbook: data inconsistency alarms
Symptoms:
- mismatch between cache and source-of-truth,
- duplicate operations,
- missing audit entries.
Actions:
1. Freeze write path if integrity at risk.
2. Compare operation logs and idempotency keys.
3. Reconcile state from source-of-truth.
4. Patch ordering/idempotency bug.
5. Add regression test covering scenario.
### 15.8 Runbook: hot reload instability
Symptoms:
- reload causes duplicate subscriptions/tasks,
- stale class references,
- degraded performance after multiple reloads.
Actions:
1. Execute repeated reload test cycle.
2. Verify disable cleanup for every resource type.
3. Remove static runtime references.
4. Enforce owner unregistration checks.
5. Recommend restart if plugin cannot guarantee reload safety.
### 15.9 Runbook: dependency outage
Symptoms:
- external DB/cache unavailable,
- cascading failures in dependent modules.
Actions:
1. Transition to degraded/read-only mode.
2. Pause non-essential background tasks.
3. Increase timeout visibility, not timeout values blindly.
4. Recover dependency and warm caches gradually.
5. Return to full mode after stability verification.
### 15.10 Runbook maintenance policy
- review runbooks every release,
- update after each incident,
- store with versioned operational docs,
- validate by rehearsal in staging.
---
## 16. Post-Incident Process
### 16.1 Incident review goals
- understand systemic causes,
- prevent recurrence,
- improve detection and mitigation speed.
### 16.2 Post-incident template
- Incident ID:
- Date/time:
- Duration:
- Severity:
- User impact:
- Services/features affected:
- Detection method:
- Timeline of events:
- Root cause:
- Contributing factors:
- Immediate mitigation:
- Permanent corrective actions:
- Owners and due dates:
- Verification plan:
### 16.3 Root cause analysis guidance
Avoid blaming individuals.
Analyze:
- design gaps,
- process gaps,
- test coverage gaps,
- observability gaps,
- release control gaps.
### 16.4 Corrective action tracking
Each action must include:
- owner,
- deadline,
- measurable success criterion,
- priority based on risk reduction.
### 16.5 Prevention tracking categories
Track actions across categories:
- detection,
- containment,
- remediation,
- validation,
- documentation.
### 16.6 Learning review cadence
- perform formal review for all P1/P2 incidents,
- summarize recurring patterns quarterly,
- feed outcomes into roadmap and release gates.
### 16.7 Incident KPI suggestions
Measure:
- MTTD (mean time to detect),
- MTTR (mean time to recover),
- recurrence rate,
- percentage of incidents with regression tests added.
---
## 17. Production Plugin Template
### 17.1 Goal
This template demonstrates production-minded structure for a BSLib plugin.
It is illustrative and should be adapted to your domain.
### 17.2 Template code
```java
@AutoScan
public final class EconomyPlatformPlugin extends FrameworkPlugin {
    private final AtomicBoolean acceptingRequests = new AtomicBoolean(false);
    private final List<FrameworkTask> tasks = new CopyOnWriteArrayList<>();
    private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
    private final ConcurrentMap<UUID, PlayerSession> sessions = new ConcurrentHashMap<>();
    private ExecutorService ioPool;
    private Config config;
    private DatabaseClient database;
    private MetricsFacade metrics;
    @Override
    public void onFrameworkEnable() {
        long startNanos = System.nanoTime();
        this.config = loadAndValidateConfigOrThrow();
        this.ioPool = Executors.newFixedThreadPool(config.ioThreads());
        this.database = DatabaseClient.connect(config.database());
        this.metrics = new MetricsFacade(getLogger(), config.metrics());
        registerCapabilities();
        validateRequiredCapabilitiesOrThrow();
        registerServices();
        registerModules();
        registerMessaging();
        registerTasks();
        this.acceptingRequests.set(true);
        this.metrics.recordStartup(Duration.ofNanos(System.nanoTime() - startNanos));
        getLogger().info("EconomyPlatformPlugin started");
    }
    @Override
    public void onFrameworkDisable() {
        long startNanos = System.nanoTime();
        this.acceptingRequests.set(false);
        // Stop periodic workload first.
        this.tasks.forEach(FrameworkTask::cancel);
        // Stop player/session resources.
        this.sessions.values().forEach(PlayerSession::close);
        this.sessions.clear();
        // Stop message handlers.
        this.subscriptions.forEach(Subscription::unsubscribe);
        this.subscriptions.clear();
        // Disable modules before unregistering registries.
        getModuleManager().disableAll();
        // Registry teardown.
        Services.unregisterOwner(this);
        PluginMessageBus.unregisterOwner(this);
        PluginCapabilityRegistry.unregisterOwner(this);
        // External resources last.
        if (this.database != null) {
            this.database.close();
        }
        if (this.ioPool != null) {
            this.ioPool.shutdownNow();
        }
        if (this.metrics != null) {
            this.metrics.recordShutdown(Duration.ofNanos(System.nanoTime() - startNanos));
        }
        getLogger().info("EconomyPlatformPlugin stopped");
    }
    private void registerCapabilities() {
        PluginCapabilityRegistry.provide(this, "economy-platform-api.v1", "economy-platform-events.v1");
        PluginCapabilityRegistry.require(this, "permissions", "database");
    }
    private void validateRequiredCapabilitiesOrThrow() {
        List<String> missing = PluginCapabilityRegistry.validateMissing(this);
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing capabilities: " + missing);
        }
    }
    private void registerServices() {
        Services.provide(this, EconomyReadApi.class, "economy-platform:read", new EconomyReadApiImpl(database));
        Services.provide(this, EconomyWriteApi.class, "economy-platform:write", new EconomyWriteApiImpl(database));
        Services.provideLazy(this, SessionFactory.class, "economy-platform:sessions", () -> new SessionFactory(this));
    }
    private void registerModules() {
        getModuleManager().register(new PersistenceModule(database, metrics));
        getModuleManager().register(new EconomyCoreModule(metrics));
        getModuleManager().register(new UiModule(metrics));
        getModuleManager().enableAll();
    }
    private void registerMessaging() {
        subscriptions.add(PluginMessageBus.subscribe(this, "economy-platform/balance-changed.v1", message -> {
            if (!(message instanceof BalanceChanged payload)) {
                metrics.increment("msg.invalid_payload");
                return;
            }
            if (payload.delta() == 0) {
                return;
            }
            metrics.increment("msg.balance_changed");
            onBalanceChanged(payload);
        }));
    }
    private void registerTasks() {
        tasks.add(Tasks.async().repeat(20 * 30).runTracked(this::refreshHotCaches));
        tasks.add(Tasks.sync().repeat(20 * 60).runTracked(this::emitHeartbeat));
        tasks.add(Tasks.async().delay(40).runTracked(this::warmup));
    }
    public void openEconomyMenu(Player player) {
        if (!acceptingRequests.get()) {
            player.sendMessage("System is not ready");
            return;
        }
        PlayerSession session = sessions.computeIfAbsent(player.getUniqueId(), id -> PlayerSession.create(this, id));
        session.openMenu(player);
    }
    public void closeSession(UUID playerId) {
        PlayerSession session = sessions.remove(playerId);
        if (session != null) {
            session.close();
        }
    }
    private void refreshHotCaches() {
        if (!acceptingRequests.get()) {
            return;
        }
        CompletableFuture
            .runAsync(() -> database.refreshCaches(), ioPool)
            .orTimeout(2, TimeUnit.SECONDS)
            .whenComplete((ok, ex) -> {
                if (ex != null) {
                    metrics.increment("cache.refresh.error");
                    getLogger().warning("cache refresh failed: " + ex.getMessage());
                } else {
                    metrics.increment("cache.refresh.ok");
                }
            });
    }
    private void emitHeartbeat() {
        metrics.gauge("sessions.active", sessions.size());
        metrics.gauge("tasks.tracked", tasks.size());
    }
    private void warmup() {
        try {
            database.primeConnectionPool();
            metrics.increment("warmup.ok");
        } catch (Exception ex) {
            metrics.increment("warmup.error");
            getLogger().warning("warmup failed: " + ex.getMessage());
        }
    }
    private void onBalanceChanged(BalanceChanged payload) {
        PlayerSession session = sessions.get(payload.playerId());
        if (session == null) {
            return;
        }
        Tasks.sync().run(() -> session.applyBalance(payload.newBalance()));
    }
    private Config loadAndValidateConfigOrThrow() {
        Config loaded = ConfigLoader.load(getDataFolder().toPath().resolve("config.yml"));
        ConfigValidator.validateOrThrow(loaded);
        return loaded;
    }
    // --- API contracts used by other plugins ---
    public interface EconomyReadApi {
        OptionalInt getBalance(UUID playerId);
    }
    public interface EconomyWriteApi {
        WriteResult addBalance(UUID playerId, int amount, String reason);
    }
    public record WriteResult(boolean success, String code) {}
    public record BalanceChanged(UUID playerId, int delta, int newBalance) {}
}
```
### 17.3 Template operational notes
- All mutable runtime resources have explicit shutdown paths.
- All cross-plugin surfaces are namespaced and typed.
- Async work is bounded and failure-aware.
- Session resources are scoped and reclaimable.
---
## 18. Anti-Patterns
### 18.1 Lifecycle anti-patterns
- Registering resources without owner context.
- Forgetting `unregisterOwner(...)` calls on disable.
- Side effects in module constructors instead of `onEnable`.
### 18.2 Concurrency anti-patterns
- Blocking I/O in sync command/menu handlers.
- Async direct Bukkit API mutation.
- Unbounded thread creation and queue growth.
### 18.3 Ownership anti-patterns
- Static maps holding player/session references indefinitely.
- Sharing mutable DTOs between plugins.
- Overwriting registry keys belonging to another plugin.
### 18.4 Messaging anti-patterns
- Global topics with ambiguous semantics.
- Payloads lacking schema/version identifier.
- No handler validation and no error metrics.
### 18.5 Config anti-patterns
- No schema version in config.
- Silent ignore of invalid required keys.
- Runtime reload of non-reloadable settings without safeguards.
### 18.6 Release anti-patterns
- Shipping without compatibility checks.
- Releasing behavior changes without migration notes.
- Deploying directly to full fleet without canary.
### 18.7 Incident anti-patterns
- Ad-hoc fixes without regression tests.
- Closing incidents without root-cause analysis.
- No follow-up ownership for corrective actions.
---
## 19. Framework Guarantees
### 19.1 What BSLib guarantees (when used correctly)
BSLib provides framework-level guarantees for:
- owner-aware registration surfaces,
- structured lifecycle hooks,
- scheduler abstractions for sync/async intent,
- module enable/disable orchestration,
- messaging and capability primitives,
- diagnostic surfaces for operational visibility.
### 19.2 What BSLib does not guarantee
BSLib does not guarantee:
- correctness of plugin business logic,
- thread safety of your custom services,
- data consistency for external systems,
- automatic recovery from dependency outages,
- security of unvalidated cross-plugin payloads.
### 19.3 Developer responsibilities
Plugin developers must guarantee:
- proper ownership and teardown discipline,
- thread-safe implementation where required,
- input validation and secure boundaries,
- timeout/retry/backpressure in external I/O,
- comprehensive testing and release governance.
### 19.4 Operator responsibilities
Operators must guarantee:
- controlled rollout and rollback processes,
- monitoring and alerting in production,
- backup and restore readiness,
- incident response and post-incident follow-up.
### 19.5 Shared responsibility model summary
- Framework provides primitives and guardrails.
- Plugin authors implement safe domain behavior.
- Operators provide disciplined production operations.
Production reliability emerges only when all three are fulfilled.
---
## Appendix A. Production Readiness Audit Worksheet
Use this worksheet before major releases.
### A.1 Lifecycle
- [ ] Enable sequence deterministic.
- [ ] Disable sequence reclaims all resources.
- [ ] Reload tested repeatedly without growth trends.
### A.2 Concurrency
- [ ] No sync path blocking I/O.
- [ ] No async Bukkit mutations.
- [ ] Bounded async queues and pool sizes configured.
### A.3 Ownership
- [ ] Service registrations owner-aware.
- [ ] Messaging subscriptions owner-aware.
- [ ] Capability declarations validated at startup.
### A.4 Observability
- [ ] Golden metrics available.
- [ ] Alert thresholds defined.
- [ ] Logs include operational context fields.
### A.5 Security
- [ ] External payload validation enforced.
- [ ] Secrets are redacted and externally managed.
- [ ] Privileged actions audited.
### A.6 Release engineering
- [ ] Compatibility check pass (including japicmp policy).
- [ ] Migration notes complete.
- [ ] Canary completed and reviewed.
### A.7 Runbooks
- [ ] Startup failure runbook validated.
- [ ] Task/reactive leak runbooks validated.
- [ ] Incident review template current.
---
## Appendix B. Example SLO Set
These are sample values; calibrate per ecosystem scale.
- Startup success rate: 99.9% per deploy.
- Startup completion time: p95 < 8 seconds.
- Command latency: p95 < 120ms, p99 < 250ms.
- Command error rate: < 1% over rolling 15m.
- Message handler failure rate: < 0.5% over rolling 15m.
- Graceful shutdown completion: p95 < 5 seconds.
- Post-reload leak delta: zero net growth over 20 cycles.
---
## Appendix C. Example Release Gate Scorecard
Assign pass/fail per item:
1. API compatibility checks passed.
2. Migration docs reviewed by maintainers.
3. Lifecycle stress suite green.
4. Soak test completed at target load.
5. Canary incident-free for stability window.
6. Rollback rehearsed on staging.
7. Runbooks updated for changed subsystems.
8. Post-release monitoring owner assigned.
Only release when all gates pass.
