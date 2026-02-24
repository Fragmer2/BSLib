# BSLib Reference

## 1. Framework Overview
BSLib is a framework API for Paper plugin development that standardizes runtime lifecycle, modular composition, service discovery, tracked scheduling, reactive state propagation, and cross-plugin contracts. This document is the advanced source-of-truth reference for plugin developers consuming `bslib-api`.

---

## 2. Package Index
### `io.github.fragmer2.bslib.api`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.animation`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.annotation`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.button`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.capability`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.command`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.config`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.cooldown`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.debug`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.di`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.event`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.feature`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.interaction`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.item`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.lifecycle`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.menu`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.message`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.messaging`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.module`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.placeholder`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.reactive`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.service`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.session`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.state`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.task`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.api.thread`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

### `io.github.fragmer2.bslib.internal.error`
- Purpose: public API surface for this concern area.
- Use when: plugin code needs this package domain (state, tasks, modules, services, etc.).

---

## 3. Class-by-Class Documentation
## `BSLib`
**Qualified name:** `io.github.fragmer2.bslib.api.BSLib`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/BSLib.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for BSLib
// import io.github.fragmer2.bslib.api.BSLib;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void initialize(Plugin plugin, ServiceContainer serviceContainer, CommandRegistryFactory factory) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setCommandRegistryFactory(CommandRegistryFactory factory) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static CommandRegistry getCommandRegistry(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setContainer(ServiceContainer c) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static ServiceContainer getContainer() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setModuleManager(ModuleManager mm) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static ModuleManager getModuleManager() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isModuleEnabled(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static BSModule getModule(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `GuiManagerProvider`
**Qualified name:** `io.github.fragmer2.bslib.api.GuiManagerProvider`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/GuiManagerProvider.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for GuiManagerProvider
// import io.github.fragmer2.bslib.api.GuiManagerProvider;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void setInstance(GuiManager manager) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static GuiManager get() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ShopPlugin`
**Qualified name:** `io.github.fragmer2.bslib.api.ShopPlugin`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/FrameworkPlugin.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ShopPlugin
// import io.github.fragmer2.bslib.api.ShopPlugin;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public final void onEnable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public final void onDisable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void liveReload() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void hardReload() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void reload() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Object getDelegate() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isAutoScanned() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<Object> getManagedObjects() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<Config> getConfigs() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Animation`
**Qualified name:** `io.github.fragmer2.bslib.api.animation.Animation`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/animation/Animation.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Animation
// import io.github.fragmer2.bslib.api.animation.Animation;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static Animation of(List<Frame> frames, boolean loop) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Animation of(Frame... frames) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Animation once(Frame... frames) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<Frame> getFrames() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isLoop() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Frame`
**Qualified name:** `io.github.fragmer2.bslib.api.animation.Frame`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/animation/Frame.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Frame
// import io.github.fragmer2.bslib.api.animation.Frame;
```

### Constructor(s)
- `public Frame(ItemStack item, long duration) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public ItemStack getItem() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long getDuration() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ApiStatus`
**Qualified name:** `io.github.fragmer2.bslib.api.annotation.ApiStatus`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/annotation/ApiStatus.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ApiStatus
// import io.github.fragmer2.bslib.api.annotation.ApiStatus;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Button`
**Qualified name:** `io.github.fragmer2.bslib.api.button.Button`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/button/Button.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Button
// import io.github.fragmer2.bslib.api.button.Button;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static Button of(ItemStack item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Button dynamic(Function<MenuView, ItemStack> renderer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Button of(ItemStack item, Consumer<ClickContext> onClick) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button click(Consumer<ClickContext> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button cancel(boolean cancel) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button command(String command) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button consoleCommand(String command) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button closeOnClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button closeOnClick(boolean close) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button animate(Animation animation) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button bind(Supplier<Object> valueSupplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button bind(io.github.fragmer2.bslib.api.reactive.Reactive<?> reactive) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveList<?> list) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveMap<?, ?> map) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean hasValueChanged() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void resetReactiveState() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isReactive() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isDynamic() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean shouldCancel() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ItemStack render(MenuView view) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Animation getAnimation() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onClick(ClickContext ctx) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ClickContext`
**Qualified name:** `io.github.fragmer2.bslib.api.button.ClickContext`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/button/ClickContext.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ClickContext
// import io.github.fragmer2.bslib.api.button.ClickContext;
```

### Constructor(s)
- `public ClickContext(Player player, InventoryClickEvent event, MenuView view) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public Player player() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InventoryClickEvent event() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InventoryClickEvent getClickEvent() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public MenuView view() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isShiftClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isLeftClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isRightClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isShiftLeftClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isShiftRightClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isMiddleClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isNumberKey() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isDoubleClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ClickType getClickType() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int getSlot() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void close() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void update() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void back() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `PluginCapabilityRegistry`
**Qualified name:** `io.github.fragmer2.bslib.api.capability.PluginCapabilityRegistry`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/capability/PluginCapabilityRegistry.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PluginCapabilityRegistry
// import io.github.fragmer2.bslib.api.capability.PluginCapabilityRegistry;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void provide(String pluginName, String... capabilities) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void require(String pluginName, String... capabilities) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static List<String> validateMissing(String pluginName) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Set<String> providedBy(String pluginName) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Arg`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Arg`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Arg.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Arg
// import io.github.fragmer2.bslib.api.command.Arg;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Command`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Command`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Command.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Command
// import io.github.fragmer2.bslib.api.command.Command;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `CommandRegistry`
**Qualified name:** `io.github.fragmer2.bslib.api.command.CommandRegistry`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/CommandRegistry.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for CommandRegistry
// import io.github.fragmer2.bslib.api.command.CommandRegistry;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `CommandRegistryFactory`
**Qualified name:** `io.github.fragmer2.bslib.api.command.CommandRegistryFactory`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/CommandRegistryFactory.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for CommandRegistryFactory
// import io.github.fragmer2.bslib.api.command.CommandRegistryFactory;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `ConsoleOnly`
**Qualified name:** `io.github.fragmer2.bslib.api.command.ConsoleOnly`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/ConsoleOnly.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ConsoleOnly
// import io.github.fragmer2.bslib.api.command.ConsoleOnly;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Cooldown`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Cooldown`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Cooldown.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Cooldown
// import io.github.fragmer2.bslib.api.command.Cooldown;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `JoinArgs`
**Qualified name:** `io.github.fragmer2.bslib.api.command.JoinArgs`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/JoinArgs.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for JoinArgs
// import io.github.fragmer2.bslib.api.command.JoinArgs;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Optional`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Optional`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Optional.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Optional
// import io.github.fragmer2.bslib.api.command.Optional;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Permission`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Permission`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Permission.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Permission
// import io.github.fragmer2.bslib.api.command.Permission;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `PlayerOnly`
**Qualified name:** `io.github.fragmer2.bslib.api.command.PlayerOnly`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/PlayerOnly.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PlayerOnly
// import io.github.fragmer2.bslib.api.command.PlayerOnly;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Subcommand`
**Qualified name:** `io.github.fragmer2.bslib.api.command.Subcommand`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/Subcommand.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Subcommand
// import io.github.fragmer2.bslib.api.command.Subcommand;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `TabComplete`
**Qualified name:** `io.github.fragmer2.bslib.api.command.TabComplete`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/TabComplete.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for TabComplete
// import io.github.fragmer2.bslib.api.command.TabComplete;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `TabType`
**Qualified name:** `io.github.fragmer2.bslib.api.command.TabType`  
**Kind:** `enum`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/command/TabType.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for TabType
// import io.github.fragmer2.bslib.api.command.TabType;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Config`
**Qualified name:** `io.github.fragmer2.bslib.api.config.Config`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/config/Config.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Config
// import io.github.fragmer2.bslib.api.config.Config;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static Config of(Plugin plugin, String fileName) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Config fromFile(Plugin plugin, File file) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Node node(String path) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void load() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void reload() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void save() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Config autoReload(Consumer<Config> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Config autoReload() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<String> keys() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<String> keys(String path) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean has(String path) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public FileConfiguration raw() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public File getFile() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void shutdown() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String asString() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String asString(String def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int asInt() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int asInt(int def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public double asDouble() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public double asDouble(double def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean asBool() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean asBool(boolean def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long asLong() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long asLong(long def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<String> asStringList() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<Integer> asIntList() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<?> asList() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Object asObject() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean exists() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void set(Object value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Node child(String sub) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<String> keys() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Cooldowns`
**Qualified name:** `io.github.fragmer2.bslib.api.cooldown.Cooldowns`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/cooldown/Cooldowns.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Cooldowns
// import io.github.fragmer2.bslib.api.cooldown.Cooldowns;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static CooldownBuilder of(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static CooldownBuilder global() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isOnCooldown(Player player, String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public CooldownBuilder key(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public CooldownBuilder seconds(int seconds) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public CooldownBuilder minutes(int minutes) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public CooldownBuilder duration(long amount, TimeUnit unit) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public CooldownBuilder bypass(String permission) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean check() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean check(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long remaining() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long remaining(TimeUnit unit) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void set(long amount, TimeUnit unit) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Debug`
**Qualified name:** `io.github.fragmer2.bslib.api.debug.Debug`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/debug/Debug.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Debug
// import io.github.fragmer2.bslib.api.debug.Debug;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void enable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void disable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isEnabled() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void toggle() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void trackTime(String label, Runnable task) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static long startTiming() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void endTiming(String label, long startNano) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static TraceScope trace(String label) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T trace(String label, Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void close() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void report(CommandSender sender) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Map<String, String> timingSummary() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void doctor(CommandSender sender, org.bukkit.plugin.Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void resetTimings() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `GuiInspector`
**Qualified name:** `io.github.fragmer2.bslib.api.debug.GuiInspector`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/debug/GuiInspector.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for GuiInspector
// import io.github.fragmer2.bslib.api.debug.GuiInspector;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void enable(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void disable(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void toggle(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isEnabled(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean inspect(Player player, Menu menu, int slot, Button button, MenuView view) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void recordRender(String menuClass, int slot, long nanos) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clearStats() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clearPlayer(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static double averageRenderMillis() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `EconomyImpl`
**Qualified name:** `io.github.fragmer2.bslib.api.di.EconomyImpl`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/di/Service.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for EconomyImpl
// import io.github.fragmer2.bslib.api.di.EconomyImpl;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Inject`
**Qualified name:** `io.github.fragmer2.bslib.api.di.Inject`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/di/Inject.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Inject
// import io.github.fragmer2.bslib.api.di.Inject;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `ServiceContainer`
**Qualified name:** `io.github.fragmer2.bslib.api.di.ServiceContainer`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/di/ServiceContainer.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ServiceContainer
// import io.github.fragmer2.bslib.api.di.ServiceContainer;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public <T> void register(Class<T> type, T instance) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> void register(Class<T> type, String name, T instance) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> void registerLazy(Class<T> type, Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> void registerLazy(Class<T> type, String name, Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> T get(Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> T get(Class<T> type, String name) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> T getOrDefault(Class<T> type, T defaultValue) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean has(Class<?> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean has(Class<?> type, String name) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void inject(Object target) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Bus`
**Qualified name:** `io.github.fragmer2.bslib.api.event.Bus`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/event/Bus.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Bus
// import io.github.fragmer2.bslib.api.event.Bus;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler, Object owner) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T publish(T event) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void unsubscribe(Subscription<?> sub) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void unsubscribeAll(Object owner) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void cancel() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Events`
**Qualified name:** `io.github.fragmer2.bslib.api.event.Events`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/event/Events.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Events
// import io.github.fragmer2.bslib.api.event.Events;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T extends Event> ListenerBuilder<T> listen(Class<T> eventType, Consumer<T> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void unregisterAll(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ListenerBuilder<T> priority(EventPriority priority) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ListenerBuilder<T> ignoreCancelled() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ListenerBuilder<T> filter(Predicate<T> filter) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ListenerBuilder<T> forPlayer(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ListenerBuilder<T> expireAfter(int times) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ManagedListener<T> register(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void unregister() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isRegistered() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Features`
**Qualified name:** `io.github.fragmer2.bslib.api.feature.Features`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/feature/Features.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Features
// import io.github.fragmer2.bslib.api.feature.Features;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void init(Plugin plugin, String fileName) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isEnabled(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isDisabled(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void enable(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void disable(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void toggle(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void set(String feature, boolean enabled) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void reset(String feature) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void resetAll() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void onChange(String feature, Consumer<Boolean> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Set<String> all() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Interact`
**Qualified name:** `io.github.fragmer2.bslib.api.interaction.Interact`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/interaction/Interact.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Interact
// import io.github.fragmer2.bslib.api.interaction.Interact;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static InteractionBuilder onRightClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static InteractionBuilder onLeftClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static InteractionBuilder onClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void init(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Player player() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public PlayerInteractEvent event() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void cancel() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InteractionBuilder with(Material material) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InteractionBuilder filter(Predicate<InteractionContext> filter) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InteractionBuilder permission(String permission) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InteractionBuilder allowDefault() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public InteractionBuilder handle(Consumer<InteractionContext> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void register(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onInteract(PlayerInteractEvent event) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Item`
**Qualified name:** `io.github.fragmer2.bslib.api.item.Item`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/item/Item.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Item
// import io.github.fragmer2.bslib.api.item.Item;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static Item of(Material material) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item amount(int amount) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item name(String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item lore(String... lines) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item lore(List<String> lines) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item glow() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item enchant(Enchantment enchantment, int level) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item unbreakable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item addFlags(ItemFlag... flags) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item hideFlags() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item model(int id) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item skullOwner(String playerName) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Item skullTexture(String base64) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ItemStack build() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ItemStack build(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Items`
**Qualified name:** `io.github.fragmer2.bslib.api.item.Items`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/item/Items.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Items
// import io.github.fragmer2.bslib.api.item.Items;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static String serialize(ItemStack item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static ItemStack deserialize(String data) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static String serializeArray(ItemStack[] items) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static ItemStack[] deserializeArray(String data) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `OnDisable`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.OnDisable`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/OnDisable.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for OnDisable
// import io.github.fragmer2.bslib.api.lifecycle.OnDisable;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `OnEnable`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.OnEnable`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/OnEnable.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for OnEnable
// import io.github.fragmer2.bslib.api.lifecycle.OnEnable;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `OnReload`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.OnReload`  
**Kind:** `@interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/OnReload.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for OnReload
// import io.github.fragmer2.bslib.api.lifecycle.OnReload;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `PlayerListener`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.PlayerListener`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/EventListener.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PlayerListener
// import io.github.fragmer2.bslib.api.lifecycle.PlayerListener;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `PluginScanner`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.PluginScanner`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/PluginScanner.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PluginScanner
// import io.github.fragmer2.bslib.api.lifecycle.PluginScanner;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static ScanResult scan(JavaPlugin plugin, AutoScan config) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Object instantiate(Class<?> clazz) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public final List<Class<?>> commandClasses = new ArrayList<>();`
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public final List<Class<?>> listenerClasses = new ArrayList<>();`
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public final List<Class<?>> serviceClasses = new ArrayList<>();`
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public final List<Class<?>> stateClasses = new ArrayList<>();`
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ShopPlugin`
**Qualified name:** `io.github.fragmer2.bslib.api.lifecycle.ShopPlugin`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/lifecycle/AutoScan.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ShopPlugin
// import io.github.fragmer2.bslib.api.lifecycle.ShopPlugin;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `InventoryPolicy`
**Qualified name:** `io.github.fragmer2.bslib.api.menu.InventoryPolicy`  
**Kind:** `enum`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/menu/InventoryPolicy.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for InventoryPolicy
// import io.github.fragmer2.bslib.api.menu.InventoryPolicy;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Menu`
**Qualified name:** `io.github.fragmer2.bslib.api.menu.Menu`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/menu/Menu.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Menu
// import io.github.fragmer2.bslib.api.menu.Menu;
```

### Constructor(s)
- `public Menu(String title, int rows) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public String getTitle() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int getRows() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Map<Integer, Button> getButtons() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Menu secure() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Menu policy(InventoryPolicy... policies) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<InventoryPolicy> getPolicies() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean hasPolicy(InventoryPolicy policy) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void open(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void openWithHistory(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onOpen(MenuView view) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onClose(MenuView view) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `MenuView`
**Qualified name:** `io.github.fragmer2.bslib.api.menu.MenuView`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/menu/MenuView.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for MenuView
// import io.github.fragmer2.bslib.api.menu.MenuView;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `ReactiveMenu`
**Qualified name:** `io.github.fragmer2.bslib.api.menu.ReactiveMenu`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/menu/ReactiveMenu.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ReactiveMenu
// import io.github.fragmer2.bslib.api.menu.ReactiveMenu;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static ReactiveMenu create(String title, int rows) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMenu render(Consumer<RenderContext> renderFn) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMenu bind(Reactive<?>... reactives) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMenu secure() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMenu policy(InventoryPolicy... policies) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Menu build() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void open(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder button(int slot) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void fill(Material material) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void fill(ItemStack filler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void border(Material material) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void border(ItemStack item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void row(int row, Button button) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Menu menu() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder item(ItemStack item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder item(Material material) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder text(String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder lore(String... lines) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder onClick(Consumer<io.github.fragmer2.bslib.api.button.ClickContext> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder closeOnClick() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder command(String command) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ButtonBuilder done() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isDeclarative(Menu menu) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean checkRerender(Menu menu) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ShopMenu`
**Qualified name:** `io.github.fragmer2.bslib.api.menu.ShopMenu`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/menu/LayoutMenu.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ShopMenu
// import io.github.fragmer2.bslib.api.menu.ShopMenu;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public LayoutMenu(String title, int rows, String... layout) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Msg`
**Qualified name:** `io.github.fragmer2.bslib.api.message.Msg`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/message/Msg.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Msg
// import io.github.fragmer2.bslib.api.message.Msg;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void init(Plugin plugin, String messagesFile) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void addLanguage(String lang, Config config) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setPrefix(String p) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setDefaultLanguage(String lang) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static MessageBuilder key(String configKey) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static MessageBuilder of(String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public MessageBuilder replace(String key, String value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public MessageBuilder replace(String key, Object value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public MessageBuilder prefixed() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void send(CommandSender sender) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void send(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void sendTitle(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void sendTitle(Player player, Duration fadeIn, Duration stay, Duration fadeOut) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void sendSubtitle(Player player, String titleKey) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void sendActionBar(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void broadcast() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String text(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String text() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Component component(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Component component() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `PluginMessageBus`
**Qualified name:** `io.github.fragmer2.bslib.api.messaging.PluginMessageBus`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/messaging/PluginMessageBus.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PluginMessageBus
// import io.github.fragmer2.bslib.api.messaging.PluginMessageBus;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> void subscribe(String topic, Consumer<T> consumer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void publish(String topic, Object payload) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clearTopic(String topic) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clearAll() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `RemoteBridge`
**Qualified name:** `io.github.fragmer2.bslib.api.messaging.RemoteBridge`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/messaging/RemoteBridge.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for RemoteBridge
// import io.github.fragmer2.bslib.api.messaging.RemoteBridge;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `AbstractModule`
**Qualified name:** `io.github.fragmer2.bslib.api.module.AbstractModule`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/AbstractModule.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for AbstractModule
// import io.github.fragmer2.bslib.api.module.AbstractModule;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public void onEnable(ModuleContext context) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `BSModule`
**Qualified name:** `io.github.fragmer2.bslib.api.module.BSModule`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/BSModule.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for BSModule
// import io.github.fragmer2.bslib.api.module.BSModule;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `ModuleContext`
**Qualified name:** `io.github.fragmer2.bslib.api.module.ModuleContext`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/ModuleContext.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ModuleContext
// import io.github.fragmer2.bslib.api.module.ModuleContext;
```

### Constructor(s)
- `public ModuleContext(Plugin plugin, ServiceContainer container) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public Plugin plugin() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ServiceContainer container() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Logger logger() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ModuleManager`
**Qualified name:** `io.github.fragmer2.bslib.api.module.ModuleManager`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/ModuleManager.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ModuleManager
// import io.github.fragmer2.bslib.api.module.ModuleManager;
```

### Constructor(s)
- `public ModuleManager(Plugin plugin, ServiceContainer container) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public void register(BSModule module) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void registerFromRegistry() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean enable(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void enableAll() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void disable(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void disableAll() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isEnabled(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BSModule getModule(String moduleId) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Collection<BSModule> getModules() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ModuleRegistry`
**Qualified name:** `io.github.fragmer2.bslib.api.module.ModuleRegistry`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/ModuleRegistry.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ModuleRegistry
// import io.github.fragmer2.bslib.api.module.ModuleRegistry;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static List<Supplier<BSModule>> getModuleSuppliers() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ScoreboardModule`
**Qualified name:** `io.github.fragmer2.bslib.api.module.ScoreboardModule`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/module/Module.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ScoreboardModule
// import io.github.fragmer2.bslib.api.module.ScoreboardModule;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `PlaceholderRegistry`
**Qualified name:** `io.github.fragmer2.bslib.api.placeholder.PlaceholderRegistry`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/placeholder/PlaceholderRegistry.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PlaceholderRegistry
// import io.github.fragmer2.bslib.api.placeholder.PlaceholderRegistry;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Placeholders`
**Qualified name:** `io.github.fragmer2.bslib.api.placeholder.Placeholders`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/placeholder/Placeholders.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Placeholders
// import io.github.fragmer2.bslib.api.placeholder.Placeholders;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void setRegistry(PlaceholderRegistry reg) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void register(String key, Function<Player, String> replacer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void register(String key, Supplier<String> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void asPlaceholder(String key, Reactive<T> reactive) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void asPlaceholder(String key, Function<Player, Reactive<T>> reactiveSupplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static String apply(Player player, String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static String apply(String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void register(String key, Function<Player, String> replacer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String apply(Player player, String text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Reactive`
**Qualified name:** `io.github.fragmer2.bslib.api.reactive.Reactive`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/reactive/Reactive.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Reactive
// import io.github.fragmer2.bslib.api.reactive.Reactive;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> Reactive<T> of(T initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Reactive<Integer> ofInt(int initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Reactive<Double> ofDouble(double initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Reactive<Boolean> ofBool(boolean initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Reactive<String> ofString(String initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public T get() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void set(T newValue) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void update(Function<T, T> updater) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void setSilent(T newValue) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public synchronized Reactive<T> beginBatch() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> endBatch() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long version() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> onChange(BiConsumer<T, T> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Subscription subscribeChange(BiConsumer<T, T> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> onSet(Consumer<T> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Subscription subscribeSet(Consumer<T> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clearListeners() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <R> Reactive<R> map(Function<T, R> mapper) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <A, B, R> Reactive<R> combine(Reactive<A> a, Reactive<B> b,`
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> distinctUntilChanged() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> throttle(long ticks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> debounce(long ticks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void destroy() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isDestroyed() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Reactive<T> asPlaceholder(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void asPlayerPlaceholder(String key, Function<org.bukkit.entity.Player, Reactive<T>> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> Reactive<T> computeAsync(T defaultValue, java.util.function.Supplier<T> asyncLoader) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void refreshAsync(java.util.function.Supplier<T> asyncLoader) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void increment() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void decrement() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void add(Number amount) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String toString() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ReactiveBinding`
**Qualified name:** `io.github.fragmer2.bslib.api.reactive.ReactiveBinding`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/reactive/ReactiveBinding.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ReactiveBinding
// import io.github.fragmer2.bslib.api.reactive.ReactiveBinding;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static ScoreboardBuilder scoreboard(Player player, String title) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static BossBarBuilder bossbar(Player player, BossBar.Color color) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static ActionBarBuilder actionbar(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void destroyAll(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static int activePlayerCount() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static int activeBindingCount() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ScoreboardBuilder line(int index, Reactive<String> value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ScoreboardBuilder line(int index, String staticText) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Destroyable build(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BossBarBuilder title(Reactive<String> title) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BossBarBuilder title(String staticTitle) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BossBarBuilder progress(Reactive<Float> progress) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BossBarBuilder progress(float staticProgress) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BossBarBuilder overlay(BossBar.Overlay overlay) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Destroyable build(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ActionBarBuilder text(Reactive<String> text) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ActionBarBuilder text(String staticText) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ActionBarBuilder interval(long ticks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Destroyable build(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ReactiveList`
**Qualified name:** `io.github.fragmer2.bslib.api.reactive.ReactiveList`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/reactive/ReactiveList.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ReactiveList
// import io.github.fragmer2.bslib.api.reactive.ReactiveList;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> ReactiveList<T> of(T... items) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> ReactiveList<T> of(Collection<T> items) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> ReactiveList<T> empty() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void add(T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void add(int index, T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean remove(T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public T remove(int index) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void set(int index, T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void addAll(Collection<T> items) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void replaceAll(Collection<T> items) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void sort(Comparator<? super T> comparator) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public T get(int index) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int size() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isEmpty() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean contains(T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int indexOf(T item) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public List<T> asList() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long version() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveList<T> onChange(Consumer<List<T>> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public java.util.function.Supplier<Object> asBindable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String toString() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `ReactiveMap`
**Qualified name:** `io.github.fragmer2.bslib.api.reactive.ReactiveMap`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/reactive/ReactiveMap.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for ReactiveMap
// import io.github.fragmer2.bslib.api.reactive.ReactiveMap;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <K, V> ReactiveMap<K, V> empty() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <K, V> ReactiveMap<K, V> of(Map<K, V> initial) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public V put(K key, V value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public V remove(K key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void putAll(Map<K, V> entries) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public V get(K key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public V getOrDefault(K key, V def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean containsKey(K key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int size() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isEmpty() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Map<K, V> asMap() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long version() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMap<K, V> onChange(Consumer<Map<K, V>> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public ReactiveMap<K, V> onChange(BiConsumer<K, V> listener) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public java.util.function.Supplier<Object> asBindable() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Subscription`
**Qualified name:** `io.github.fragmer2.bslib.api.reactive.Subscription`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/reactive/Subscription.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Subscription
// import io.github.fragmer2.bslib.api.reactive.Subscription;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Services`
**Qualified name:** `io.github.fragmer2.bslib.api.service.Services`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/service/Services.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Services
// import io.github.fragmer2.bslib.api.service.Services;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> void provide(Class<T> type, T instance) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void provide(Class<T> type, String name, T instance) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void provideLazy(Class<T> type, Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void provideLazy(Class<T> type, String name, Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T get(Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T get(Class<T> type, String name) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T getOrDefault(Class<T> type, T fallback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean has(Class<?> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean has(Class<?> type, String name) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static int registeredCount() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static int lazyCount() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Set<String> serviceKeys() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void onProvide(Class<T> type, Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void remove(Class<?> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void remove(Class<?> type, String name) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Session`
**Qualified name:** `io.github.fragmer2.bslib.api.session.Session`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/session/Session.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Session
// import io.github.fragmer2.bslib.api.session.Session;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public Session set(String key, Object value) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Session remove(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> T get(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <T> T get(String key, T defaultValue) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String getString(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public String getString(String key, String def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int getInt(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int getInt(String key, int def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public double getDouble(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public double getDouble(String key, double def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long getLong(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public long getLong(String key, long def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean getBool(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean getBool(String key, boolean def) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean has(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Map<String, Object> all() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Sessions`
**Qualified name:** `io.github.fragmer2.bslib.api.session.Sessions`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/session/Sessions.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Sessions
// import io.github.fragmer2.bslib.api.session.Sessions;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void init(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Session of(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Session get(UUID uuid) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean has(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void autoLoad(Plugin plugin, Class<T> stateClass) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void onJoin(Consumer<SessionContext> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void onQuit(BiConsumer<Player, Session> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onPlayerJoin(PlayerJoinEvent event) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void onPlayerQuit(PlayerQuitEvent event) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void clear() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `PlayerData`
**Qualified name:** `io.github.fragmer2.bslib.api.state.PlayerData`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/state/State.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PlayerData
// import io.github.fragmer2.bslib.api.state.PlayerData;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `PlayerData`
**Qualified name:** `io.github.fragmer2.bslib.api.state.PlayerData`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/state/StateKey.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for PlayerData
// import io.github.fragmer2.bslib.api.state.PlayerData;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `StateManager`
**Qualified name:** `io.github.fragmer2.bslib.api.state.StateManager`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/state/StateManager.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for StateManager
// import io.github.fragmer2.bslib.api.state.StateManager;
```

### Constructor(s)
- `public StateManager(Plugin plugin, Class<T> type) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public T getOrCreate(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public T get(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void load(String key, Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public T loadSync(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void save(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void saveSync(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void saveAllDirty() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void saveAll() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void unload(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void delete(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean exists(String key) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<String> cachedKeys() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Set<String> allKeys() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void shutdown() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `StateSerializer`
**Qualified name:** `io.github.fragmer2.bslib.api.state.StateSerializer`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/state/StateSerializer.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for StateSerializer
// import io.github.fragmer2.bslib.api.state.StateSerializer;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static Map<String, Object> serialize(Object state) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void deserialize(Object state, Map<String, Object> data) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static String getKey(Object state) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setKey(Object state, String keyValue) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `States`
**Qualified name:** `io.github.fragmer2.bslib.api.state.States`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/state/States.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for States
// import io.github.fragmer2.bslib.api.state.States;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static <T> void register(Plugin plugin, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void autoRegister(Plugin plugin, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T get(Plugin plugin, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void saveGlobal(Plugin plugin, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void loadGlobal(Plugin plugin, Class<T> type, Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T of(Player player, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T getOrCreate(Player player, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void load(Player player, Class<T> type, Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void save(Player player, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void unload(Player player, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T of(String key, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void load(String key, Class<T> type, Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void save(String key, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void unload(String key, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void delete(String key, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> boolean exists(String key, Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void saveAllDirty(Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void saveAll(Class<T> type) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void unloadAll(Player player) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void shutdown() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void shutdown(Plugin plugin) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static Set<Class<?>> registeredTypes() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `FrameworkTask`
**Qualified name:** `io.github.fragmer2.bslib.api.task.FrameworkTask`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/task/FrameworkTask.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for FrameworkTask
// import io.github.fragmer2.bslib.api.task.FrameworkTask;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `SchedulerAdapter`
**Qualified name:** `io.github.fragmer2.bslib.api.task.SchedulerAdapter`  
**Kind:** `interface`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/task/SchedulerAdapter.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for SchedulerAdapter
// import io.github.fragmer2.bslib.api.task.SchedulerAdapter;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
- No direct public methods declared on this type.

---

## `Tasks`
**Qualified name:** `io.github.fragmer2.bslib.api.task.Tasks`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/task/Tasks.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Tasks
// import io.github.fragmer2.bslib.api.task.Tasks;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void init(Plugin p) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setScheduler(SchedulerAdapter adapter) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static int trackedTaskCount() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static TaskBuilder sync() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static TaskBuilder async() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static TaskBuilder timer(long intervalTicks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> io.github.fragmer2.bslib.api.thread.AsyncChain<T> compute(java.util.function.Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public TaskBuilder delay(long ticks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public TaskBuilder repeat(long ticks) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public BukkitTask run(Runnable runnable) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public FrameworkTask runTracked(Runnable runnable) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void run(Consumer<BukkitTask> consumer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public FrameworkTask runTracked(Consumer<BukkitTask> consumer) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public int getTaskId() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public Plugin getOwner() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isSync() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void cancel() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isCancelled() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `Async`
**Qualified name:** `io.github.fragmer2.bslib.api.thread.Async`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/thread/Async.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for Async
// import io.github.fragmer2.bslib.api.thread.Async;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void init(Logger log) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void setDebug(boolean debug) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static boolean isMainThread() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void ensureSync() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void ensureAsync() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void sync(Runnable task) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void async(Runnable task) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> T syncGet(Supplier<T> supplier) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static <T> void run(Supplier<T> asyncWork, Consumer<T> syncCallback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public static void guard() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `AsyncChain`
**Qualified name:** `io.github.fragmer2.bslib.api.thread.AsyncChain`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/api/thread/AsyncChain.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for AsyncChain
// import io.github.fragmer2.bslib.api.thread.AsyncChain;
```

### Constructor(s)
- `public AsyncChain(Supplier<T> computation) {`
  - Parameters: use values matching the constructor contract in source.
  - Threading: instantiate on plugin bootstrap thread unless type docs/state imply otherwise.

### Methods
#### `public AsyncChain<T> thenSync(Consumer<T> callback) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public <R> AsyncChain<R> thenAsync(Function<T, R> transform) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public AsyncChain<T> onError(Consumer<Throwable> handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public AsyncChain<T> timeout(long amount, TimeUnit unit) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public AsyncChain<T> onTimeout(Runnable handler) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public void cancel() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```
#### `public boolean isCancelled() `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## `FrameworkExceptionHandler`
**Qualified name:** `io.github.fragmer2.bslib.internal.error.FrameworkExceptionHandler`  
**Kind:** `class`  
**Source:** `bslib-api/src/main/java/io/github/fragmer2/bslib/internal/error/FrameworkExceptionHandler.java`

### Purpose
Defines public behavior for this framework domain and serves as a stable integration point for plugin code.

### Lifecycle
- Created by plugin bootstrap, static factory, or framework registration path depending on type design.
- Must be cleaned up if it owns runtime resources (subscriptions, tasks, registry entries).

### Thread Safety
- Assume Bukkit main-thread requirements for game-state mutation.
- Async use is only safe for methods explicitly intended for non-Bukkit work.

### Usage Example
```java
// Example integration point for FrameworkExceptionHandler
// import io.github.fragmer2.bslib.internal.error.FrameworkExceptionHandler;
```

### Constructor(s)
- No public constructor (factory/static/annotation/interface-driven API).

### Methods
#### `public static void handle(Plugin plugin, Source source, Throwable error, Map<String, Object> context) `
- **Description:** Executes the declared API operation.
- **Parameters:** Defined by the signature; validate input ownership/lifecycle before calling.
- **Return value:** As declared in signature; may be fluent (`this`), data, or control handle.
- **Threading rules:** Keep Bukkit-mutating usage on main thread; async only where safe.
- **Ownership behavior:** If registration/subscription/task producing, caller is responsible for teardown.
- **Common mistakes:** forgetting disable cleanup, mixing async with Bukkit state, ignoring nullability/contracts.
- **Example usage:**
```java
// invoke according to signature
```

---

## 4. Reactive System Deep Reference
- Batching: `beginBatch()` collects intermediate mutations and `endBatch()` applies dispatch semantics at batch close.
- Subscriptions: `subscribeSet`/`subscribeChange` return `Subscription` handles; caller must unsubscribe for bounded lifetimes.
- Disposal: `destroy()` tears down listeners/upstream dependencies; use for GUI/player/session scope end.
- `debounce(ticks)`: emits after quiet interval; latest value wins in burst scenarios.
- `throttle(ticks)`: emits first value in window then suppresses until window closes.
- Memory safety: never keep long-lived reactive graphs tied to transient player objects without destroy/unsubscribe.

```java
Reactive<Integer> xp = Reactive.of(0);
Reactive<Integer> hud = xp.distinctUntilChanged().throttle(2);
Subscription handle = hud.subscribeSet(v -> sidebar.updateLine("xp", String.valueOf(v)));
// on close/disable
handle.unsubscribe();
hud.destroy();
xp.destroy();
```

## 5. Task System Reference
- `Tasks` routes scheduling through `SchedulerAdapter`; runtime uses Bukkit adapter, tests may inject in-memory adapter.
- `FrameworkTask` is the stable cancellation/tracking handle.
- Tracked task count derives from active-task probing and explicit cancel/unregister flow.
- Sync vs async execution follows builder (`sync()` / `async()`).
- Cancellation guarantee: calling `cancel()` stops future executions for repeating tasks and removes tracking entry.

## 6. Services Registry Reference
- `Services` is a shared registry for type(+name)-keyed service publication.
- Owner-aware registration prevents cross-plugin overwrite collisions.
- Lookup resolves eagerly registered instances and lazy suppliers.
- Teardown: `unregisterOwner(plugin)` removes all owned keys; required at disable/reload.

## 7. Messaging Bus Reference
- `PluginMessageBus` provides local process pub/sub by topic.
- Delivery runs on caller thread; thread model is producer-defined.
- Owner-based subscription unregister avoids leak accumulation across reloads.

## 8. Capability System Reference
- `PluginCapabilityRegistry` declares provided and required capabilities per plugin owner.
- `validateMissing(...)` computes unresolved requirements against provided capability set.
- Use for optional integration gating and startup validation.

## 9. Module System Reference
- `ModuleManager` manages module registration, dependency resolution, and deterministic enable/disable ordering.
- State machine (conceptual): `registered -> enabling -> enabled -> disabling -> disabled`.
- Reverse-order disable is used for safe dependency unwinding.

## 10. Error Handling System
- `FrameworkExceptionHandler` centralizes exception capture with source tag and context map.
- Use around custom execution boundaries to preserve plugin uptime and structured diagnostics.

## 11. Lifecycle & Ownership Model (CRITICAL)
```text
Plugin enable
  -> initialize framework runtime
  -> register services/modules/subscriptions/tasks (owner = plugin)
  -> run gameplay operations
Plugin disable/reload
  -> cancel tasks
  -> unsubscribe/destroy reactive resources
  -> unregisterOwner for services/messaging/capabilities
  -> disable modules in reverse order
```

## 12. Threading Model
- SAFE main-thread operations: Bukkit world/entity/inventory mutation.
- SAFE async operations: IO/computation not touching Bukkit mutable runtime.
- UNSAFE: direct Bukkit state mutation from async tasks/callbacks.
- Pattern: compute async -> schedule sync apply via task API.

## 13. Performance Characteristics
- Service registry lookup is map-based and suitable for frequent access.
- Task tracking overhead scales with active task count; cleanup is linear over tracked entries.
- Reactive chains can amplify update fan-out; apply throttle/debounce/distinct where needed.
- Module lifecycle work is dependency-order bounded by registered module graph size.

## 14. Anti-Patterns
- Not unregistering owner resources on disable (leaks/collision risks).
- Fire-and-forget repeating tasks without stored handles.
- Async Bukkit mutations in reactive/task callbacks.
- Retaining player-bound reactive subscriptions past player/session lifecycle.

```java
// WRONG: async Bukkit mutation
Tasks.async().runTracked(() -> player.teleport(location));

// RIGHT: async compute then sync apply
Tasks.async().runTracked(() -> {
    Data d = load();
    Tasks.sync().runTracked(() -> apply(player, d));
});
```

---

## Appendix: Full Public Signature Inventory
This appendix enumerates all detected public signature lines from `bslib-api` source for completeness.
### `io.github.fragmer2.bslib.api.BSLib`
- `public static void initialize(Plugin plugin, ServiceContainer serviceContainer, CommandRegistryFactory factory) {`
- `public static void setCommandRegistryFactory(CommandRegistryFactory factory) {`
- `public static CommandRegistry getCommandRegistry(Plugin plugin) {`
- `public static void setContainer(ServiceContainer c) {`
- `public static ServiceContainer getContainer() {`
- `public static void setModuleManager(ModuleManager mm) {`
- `public static ModuleManager getModuleManager() {`
- `public static boolean isModuleEnabled(String moduleId) {`
- `public static BSModule getModule(String moduleId) {`
### `io.github.fragmer2.bslib.api.GuiManagerProvider`
- `public static void setInstance(GuiManager manager) {`
- `public static GuiManager get() {`
### `io.github.fragmer2.bslib.api.ShopPlugin`
- `public final void onEnable() {`
- `public final void onDisable() {`
- `public void liveReload() {`
- `public void hardReload() {`
- `public void reload() {`
- `public Object getDelegate() { return delegate; }`
- `public boolean isAutoScanned() { return autoScanned; }`
- `public List<Object> getManagedObjects() { return List.copyOf(managedObjects); }`
- `public List<Config> getConfigs() { return List.copyOf(configs); }`
### `io.github.fragmer2.bslib.api.animation.Animation`
- `public static Animation of(List<Frame> frames, boolean loop) {`
- `public static Animation of(Frame... frames) {`
- `public static Animation once(Frame... frames) {`
- `public List<Frame> getFrames() { return frames; }`
- `public boolean isLoop() { return loop; }`
### `io.github.fragmer2.bslib.api.animation.Frame`
- `public Frame(ItemStack item, long duration) {`
- `public ItemStack getItem() { return item; }`
- `public long getDuration() { return duration; }`
### `io.github.fragmer2.bslib.api.annotation.ApiStatus`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.button.Button`
- `public static Button of(ItemStack item) {`
- `public static Button dynamic(Function<MenuView, ItemStack> renderer) {`
- `public static Button of(ItemStack item, Consumer<ClickContext> onClick) {`
- `public Button click(Consumer<ClickContext> handler) {`
- `public Button cancel(boolean cancel) {`
- `public Button command(String command) {`
- `public Button consoleCommand(String command) {`
- `public Button closeOnClick() {`
- `public Button closeOnClick(boolean close) {`
- `public Button animate(Animation animation) {`
- `public Button bind(Supplier<Object> valueSupplier) {`
- `public Button bind(io.github.fragmer2.bslib.api.reactive.Reactive<?> reactive) {`
- `public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveList<?> list) {`
- `public Button bind(io.github.fragmer2.bslib.api.reactive.ReactiveMap<?, ?> map) {`
- `public boolean hasValueChanged() {`
- `public void resetReactiveState() {`
- `public boolean isReactive() {`
- `public boolean isDynamic() {`
- `public boolean shouldCancel() { return cancel; }`
- `public ItemStack render(MenuView view) {`
- `public Animation getAnimation() { return animation; }`
- `public void onClick(ClickContext ctx) {`
### `io.github.fragmer2.bslib.api.button.ClickContext`
- `public ClickContext(Player player, InventoryClickEvent event, MenuView view) {`
- `public Player player() { return player; }`
- `public InventoryClickEvent event() { return event; }`
- `public InventoryClickEvent getClickEvent() { return event; }`
- `public MenuView view() { return view; }`
- `public boolean isShiftClick() {`
- `public boolean isLeftClick() {`
- `public boolean isRightClick() {`
- `public boolean isShiftLeftClick() {`
- `public boolean isShiftRightClick() {`
- `public boolean isMiddleClick() {`
- `public boolean isNumberKey() {`
- `public boolean isDoubleClick() {`
- `public ClickType getClickType() {`
- `public int getSlot() {`
- `public void close() { view.close(); }`
- `public void update() { view.update(); }`
- `public void back() {`
### `io.github.fragmer2.bslib.api.capability.PluginCapabilityRegistry`
- `public static void provide(String pluginName, String... capabilities) {`
- `public static void require(String pluginName, String... capabilities) {`
- `public static List<String> validateMissing(String pluginName) {`
- `public static Set<String> providedBy(String pluginName) {`
- `public static void clear() {`
### `io.github.fragmer2.bslib.api.command.Arg`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.Command`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.CommandRegistry`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.CommandRegistryFactory`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.ConsoleOnly`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.Cooldown`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.JoinArgs`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.Optional`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.Permission`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.PlayerOnly`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.Subcommand`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.TabComplete`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.command.TabType`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.config.Config`
- `public static Config of(Plugin plugin, String fileName) {`
- `public static Config fromFile(Plugin plugin, File file) {`
- `public Node node(String path) {`
- `public void load() {`
- `public void reload() {`
- `public void save() {`
- `public Config autoReload(Consumer<Config> callback) {`
- `public Config autoReload() {`
- `public Set<String> keys() {`
- `public Set<String> keys(String path) {`
- `public boolean has(String path) {`
- `public FileConfiguration raw() {`
- `public File getFile() {`
- `public void shutdown() {`
- `public String asString() {`
- `public String asString(String def) {`
- `public int asInt() {`
- `public int asInt(int def) {`
- `public double asDouble() {`
- `public double asDouble(double def) {`
- `public boolean asBool() {`
- `public boolean asBool(boolean def) {`
- `public long asLong() {`
- `public long asLong(long def) {`
- `public List<String> asStringList() {`
- `public List<Integer> asIntList() {`
- `public List<?> asList() {`
- `public Object asObject() {`
- `public boolean exists() {`
- `public void set(Object value) {`
- `public Node child(String sub) {`
- `public Set<String> keys() {`
### `io.github.fragmer2.bslib.api.cooldown.Cooldowns`
- `public static CooldownBuilder of(Player player) {`
- `public static CooldownBuilder global() {`
- `public static boolean isOnCooldown(Player player, String key) {`
- `public static void clear() {`
- `public CooldownBuilder key(String key) {`
- `public CooldownBuilder seconds(int seconds) {`
- `public CooldownBuilder minutes(int minutes) {`
- `public CooldownBuilder duration(long amount, TimeUnit unit) {`
- `public CooldownBuilder bypass(String permission) {`
- `public boolean check() {`
- `public boolean check(Player player) {`
- `public long remaining() {`
- `public long remaining(TimeUnit unit) {`
- `public void set(long amount, TimeUnit unit) {`
- `public void clear() {`
### `io.github.fragmer2.bslib.api.debug.Debug`
- `public static void enable() { enabled = true; }`
- `public static void disable() { enabled = false; }`
- `public static boolean isEnabled() { return enabled; }`
- `public static void toggle() { enabled = !enabled; }`
- `public static void trackTime(String label, Runnable task) {`
- `public static long startTiming() {`
- `public static void endTiming(String label, long startNano) {`
- `public static TraceScope trace(String label) {`
- `public static <T> T trace(String label, Supplier<T> supplier) {`
- `public void close() {`
- `public static void report(CommandSender sender) {`
- `public static Map<String, String> timingSummary() {`
- `public static void doctor(CommandSender sender, org.bukkit.plugin.Plugin plugin) {`
- `public static void resetTimings() {`
### `io.github.fragmer2.bslib.api.debug.GuiInspector`
- `public static void enable(Player player) {`
- `public static void disable(Player player) {`
- `public static void toggle(Player player) {`
- `public static boolean isEnabled(Player player) {`
- `public static boolean inspect(Player player, Menu menu, int slot, Button button, MenuView view) {`
- `public static void recordRender(String menuClass, int slot, long nanos) {`
- `public static void clearStats() {`
- `public static void clearPlayer(Player player) {`
- `public static double averageRenderMillis() {`
### `io.github.fragmer2.bslib.api.di.EconomyImpl`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.di.Inject`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.di.ServiceContainer`
- `public <T> void register(Class<T> type, T instance) {`
- `public <T> void register(Class<T> type, String name, T instance) {`
- `public <T> void registerLazy(Class<T> type, Supplier<T> supplier) {`
- `public <T> void registerLazy(Class<T> type, String name, Supplier<T> supplier) {`
- `public <T> T get(Class<T> type) {`
- `public <T> T get(Class<T> type, String name) {`
- `public <T> T getOrDefault(Class<T> type, T defaultValue) {`
- `public boolean has(Class<?> type) {`
- `public boolean has(Class<?> type, String name) {`
- `public void inject(Object target) {`
- `public void clear() {`
### `io.github.fragmer2.bslib.api.event.Bus`
- `public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler) {`
- `public static <T> Subscription<T> subscribe(Class<T> eventType, Consumer<T> handler, Object owner) {`
- `public static <T> T publish(T event) {`
- `public static void unsubscribe(Subscription<?> sub) {`
- `public static void unsubscribeAll(Object owner) {`
- `public static void clear() {`
- `public void cancel() {`
### `io.github.fragmer2.bslib.api.event.Events`
- `public static <T extends Event> ListenerBuilder<T> listen(Class<T> eventType, Consumer<T> handler) {`
- `public static void unregisterAll(Plugin plugin) {`
- `public static void clear() {`
- `public ListenerBuilder<T> priority(EventPriority priority) {`
- `public ListenerBuilder<T> ignoreCancelled() {`
- `public ListenerBuilder<T> filter(Predicate<T> filter) {`
- `public ListenerBuilder<T> forPlayer(Player player) {`
- `public ListenerBuilder<T> expireAfter(int times) {`
- `public ManagedListener<T> register(Plugin plugin) {`
- `public void unregister() {`
- `public boolean isRegistered() {`
### `io.github.fragmer2.bslib.api.feature.Features`
- `public static void init(Plugin plugin, String fileName) {`
- `public static boolean isEnabled(String feature) {`
- `public static boolean isDisabled(String feature) {`
- `public static void enable(String feature) {`
- `public static void disable(String feature) {`
- `public static void toggle(String feature) {`
- `public static void set(String feature, boolean enabled) {`
- `public static void reset(String feature) {`
- `public static void resetAll() {`
- `public static void onChange(String feature, Consumer<Boolean> listener) {`
- `public static Set<String> all() {`
- `public static void clear() {`
### `io.github.fragmer2.bslib.api.interaction.Interact`
- `public static InteractionBuilder onRightClick() {`
- `public static InteractionBuilder onLeftClick() {`
- `public static InteractionBuilder onClick() {`
- `public static void init(Plugin plugin) {`
- `public static void clear() {`
- `public Player player() { return player; }`
- `public PlayerInteractEvent event() { return event; }`
- `public void cancel() { event.setCancelled(true); }`
- `public InteractionBuilder with(Material material) {`
- `public InteractionBuilder filter(Predicate<InteractionContext> filter) {`
- `public InteractionBuilder permission(String permission) {`
- `public InteractionBuilder allowDefault() {`
- `public InteractionBuilder handle(Consumer<InteractionContext> handler) {`
- `public void register(Plugin plugin) {`
- `public void onInteract(PlayerInteractEvent event) {`
### `io.github.fragmer2.bslib.api.item.Item`
- `public static Item of(Material material) {`
- `public Item amount(int amount) {`
- `public Item name(String text) {`
- `public Item lore(String... lines) {`
- `public Item lore(List<String> lines) {`
- `public Item glow() {`
- `public Item enchant(Enchantment enchantment, int level) {`
- `public Item unbreakable() {`
- `public Item addFlags(ItemFlag... flags) {`
- `public Item hideFlags() {`
- `public Item model(int id) {`
- `public Item skullOwner(String playerName) {`
- `public Item skullTexture(String base64) {`
- `public ItemStack build() {`
- `public ItemStack build(Player player) {`
### `io.github.fragmer2.bslib.api.item.Items`
- `public static String serialize(ItemStack item) {`
- `public static ItemStack deserialize(String data) {`
- `public static String serializeArray(ItemStack[] items) {`
- `public static ItemStack[] deserializeArray(String data) {`
### `io.github.fragmer2.bslib.api.lifecycle.OnDisable`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.lifecycle.OnEnable`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.lifecycle.OnReload`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.lifecycle.PlayerListener`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.lifecycle.PluginScanner`
- `public static ScanResult scan(JavaPlugin plugin, AutoScan config) {`
- `public static Object instantiate(Class<?> clazz) {`
- `public final List<Class<?>> commandClasses = new ArrayList<>();`
- `public final List<Class<?>> listenerClasses = new ArrayList<>();`
- `public final List<Class<?>> serviceClasses = new ArrayList<>();`
- `public final List<Class<?>> stateClasses = new ArrayList<>();`
### `io.github.fragmer2.bslib.api.lifecycle.ShopPlugin`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.menu.InventoryPolicy`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.menu.Menu`
- `public Menu(String title, int rows) {`
- `public String getTitle() { return title; }`
- `public int getRows() { return rows; }`
- `public Map<Integer, Button> getButtons() { return buttons; }`
- `public Menu secure() {`
- `public Menu policy(InventoryPolicy... policies) {`
- `public Set<InventoryPolicy> getPolicies() {`
- `public boolean hasPolicy(InventoryPolicy policy) {`
- `public void open(Player player) {`
- `public void openWithHistory(Player player) {`
- `public void onOpen(MenuView view) {}`
- `public void onClose(MenuView view) {}`
### `io.github.fragmer2.bslib.api.menu.MenuView`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.menu.ReactiveMenu`
- `public static ReactiveMenu create(String title, int rows) {`
- `public ReactiveMenu render(Consumer<RenderContext> renderFn) {`
- `public ReactiveMenu bind(Reactive<?>... reactives) {`
- `public ReactiveMenu secure() {`
- `public ReactiveMenu policy(InventoryPolicy... policies) {`
- `public Menu build() {`
- `public void open(Player player) {`
- `public ButtonBuilder button(int slot) {`
- `public void fill(Material material) {`
- `public void fill(ItemStack filler) {`
- `public void border(Material material) {`
- `public void border(ItemStack item) {`
- `public void row(int row, Button button) {`
- `public Menu menu() { return menu; }`
- `public ButtonBuilder item(ItemStack item) {`
- `public ButtonBuilder item(Material material) {`
- `public ButtonBuilder text(String text) {`
- `public ButtonBuilder lore(String... lines) {`
- `public ButtonBuilder onClick(Consumer<io.github.fragmer2.bslib.api.button.ClickContext> handler) {`
- `public ButtonBuilder closeOnClick() {`
- `public ButtonBuilder command(String command) {`
- `public ButtonBuilder done() {`
- `public static boolean isDeclarative(Menu menu) {`
- `public static boolean checkRerender(Menu menu) {`
### `io.github.fragmer2.bslib.api.menu.ShopMenu`
- `public LayoutMenu(String title, int rows, String... layout) {`
### `io.github.fragmer2.bslib.api.message.Msg`
- `public static void init(Plugin plugin, String messagesFile) {`
- `public static void addLanguage(String lang, Config config) {`
- `public static void setPrefix(String p) {`
- `public static void setDefaultLanguage(String lang) {`
- `public static void clear() {`
- `public static MessageBuilder key(String configKey) {`
- `public static MessageBuilder of(String text) {`
- `public MessageBuilder replace(String key, String value) {`
- `public MessageBuilder replace(String key, Object value) {`
- `public MessageBuilder prefixed() {`
- `public void send(CommandSender sender) {`
- `public void send(Player player) {`
- `public void sendTitle(Player player) {`
- `public void sendTitle(Player player, Duration fadeIn, Duration stay, Duration fadeOut) {`
- `public void sendSubtitle(Player player, String titleKey) {`
- `public void sendActionBar(Player player) {`
- `public void broadcast() {`
- `public String text(Player player) {`
- `public String text() {`
- `public Component component(Player player) {`
- `public Component component() {`
### `io.github.fragmer2.bslib.api.messaging.PluginMessageBus`
- `public static <T> void subscribe(String topic, Consumer<T> consumer) {`
- `public static void publish(String topic, Object payload) {`
- `public static void clearTopic(String topic) {`
- `public static void clearAll() {`
### `io.github.fragmer2.bslib.api.messaging.RemoteBridge`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.module.AbstractModule`
- `public void onEnable(ModuleContext context) {`
### `io.github.fragmer2.bslib.api.module.BSModule`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.module.ModuleContext`
- `public ModuleContext(Plugin plugin, ServiceContainer container) {`
- `public Plugin plugin() {`
- `public ServiceContainer container() {`
- `public Logger logger() {`
### `io.github.fragmer2.bslib.api.module.ModuleManager`
- `public ModuleManager(Plugin plugin, ServiceContainer container) {`
- `public void register(BSModule module) {`
- `public void registerFromRegistry() {`
- `public boolean enable(String moduleId) {`
- `public void enableAll() {`
- `public void disable(String moduleId) {`
- `public void disableAll() {`
- `public boolean isEnabled(String moduleId) {`
- `public BSModule getModule(String moduleId) {`
- `public Collection<BSModule> getModules() {`
### `io.github.fragmer2.bslib.api.module.ModuleRegistry`
- `public static List<Supplier<BSModule>> getModuleSuppliers() {`
### `io.github.fragmer2.bslib.api.module.ScoreboardModule`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.placeholder.PlaceholderRegistry`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.placeholder.Placeholders`
- `public static void setRegistry(PlaceholderRegistry reg) {`
- `public static void register(String key, Function<Player, String> replacer) {`
- `public static void register(String key, Supplier<String> supplier) {`
- `public static <T> void asPlaceholder(String key, Reactive<T> reactive) {`
- `public static <T> void asPlaceholder(String key, Function<Player, Reactive<T>> reactiveSupplier) {`
- `public static String apply(Player player, String text) {`
- `public static String apply(String text) {`
- `public void register(String key, Function<Player, String> replacer) {`
- `public String apply(Player player, String text) {`
### `io.github.fragmer2.bslib.api.reactive.Reactive`
- `public static <T> Reactive<T> of(T initial) {`
- `public static Reactive<Integer> ofInt(int initial) { return new Reactive<>(initial); }`
- `public static Reactive<Double> ofDouble(double initial) { return new Reactive<>(initial); }`
- `public static Reactive<Boolean> ofBool(boolean initial) { return new Reactive<>(initial); }`
- `public static Reactive<String> ofString(String initial) { return new Reactive<>(initial); }`
- `public T get() { return value; }`
- `public void set(T newValue) {`
- `public void update(Function<T, T> updater) {`
- `public void setSilent(T newValue) {`
- `public synchronized Reactive<T> beginBatch() {`
- `public Reactive<T> endBatch() {`
- `public long version() { return version; }`
- `public Reactive<T> onChange(BiConsumer<T, T> listener) {`
- `public Subscription subscribeChange(BiConsumer<T, T> listener) {`
- `public Reactive<T> onSet(Consumer<T> listener) {`
- `public Subscription subscribeSet(Consumer<T> listener) {`
- `public void clearListeners() {`
- `public <R> Reactive<R> map(Function<T, R> mapper) {`
- `public static <A, B, R> Reactive<R> combine(Reactive<A> a, Reactive<B> b,`
- `public Reactive<T> distinctUntilChanged() {`
- `public Reactive<T> throttle(long ticks) {`
- `public Reactive<T> debounce(long ticks) {`
- `public void destroy() {`
- `public boolean isDestroyed() {`
- `public Reactive<T> asPlaceholder(String key) {`
- `public static <T> void asPlayerPlaceholder(String key, Function<org.bukkit.entity.Player, Reactive<T>> supplier) {`
- `public static <T> Reactive<T> computeAsync(T defaultValue, java.util.function.Supplier<T> asyncLoader) {`
- `public void refreshAsync(java.util.function.Supplier<T> asyncLoader) {`
- `public void increment() {`
- `public void decrement() {`
- `public void add(Number amount) {`
- `public String toString() { return "Reactive{" + value + "}"; }`
### `io.github.fragmer2.bslib.api.reactive.ReactiveBinding`
- `public static ScoreboardBuilder scoreboard(Player player, String title) {`
- `public static BossBarBuilder bossbar(Player player, BossBar.Color color) {`
- `public static ActionBarBuilder actionbar(Player player) {`
- `public static void destroyAll(Player player) {`
- `public static void clear() {`
- `public static int activePlayerCount() {`
- `public static int activeBindingCount() {`
- `public ScoreboardBuilder line(int index, Reactive<String> value) {`
- `public ScoreboardBuilder line(int index, String staticText) {`
- `public Destroyable build(Plugin plugin) {`
- `public BossBarBuilder title(Reactive<String> title) {`
- `public BossBarBuilder title(String staticTitle) {`
- `public BossBarBuilder progress(Reactive<Float> progress) {`
- `public BossBarBuilder progress(float staticProgress) {`
- `public BossBarBuilder overlay(BossBar.Overlay overlay) {`
- `public Destroyable build(Plugin plugin) {`
- `public ActionBarBuilder text(Reactive<String> text) {`
- `public ActionBarBuilder text(String staticText) {`
- `public ActionBarBuilder interval(long ticks) {`
- `public Destroyable build(Plugin plugin) {`
### `io.github.fragmer2.bslib.api.reactive.ReactiveList`
- `public static <T> ReactiveList<T> of(T... items) {`
- `public static <T> ReactiveList<T> of(Collection<T> items) {`
- `public static <T> ReactiveList<T> empty() {`
- `public void add(T item) {`
- `public void add(int index, T item) {`
- `public boolean remove(T item) {`
- `public T remove(int index) {`
- `public void set(int index, T item) {`
- `public void clear() {`
- `public void addAll(Collection<T> items) {`
- `public void replaceAll(Collection<T> items) {`
- `public void sort(Comparator<? super T> comparator) {`
- `public T get(int index) { return list.get(index); }`
- `public int size() { return list.size(); }`
- `public boolean isEmpty() { return list.isEmpty(); }`
- `public boolean contains(T item) { return list.contains(item); }`
- `public int indexOf(T item) { return list.indexOf(item); }`
- `public List<T> asList() { return Collections.unmodifiableList(list); }`
- `public long version() { return version; }`
- `public ReactiveList<T> onChange(Consumer<List<T>> listener) {`
- `public java.util.function.Supplier<Object> asBindable() {`
- `public String toString() {`
### `io.github.fragmer2.bslib.api.reactive.ReactiveMap`
- `public static <K, V> ReactiveMap<K, V> empty() {`
- `public static <K, V> ReactiveMap<K, V> of(Map<K, V> initial) {`
- `public V put(K key, V value) {`
- `public V remove(K key) {`
- `public void putAll(Map<K, V> entries) {`
- `public void clear() {`
- `public V get(K key) { return map.get(key); }`
- `public V getOrDefault(K key, V def) { return map.getOrDefault(key, def); }`
- `public boolean containsKey(K key) { return map.containsKey(key); }`
- `public int size() { return map.size(); }`
- `public boolean isEmpty() { return map.isEmpty(); }`
- `public Map<K, V> asMap() { return Collections.unmodifiableMap(map); }`
- `public long version() { return version; }`
- `public ReactiveMap<K, V> onChange(Consumer<Map<K, V>> listener) {`
- `public ReactiveMap<K, V> onChange(BiConsumer<K, V> listener) {`
- `public java.util.function.Supplier<Object> asBindable() {`
### `io.github.fragmer2.bslib.api.reactive.Subscription`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.service.Services`
- `public static <T> void provide(Class<T> type, T instance) {`
- `public static <T> void provide(Class<T> type, String name, T instance) {`
- `public static <T> void provideLazy(Class<T> type, Supplier<T> supplier) {`
- `public static <T> void provideLazy(Class<T> type, String name, Supplier<T> supplier) {`
- `public static <T> T get(Class<T> type) {`
- `public static <T> T get(Class<T> type, String name) {`
- `public static <T> T getOrDefault(Class<T> type, T fallback) {`
- `public static boolean has(Class<?> type) {`
- `public static boolean has(Class<?> type, String name) {`
- `public static int registeredCount() {`
- `public static int lazyCount() {`
- `public static Set<String> serviceKeys() {`
- `public static <T> void onProvide(Class<T> type, Consumer<T> callback) {`
- `public static void remove(Class<?> type) {`
- `public static void remove(Class<?> type, String name) {`
- `public static void clear() {`
### `io.github.fragmer2.bslib.api.session.Session`
- `public Session set(String key, Object value) {`
- `public Session remove(String key) {`
- `public void clear() {`
- `public <T> T get(String key) {`
- `public <T> T get(String key, T defaultValue) {`
- `public String getString(String key) {`
- `public String getString(String key, String def) {`
- `public int getInt(String key) {`
- `public int getInt(String key, int def) {`
- `public double getDouble(String key) {`
- `public double getDouble(String key, double def) {`
- `public long getLong(String key) {`
- `public long getLong(String key, long def) {`
- `public boolean getBool(String key) {`
- `public boolean getBool(String key, boolean def) {`
- `public boolean has(String key) {`
- `public Map<String, Object> all() {`
### `io.github.fragmer2.bslib.api.session.Sessions`
- `public static void init(Plugin plugin) {`
- `public static Session of(Player player) {`
- `public static Session get(UUID uuid) {`
- `public static boolean has(Player player) {`
- `public static <T> void autoLoad(Plugin plugin, Class<T> stateClass) {`
- `public static void onJoin(Consumer<SessionContext> handler) {`
- `public static void onQuit(BiConsumer<Player, Session> handler) {`
- `public void onPlayerJoin(PlayerJoinEvent event) {`
- `public void onPlayerQuit(PlayerQuitEvent event) {`
- `public static void clear() {`
### `io.github.fragmer2.bslib.api.state.PlayerData`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.state.PlayerData`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.state.StateManager`
- `public StateManager(Plugin plugin, Class<T> type) {`
- `public T getOrCreate(String key) {`
- `public T get(String key) {`
- `public void load(String key, Consumer<T> callback) {`
- `public T loadSync(String key) {`
- `public void save(String key) {`
- `public void saveSync(String key) {`
- `public void saveAllDirty() {`
- `public void saveAll() {`
- `public void unload(String key) {`
- `public void delete(String key) {`
- `public boolean exists(String key) {`
- `public Set<String> cachedKeys() {`
- `public Set<String> allKeys() {`
- `public void shutdown() {`
### `io.github.fragmer2.bslib.api.state.StateSerializer`
- `public static Map<String, Object> serialize(Object state) {`
- `public static void deserialize(Object state, Map<String, Object> data) {`
- `public static String getKey(Object state) {`
- `public static void setKey(Object state, String keyValue) {`
### `io.github.fragmer2.bslib.api.state.States`
- `public static <T> void register(Plugin plugin, Class<T> type) {`
- `public static <T> void autoRegister(Plugin plugin, Class<T> type) {`
- `public static <T> T get(Plugin plugin, Class<T> type) {`
- `public static <T> void saveGlobal(Plugin plugin, Class<T> type) {`
- `public static <T> void loadGlobal(Plugin plugin, Class<T> type, Consumer<T> callback) {`
- `public static <T> T of(Player player, Class<T> type) {`
- `public static <T> T getOrCreate(Player player, Class<T> type) {`
- `public static <T> void load(Player player, Class<T> type, Consumer<T> callback) {`
- `public static <T> void save(Player player, Class<T> type) {`
- `public static <T> void unload(Player player, Class<T> type) {`
- `public static <T> T of(String key, Class<T> type) {`
- `public static <T> void load(String key, Class<T> type, Consumer<T> callback) {`
- `public static <T> void save(String key, Class<T> type) {`
- `public static <T> void unload(String key, Class<T> type) {`
- `public static <T> void delete(String key, Class<T> type) {`
- `public static <T> boolean exists(String key, Class<T> type) {`
- `public static <T> void saveAllDirty(Class<T> type) {`
- `public static <T> void saveAll(Class<T> type) {`
- `public static void unloadAll(Player player) {`
- `public static void shutdown() {`
- `public static void shutdown(Plugin plugin) {`
- `public static Set<Class<?>> registeredTypes() {`
### `io.github.fragmer2.bslib.api.task.FrameworkTask`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.task.SchedulerAdapter`
- _(no direct public signature lines detected)_
### `io.github.fragmer2.bslib.api.task.Tasks`
- `public static void init(Plugin p) {`
- `public static void setScheduler(SchedulerAdapter adapter) {`
- `public static int trackedTaskCount() {`
- `public static TaskBuilder sync() {`
- `public static TaskBuilder async() {`
- `public static TaskBuilder timer(long intervalTicks) {`
- `public static <T> io.github.fragmer2.bslib.api.thread.AsyncChain<T> compute(java.util.function.Supplier<T> supplier) {`
- `public TaskBuilder delay(long ticks) {`
- `public TaskBuilder repeat(long ticks) {`
- `public BukkitTask run(Runnable runnable) {`
- `public FrameworkTask runTracked(Runnable runnable) {`
- `public void run(Consumer<BukkitTask> consumer) {`
- `public FrameworkTask runTracked(Consumer<BukkitTask> consumer) {`
- `public int getTaskId() {`
- `public Plugin getOwner() {`
- `public boolean isSync() {`
- `public void cancel() {`
- `public boolean isCancelled() {`
### `io.github.fragmer2.bslib.api.thread.Async`
- `public static void init(Logger log) {`
- `public static void setDebug(boolean debug) {`
- `public static boolean isMainThread() {`
- `public static void ensureSync() {`
- `public static void ensureAsync() {`
- `public static void sync(Runnable task) {`
- `public static void async(Runnable task) {`
- `public static <T> T syncGet(Supplier<T> supplier) {`
- `public static <T> void run(Supplier<T> asyncWork, Consumer<T> syncCallback) {`
- `public static void guard() {`
### `io.github.fragmer2.bslib.api.thread.AsyncChain`
- `public AsyncChain(Supplier<T> computation) {`
- `public AsyncChain<T> thenSync(Consumer<T> callback) {`
- `public <R> AsyncChain<R> thenAsync(Function<T, R> transform) {`
- `public AsyncChain<T> onError(Consumer<Throwable> handler) {`
- `public AsyncChain<T> timeout(long amount, TimeUnit unit) {`
- `public AsyncChain<T> onTimeout(Runnable handler) {`
- `public void cancel() {`
- `public boolean isCancelled() {`
### `io.github.fragmer2.bslib.internal.error.FrameworkExceptionHandler`
- `public static void handle(Plugin plugin, Source source, Throwable error, Map<String, Object> context) {`